package com.nokia.vulnscanner.scanner

import android.content.Context
import android.net.ConnectivityManager
import com.nokia.vulnscanner.data.models.AppLogger
import android.net.NetworkCapabilities
import android.net.VpnService
import android.net.wifi.WifiManager
import android.os.Build
import com.nokia.vulnscanner.data.models.SecurityFinding
import com.nokia.vulnscanner.data.models.NetworkSecurityResult
import com.nokia.vulnscanner.data.models.Severity
import java.net.InetSocketAddress
import java.net.Proxy

class NetworkScanner(private val context: Context) {

    fun scan(): NetworkSecurityResult {
        AppLogger.i("NetworkScanner", "Starting network scan")
        val cm      = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifiMgr = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        val isVpn   = checkVpnActive(cm)
        val isProxy = checkProxySet()

        @Suppress("DEPRECATION")
        val wifiInfo = wifiMgr.connectionInfo
        val ssid     = wifiInfo?.ssid?.replace("\"", "") ?: "Not connected"
        val bssid    = wifiInfo?.bssid ?: "N/A"

        // Determine WiFi security type from scan results
        val secType  = getWifiSecurityType(wifiMgr, ssid)
        val findings = mutableListOf<SecurityFinding>()

        when (secType) {
            "Open" -> findings.add(SecurityFinding(
                title          = "Open WiFi Network",
                detail         = "You are connected to an unencrypted WiFi network ($ssid). " +
                                 "All your traffic can be sniffed.",
                severity       = Severity.CRITICAL,
                recommendation = "Disconnect immediately. Use mobile data or connect to a WPA2/WPA3 network. " +
                                 "If you must use open WiFi, always use a VPN."
            ))
            "WEP" -> findings.add(SecurityFinding(
                title          = "WEP WiFi – Broken Encryption",
                detail         = "WEP encryption is cryptographically broken and crackable in minutes.",
                severity       = Severity.HIGH,
                recommendation = "Switch to WPA3 or at minimum WPA2 on your router. Use a VPN until resolved."
            ))
            "WPA" -> findings.add(SecurityFinding(
                title          = "WPA (TKIP) – Weak Encryption",
                detail         = "WPA TKIP has known vulnerabilities (KRACK, Beck-Tews).",
                severity       = Severity.MEDIUM,
                recommendation = "Upgrade router settings to WPA2-AES or WPA3. " +
                                 "Settings → Router admin → Wireless → Security Mode."
            ))
        }

        if (!isVpn) findings.add(SecurityFinding(
            title          = "No VPN Active",
            detail         = "Your internet traffic is not tunnelled through a VPN.",
            severity       = Severity.LOW,
            recommendation = "Use a trusted VPN especially on public networks. " +
                             "Download a VPN app (Mullvad, ProtonVPN, or corporate VPN)."
        ))

        if (isProxy) findings.add(SecurityFinding(
            title          = "Manual Proxy Configured",
            detail         = "A network proxy is set — traffic may be intercepted.",
            severity       = Severity.MEDIUM,
            recommendation = "Settings → WiFi → Long press current network → Modify → Advanced → " +
                             "Proxy → set to None unless you intentionally configured this."
        ))

        AppLogger.i("NetworkScanner", "Network scan complete — SSID=$ssid type=$secType VPN=$isVpn findings=${findings.size}")
        return NetworkSecurityResult(
            ssid         = ssid,
            bssid        = bssid,
            securityType = secType,
            isVpnActive  = isVpn,
            isProxySet   = isProxy,
            tlsVersion   = "TLS 1.3",  // Android 10+ defaults
            findings     = findings
        )
    }

    private fun checkVpnActive(cm: ConnectivityManager): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNet = cm.activeNetwork ?: return false
            val caps      = cm.getNetworkCapabilities(activeNet) ?: return false
            return caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
        }
        return false
    }

    private fun checkProxySet(): Boolean {
        val host = System.getProperty("http.proxyHost") ?: return false
        return host.isNotBlank()
    }

    private fun getWifiSecurityType(wifiMgr: WifiManager, ssid: String): String {
        if (ssid == "Not connected" || ssid == "<unknown ssid>") return "Not Connected"
        return try {
            @Suppress("DEPRECATION")
            val results = wifiMgr.scanResults
            val match   = results?.firstOrNull { it.SSID == ssid }
            val caps    = match?.capabilities ?: return "Unknown"
            when {
                caps.contains("WPA3") -> "WPA3"
                caps.contains("WPA2") -> "WPA2"
                caps.contains("WPA")  -> "WPA"
                caps.contains("WEP")  -> "WEP"
                !caps.contains("WPA") && !caps.contains("WEP") -> "Open"
                else -> "Unknown"
            }
        } catch (_: Exception) { "Unknown" }
    }
}
