package com.ehansih.vulnscanner.scanner

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.ehansih.vulnscanner.data.models.AppLogger
import com.ehansih.vulnscanner.data.models.BluetoothDeviceInfo
import com.ehansih.vulnscanner.data.models.BluetoothScanResult
import com.ehansih.vulnscanner.data.models.SecurityFinding
import com.ehansih.vulnscanner.data.models.Severity

class BluetoothSecurityScanner(private val context: Context) {

    fun scan(): BluetoothScanResult {
        AppLogger.i("BluetoothScanner", "Starting Bluetooth security scan")

        val adapter = getAdapter()
        if (adapter == null) {
            AppLogger.i("BluetoothScanner", "Bluetooth not supported on this device")
            return BluetoothScanResult(
                isEnabled       = false,
                isDiscoverable  = false,
                pairedDevices   = emptyList(),
                findings        = emptyList(),
                riskScore       = 0
            )
        }

        val isEnabled      = runCatching { adapter.isEnabled }.getOrDefault(false)
        val isDiscoverable = runCatching { checkDiscoverable(adapter) }.getOrDefault(false)
        val pairedDevices  = runCatching { getPairedDevices(adapter) }.getOrDefault(emptyList())
        val findings       = mutableListOf<SecurityFinding>()

        if (!isEnabled) {
            AppLogger.i("BluetoothScanner", "Bluetooth is disabled — no risk")
            return BluetoothScanResult(
                isEnabled       = false,
                isDiscoverable  = false,
                pairedDevices   = emptyList(),
                findings        = findings,
                riskScore       = 0
            )
        }

        if (isDiscoverable) {
            findings.add(SecurityFinding(
                title          = "Bluetooth Discoverable Mode Active",
                detail         = "Your device is currently visible to nearby Bluetooth scanners. " +
                                 "Discoverable mode can expose device identity and enable Bluejacking or " +
                                 "unsolicited pairing requests.",
                severity       = Severity.MEDIUM,
                recommendation = "Turn off discoverable mode when not actively pairing. " +
                                 "Go to Settings → Connected devices → Bluetooth → make sure your device " +
                                 "is not set to be always discoverable. Discoverable mode typically auto-disables after 2 minutes."
            ))
        }

        if (pairedDevices.size > 10) {
            findings.add(SecurityFinding(
                title          = "Large Number of Paired Bluetooth Devices (${pairedDevices.size})",
                detail         = "A high number of paired devices increases the attack surface. " +
                                 "Old paired devices from public kiosks or unknown parties can be used " +
                                 "for proximity tracking.",
                severity       = Severity.LOW,
                recommendation = "Settings → Connected devices → Previously connected devices. " +
                                 "Remove (forget) any device you no longer use or do not recognise."
            ))
        }

        val unknownDevices = pairedDevices.filter {
            it.name.isBlank() || it.name == it.address || it.name.startsWith("00:") ||
            it.name.startsWith("Unknown")
        }
        if (unknownDevices.isNotEmpty()) {
            findings.add(SecurityFinding(
                title          = "Unknown Paired Bluetooth Devices (${unknownDevices.size})",
                detail         = "Devices with missing or suspicious names may represent unwanted pairings: " +
                                 unknownDevices.take(5).joinToString { "${it.name} [${it.address}]" },
                severity       = Severity.MEDIUM,
                recommendation = "Remove unrecognised paired devices in Settings → Connected devices → " +
                                 "Previously connected devices → tap device → Forget."
            ))
        }

        if (isEnabled && pairedDevices.isEmpty()) {
            findings.add(SecurityFinding(
                title          = "Bluetooth Enabled with No Paired Devices",
                detail         = "Bluetooth is on but you have no paired devices. " +
                                 "Leaving Bluetooth on unnecessarily exposes you to proximity-based attacks.",
                severity       = Severity.LOW,
                recommendation = "Disable Bluetooth when not in use: swipe down from the status bar and " +
                                 "tap the Bluetooth toggle."
            ))
        }

        val riskScore = calculateRiskScore(isDiscoverable, pairedDevices.size, unknownDevices.size)

        AppLogger.i(
            "BluetoothScanner",
            "Scan complete — enabled=$isEnabled discoverable=$isDiscoverable " +
            "paired=${pairedDevices.size} riskScore=$riskScore"
        )

        return BluetoothScanResult(
            isEnabled       = isEnabled,
            isDiscoverable  = isDiscoverable,
            pairedDevices   = pairedDevices,
            findings        = findings,
            riskScore       = riskScore
        )
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun getAdapter(): BluetoothAdapter? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val bm = context.getSystemService(BluetoothManager::class.java)
            bm?.adapter
        } else {
            @Suppress("DEPRECATION")
            BluetoothAdapter.getDefaultAdapter()
        }
    }

    private fun checkDiscoverable(adapter: BluetoothAdapter): Boolean {
        return runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (context.checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) !=
                    PackageManager.PERMISSION_GRANTED) {
                    AppLogger.w("BluetoothScanner", "BLUETOOTH_CONNECT permission not granted — cannot check discoverability")
                    return@runCatching false
                }
            }
            adapter.scanMode == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE
        }.getOrElse { e ->
            AppLogger.e("BluetoothScanner", "Error checking discoverable state: ${e.message}", e)
            false
        }
    }

    private fun getPairedDevices(adapter: BluetoothAdapter): List<BluetoothDeviceInfo> {
        return runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (context.checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) !=
                    PackageManager.PERMISSION_GRANTED) {
                    AppLogger.w("BluetoothScanner", "BLUETOOTH_CONNECT permission not granted — cannot list paired devices")
                    return@runCatching emptyList()
                }
            }

            val bonded = adapter.bondedDevices ?: return@runCatching emptyList<BluetoothDeviceInfo>()

            bonded.map { device ->
                val name = runCatching {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        if (context.checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) ==
                            PackageManager.PERMISSION_GRANTED) {
                            device.name ?: "Unknown"
                        } else "Unknown"
                    } else {
                        @Suppress("MissingPermission")
                        device.name ?: "Unknown"
                    }
                }.getOrDefault("Unknown")

                val address = runCatching { device.address ?: "N/A" }.getOrDefault("N/A")

                val deviceClass = runCatching {
                    classToString(device.bluetoothClass?.deviceClass ?: 0)
                }.getOrDefault("Unknown")

                val bondState = runCatching {
                    bondStateToString(device.bondState)
                }.getOrDefault("Unknown")

                BluetoothDeviceInfo(
                    name        = name,
                    address     = address,
                    deviceClass = deviceClass,
                    bondState   = bondState
                )
            }
        }.getOrElse { e ->
            AppLogger.e("BluetoothScanner", "Error fetching paired devices: ${e.message}", e)
            emptyList()
        }
    }

    private fun classToString(deviceClass: Int): String = when (deviceClass) {
        BluetoothClass.Device.PHONE_SMART              -> "Smartphone"
        BluetoothClass.Device.COMPUTER_LAPTOP          -> "Laptop"
        BluetoothClass.Device.COMPUTER_DESKTOP         -> "Desktop"
        BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES   -> "Headphones"
        BluetoothClass.Device.AUDIO_VIDEO_LOUDSPEAKER  -> "Speaker"
        BluetoothClass.Device.WEARABLE_WRIST_WATCH     -> "Smartwatch"
        BluetoothClass.Device.PERIPHERAL_KEYBOARD      -> "Keyboard"
        BluetoothClass.Device.PERIPHERAL_POINTING      -> "Mouse/Pointer"
        BluetoothClass.Device.TOY_VEHICLE               -> "Vehicle Toy"
        else -> "Device class 0x${deviceClass.toString(16)}"
    }

    private fun bondStateToString(state: Int): String = when (state) {
        BluetoothDevice.BOND_BONDED  -> "Bonded"
        BluetoothDevice.BOND_BONDING -> "Bonding"
        BluetoothDevice.BOND_NONE    -> "Not bonded"
        else                         -> "Unknown ($state)"
    }

    private fun calculateRiskScore(isDiscoverable: Boolean, pairedCount: Int, unknownCount: Int): Int {
        var score = 0
        if (isDiscoverable) score += 30
        score += (pairedCount * 2).coerceAtMost(20)
        score += (unknownCount * 10).coerceAtMost(30)
        return score.coerceAtMost(100)
    }
}
