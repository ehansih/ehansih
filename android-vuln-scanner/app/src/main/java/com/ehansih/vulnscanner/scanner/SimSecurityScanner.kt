package com.ehansih.vulnscanner.scanner

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.TelephonyManager
import com.ehansih.vulnscanner.data.models.AppLogger
import com.ehansih.vulnscanner.data.models.SecurityFinding
import com.ehansih.vulnscanner.data.models.Severity
import com.ehansih.vulnscanner.data.models.SimSecurityResult

class SimSecurityScanner(private val context: Context) {

    fun scan(): SimSecurityResult {
        AppLogger.i("SimSecurityScanner", "Starting SIM/telecom security scan")

        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val hasPhonePermission = context.checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE) ==
                PackageManager.PERMISSION_GRANTED

        val simState       = getSimState(tm)
        val operatorName   = getOperatorName(tm, hasPhonePermission)
        val countryIso     = getCountryIso(tm)
        val networkType    = getNetworkTypeName(tm, hasPhonePermission)
        val isRoaming      = getIsRoaming(tm, hasPhonePermission)
        val phoneCount     = getPhoneCount(tm)
        val isLegacyNet    = isLegacyNetwork(networkType)

        val findings = mutableListOf<SecurityFinding>()

        // SIM state findings
        when (simState) {
            "PIN_REQUIRED" -> findings.add(SecurityFinding(
                title          = "SIM Card PIN Required",
                detail         = "The SIM is locked and waiting for a PIN. This is expected after a reboot " +
                                 "if SIM PIN lock is enabled.",
                severity       = Severity.INFO,
                recommendation = "Enter your SIM PIN to restore mobile connectivity. If you did not set a SIM PIN, " +
                                 "contact your carrier."
            ))
            "PUK_REQUIRED" -> findings.add(SecurityFinding(
                title          = "SIM Card PUK Required — Locked Out",
                detail         = "The SIM has been blocked after too many incorrect PIN attempts. " +
                                 "PUK (Personal Unblocking Key) is required.",
                severity       = Severity.HIGH,
                recommendation = "Contact your mobile carrier to obtain your PUK code. " +
                                 "Do not guess the PUK — 10 wrong attempts will permanently block the SIM."
            ))
            "ABSENT" -> findings.add(SecurityFinding(
                title          = "No SIM Card Present",
                detail         = "No SIM card was detected. Emergency calls only.",
                severity       = Severity.MEDIUM,
                recommendation = "Insert a SIM card to restore mobile network functionality."
            ))
            "NETWORK_LOCKED" -> findings.add(SecurityFinding(
                title          = "SIM Network Locked",
                detail         = "The SIM is locked to a specific carrier network.",
                severity       = Severity.LOW,
                recommendation = "Contact your carrier if you need to unlock the SIM for use on other networks."
            ))
        }

        // Roaming findings
        if (isRoaming) {
            findings.add(SecurityFinding(
                title          = "Device is Roaming",
                detail         = "Your device is connected to a foreign or roaming network. " +
                                 "Roaming networks may have different security standards. " +
                                 "Additionally, roaming charges can be exploited by subscription fraud malware.",
                severity       = Severity.LOW,
                recommendation = "Disable mobile data roaming if not needed: Settings → Network & internet → " +
                                 "Mobile network → Roaming → Off."
            ))
        }

        // 2G/legacy network findings (IMSI catcher risk)
        if (isLegacyNet) {
            findings.add(SecurityFinding(
                title          = "Device Connected to 2G/EDGE Network (IMSI Catcher Risk)",
                detail         = "Your device is on a 2G or EDGE network ($networkType). " +
                                 "IMSI catchers (Stingrays) force devices onto 2G to intercept calls and SMS. " +
                                 "2G does not authenticate the network to the phone, enabling man-in-the-middle attacks.",
                severity       = Severity.HIGH,
                recommendation = "On Android 12+, disable 2G in Settings → Network & internet → SIMs → " +
                                 "Allow 2G → Off. This prevents IMSI catcher downgrade attacks. " +
                                 "Avoid making sensitive calls or sending 2FA SMS while on 2G."
            ))
        }

        // No SIM PIN configured check (heuristic — can only detect if SIM ready without PIN lock)
        if (simState == "READY" && !hasPhonePermission) {
            AppLogger.d("SimSecurityScanner", "READ_PHONE_STATE not granted — some checks limited")
        }

        val riskScore = calculateRiskScore(simState, isRoaming, isLegacyNet)

        AppLogger.i(
            "SimSecurityScanner",
            "Scan complete — state=$simState operator=$operatorName network=$networkType " +
            "roaming=$isRoaming legacy=$isLegacyNet riskScore=$riskScore"
        )

