# VulnScanner — Android Device Vulnerability Scanner

A native Android app that scans your phone for security vulnerabilities, risky apps, weak network configuration, and outdated OS patches — with clear colour-coded results and step-by-step fix instructions.

---

## Screenshots

| Dashboard | Device Security | App Scanner | Debug Logs |
|-----------|----------------|-------------|------------|
| Overall risk score + summary | Checklist with GREEN/RED status | Every app vs NVD CVE database | Live log viewer with share |

---

## Features

| Module | What it checks |
|--------|---------------|
| **Device Security** | Root status, developer mode, USB debugging, screen lock, storage encryption, biometrics, Play Protect, security patch age |
| **Installed Apps** | All apps scanned against [NVD CVE database](https://nvd.nist.gov/), dangerous permissions granted, sideloaded/unknown-source apps |
| **OS Vulnerabilities** | Android version matched against known CVEs |
| **Network Security** | WiFi encryption type (WEP/WPA/WPA2/WPA3/Open), VPN active, proxy configuration |
| **Attack Intelligence** | Real-world mobile attack campaigns (FluBot, Pegasus, Cerberus, etc.) matched to your scan findings with documented financial + data-breach impact |
| **Debug Logs** | Live in-app log viewer — filter by level, copy or share for support |

---

## Colour Coding

| Colour | Meaning |
|--------|---------|
| 🟢 **Green** | Secure / No issues |
| 🟡 **Yellow** | Medium risk |
| 🟠 **Orange** | High risk |
| 🔴 **Red** | Critical — immediate action needed |

Every finding includes a **"How to fix"** section with exact Settings menu steps for your device.

---

## Installation (Sideload APK)

### Requirements
- Android 8.0+ (API 26+)
- Internet connection (for CVE lookups)

### Steps

1. **Download the APK** from [GitHub Releases](https://github.com/ehansih/ehansih/releases)
   - Look for the latest `VulnScanner-vX.X.X-debug.apk`

2. **Transfer to your phone**
   - Send via WhatsApp/Telegram to yourself, or copy via USB

3. **Allow installation from unknown sources**
   - Go to **Settings → Apps → Special app access → Install unknown apps**
   - Select the app you'll use to open the APK (e.g. Files, Chrome)
   - Toggle **Allow from this source** → ON

4. **Tap the APK file** → **Install** → **Open**

5. **Run a scan**
   - Tap **START FULL SCAN** on the Dashboard
   - Wait for all modules to complete (app scan may take a few minutes depending on number of installed apps)

> **Note:** After installing, you can turn off "Install unknown apps" again for security.

---

## Build from Source

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17
- Android SDK 34

### Steps

```bash
# Clone the repo
git clone https://github.com/ehansih/ehansih.git
cd ehansih/android-vuln-scanner

# Open in Android Studio
# File → Open → select android-vuln-scanner/

# OR build via command line
./gradlew assembleDebug

# APK output
app/build/outputs/apk/debug/app-debug.apk
```

### Install directly to connected device
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## Permissions

| Permission | Why needed |
|-----------|------------|
| `INTERNET` | Query NVD CVE database |
| `ACCESS_NETWORK_STATE` | Detect active network type |
| `ACCESS_WIFI_STATE` | Read WiFi security configuration |
| `QUERY_ALL_PACKAGES` | Scan all installed apps |
| `USE_BIOMETRIC` | Check if biometric lock is enrolled |
| `USE_FINGERPRINT` | Fingerprint check on older Android versions |

---

## Architecture

```
android-vuln-scanner/
├── app/src/main/java/com/ehansih/vulnscanner/
│   ├── MainActivity.kt          # Entry point, bottom nav
│   ├── data/
│   │   ├── api/                 # NVD REST API (Retrofit)
│   │   ├── db/                  # Room local CVE cache
│   │   └── models/              # Data classes + AppLogger
│   ├── scanner/
│   │   ├── AppScanner.kt             # Installed app + CVE lookup (with 429 backoff)
│   │   ├── AttackIntelligenceEngine.kt  # Maps scan findings → real-world attacks
│   │   ├── DeviceSecurityScanner.kt  # OS/device checks
│   │   ├── NetworkScanner.kt         # WiFi/VPN checks
│   │   └── ScanOrchestrator.kt       # Coordinates all scanners
│   ├── ui/
│   │   ├── screens/             # Compose screens (Home, Device, Apps, Network, Threats, Logs)
│   │   ├── components/          # SeverityBadge, ScoreGauge
│   │   └── theme/               # Dark theme, severity colours
│   └── viewmodel/
│       └── ScanViewModel.kt     # StateFlow → UI state
```

**Stack:** Kotlin · Jetpack Compose · MVVM · Room · Retrofit · Coroutines · Material 3

---

## Releases

| Version | Changes |
|---------|---------|
| **v1.2.0** | **Performance & NVD fix:** Device scan + network scan now run in parallel. System apps filtered out (only user-installed + updated apps scanned — significantly fewer apps). **NVD rate-limit fix:** corrected delay to 6200ms unauthenticated (was 1500ms — was exceeding 5 req/30s limit causing 429 blocks). NVD API key support added (`NVD_API_KEY` in build.gradle.kts) — 700ms delay with key (50 req/30s). |
| v1.1.0 | versionCode bump / internal build |
| v1.0.4 | **Attack Intelligence Dashboard** — real-world attack campaigns matched to your scan findings with financial and data-breach impact. **Bug fixes:** NVD rate-limit 429 exponential backoff (2s→4s→8s), DNS-failure early-exit, CVE network warning banner on Dashboard |
| v1.0.3 | Package name cleanup (removed Nokia references) |
| v1.0.2 | In-app debug log screen, real-time scan progress per app |
| v1.0.1 | Fix: biometric permission crash on scan start |
| v1.0.0 | Initial release |

---

## License

For personal and internal security use. CVE data sourced from [NVD (NIST)](https://nvd.nist.gov/) under public domain.