        return SimSecurityResult(
            simState          = simState,
            operatorName      = operatorName,
            countryIso        = countryIso,
            networkType       = networkType,
            isRoaming         = isRoaming,
            isOnLegacyNetwork = isLegacyNet,
            phoneCount        = phoneCount,
            findings          = findings,
            riskScore         = riskScore
        )
    }

    // ── SIM state ─────────────────────────────────────────────────────────────

    private fun getSimState(tm: TelephonyManager): String {
        return runCatching {
            when (tm.simState) {
                TelephonyManager.SIM_STATE_ABSENT           -> "ABSENT"
                TelephonyManager.SIM_STATE_PIN_REQUIRED     -> "PIN_REQUIRED"
                TelephonyManager.SIM_STATE_PUK_REQUIRED     -> "PUK_REQUIRED"
                TelephonyManager.SIM_STATE_NETWORK_LOCKED   -> "NETWORK_LOCKED"
                TelephonyManager.SIM_STATE_READY            -> "READY"
                TelephonyManager.SIM_STATE_NOT_READY        -> "NOT_READY"
                TelephonyManager.SIM_STATE_PERM_DISABLED    -> "PERMANENTLY_DISABLED"
                TelephonyManager.SIM_STATE_CARD_IO_ERROR    -> "CARD_IO_ERROR"
                TelephonyManager.SIM_STATE_CARD_RESTRICTED  -> "CARD_RESTRICTED"
                TelephonyManager.SIM_STATE_UNKNOWN          -> "UNKNOWN"
                else -> "UNKNOWN"
            }
        }.getOrElse { e ->
            AppLogger.e("SimSecurityScanner", "Error getting SIM state: ${e.message}", e)
            "UNKNOWN"
        }
    }

    // ── Operator ──────────────────────────────────────────────────────────────

    private fun getOperatorName(tm: TelephonyManager, hasPermission: Boolean): String {
        return runCatching {
            tm.networkOperatorName?.takeIf { it.isNotBlank() } ?: "Unknown"
        }.getOrDefault("Unknown")
    }

    private fun getCountryIso(tm: TelephonyManager): String {
        return runCatching {
            tm.networkCountryIso?.uppercase()?.takeIf { it.isNotBlank() } ?: "Unknown"
        }.getOrDefault("Unknown")
    }

    // ── Network type ──────────────────────────────────────────────────────────

    private fun getNetworkTypeName(tm: TelephonyManager, hasPermission: Boolean): String {
        if (!hasPermission) {
            AppLogger.w("SimSecurityScanner", "READ_PHONE_STATE not granted — network type unavailable")
            return "Unknown (permission required)"
        }
        return runCatching {
            @Suppress("MissingPermission")
            val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tm.dataNetworkType
            } else {
                @Suppress("DEPRECATION")
                tm.networkType
            }
            networkTypeToString(type)
        }.getOrElse { e ->
            AppLogger.e("SimSecurityScanner", "Error getting network type: ${e.message}", e)
            "Unknown"
        }
    }

    private fun networkTypeToString(type: Int): String = when (type) {
        TelephonyManager.NETWORK_TYPE_GPRS     -> "GPRS (2G)"
        TelephonyManager.NETWORK_TYPE_EDGE     -> "EDGE (2G)"
        TelephonyManager.NETWORK_TYPE_CDMA     -> "CDMA (2G)"
        TelephonyManager.NETWORK_TYPE_1xRTT    -> "1xRTT (2G)"
        TelephonyManager.NETWORK_TYPE_IDEN     -> "iDEN (2G)"
        TelephonyManager.NETWORK_TYPE_UMTS     -> "UMTS (3G)"
        TelephonyManager.NETWORK_TYPE_EVDO_0   -> "EVDO rev.0 (3G)"
        TelephonyManager.NETWORK_TYPE_EVDO_A   -> "EVDO rev.A (3G)"
        TelephonyManager.NETWORK_TYPE_EVDO_B   -> "EVDO rev.B (3G)"
        TelephonyManager.NETWORK_TYPE_HSDPA    -> "HSDPA (3G)"
        TelephonyManager.NETWORK_TYPE_HSUPA    -> "HSUPA (3G)"
        TelephonyManager.NETWORK_TYPE_HSPA     -> "HSPA (3G)"
        TelephonyManager.NETWORK_TYPE_HSPAP    -> "HSPA+ (3G)"
        TelephonyManager.NETWORK_TYPE_LTE      -> "LTE (4G)"
        TelephonyManager.NETWORK_TYPE_NR       -> "NR (5G)"
        TelephonyManager.NETWORK_TYPE_UNKNOWN  -> "Unknown"
        else                                   -> "Unknown ($type)"
    }

    private fun isLegacyNetwork(networkTypeName: String): Boolean {
        return networkTypeName.contains("2G") || networkTypeName.contains("GPRS") ||
               networkTypeName.contains("EDGE") || networkTypeName.contains("CDMA") ||
               networkTypeName.contains("1xRTT") || networkTypeName.contains("iDEN")
    }

    // ── Roaming ───────────────────────────────────────────────────────────────

    private fun getIsRoaming(tm: TelephonyManager, hasPermission: Boolean): Boolean {
        return runCatching {
            tm.isNetworkRoaming
        }.getOrElse { e ->
            AppLogger.e("SimSecurityScanner", "Error checking roaming state: ${e.message}", e)
            false
        }
    }

    // ── Phone count ───────────────────────────────────────────────────────────

    private fun getPhoneCount(tm: TelephonyManager): Int {
        return runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                tm.activeModemCount
            } else {
                @Suppress("DEPRECATION")
                tm.phoneCount
            }
        }.getOrDefault(1)
    }

    // ── Risk score ────────────────────────────────────────────────────────────

    private fun calculateRiskScore(simState: String, isRoaming: Boolean, isLegacy: Boolean): Int {
        var score = 0
        if (simState == "ABSENT")          score += 10
        if (simState == "PUK_REQUIRED")    score += 15
        if (simState == "NETWORK_LOCKED")  score += 5
        if (isRoaming)                     score += 10
        if (isLegacy)                      score += 35
        return score.coerceAtMost(100)
    }
}
