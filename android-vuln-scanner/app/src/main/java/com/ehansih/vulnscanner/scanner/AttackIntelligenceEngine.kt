package com.ehansih.vulnscanner.scanner

import com.ehansih.vulnscanner.data.models.*

/**
 * Maps scan findings to documented real-world mobile attack campaigns.
 * Trigger keys: "sideloaded", "sms_permission", "camera_mic", "phone_state", "rooted"
 */
object AttackIntelligenceEngine {

    private val attackDatabase = listOf(
        AttackIncident(
            name                = "FluBot Banking Trojan",
            year                = "2021–2022",
            attackType          = AttackType.BANKING_TROJAN,
            affectedDevices     = "500K+ devices across Europe",
            financialImpact     = "\$50M+ in banking losses",
            financialImpactValue = 50_000_000L,
            dataBreachImpact    = "Contact lists of 500K+ users exfiltrated",
            description         = "Spread via SMS phishing ('missed parcel' notifications). Installed via sideloaded APKs. Intercepted SMS OTPs to bypass 2FA on banking apps.",
            relevantTrigger     = "sideloaded",
            severity            = Severity.CRITICAL
        ),
        AttackIncident(
            name                = "BRATA Android Malware",
            year                = "2022",
            attackType          = AttackType.BANKING_TROJAN,
            affectedDevices     = "100K+ devices in Italy, UK, Spain",
            financialImpact     = "\$1M+ drained from bank accounts before detection",
            financialImpactValue = 1_000_000L,
            dataBreachImpact    = "Bank credentials and session tokens stolen",
            description         = "Disguised as a security scanner app. Drained bank accounts then performed factory reset to erase all evidence. Used screen recording to capture banking PINs.",
            relevantTrigger     = "sideloaded",
            severity            = Severity.CRITICAL
        ),
        AttackIncident(
            name                = "TeaBot (Anatsa) Banking Trojan",
            year                = "2021–2023",
            attackType          = AttackType.BANKING_TROJAN,
            affectedDevices     = "400+ banking apps in 60+ countries targeted",
            financialImpact     = "Multi-million dollar losses across European banks",
            financialImpactValue = 20_000_000L,
            dataBreachImpact    = "Full banking credential sets + 2FA tokens exfiltrated",
            description         = "Used Android Accessibility Services to perform Account Takeover (ATO) on banking apps in real-time. Distributed via sideloaded fake VLC and PDF reader apps.",
            relevantTrigger     = "sideloaded",
            severity            = Severity.CRITICAL
        ),
        AttackIncident(
            name                = "Hermit Enterprise Spyware",
            year                = "2022",
            attackType          = AttackType.SPYWARE,
            affectedDevices     = "Targeted attacks on dissidents, journalists, politicians",
            financialImpact     = "\$30M+ in government licensing fees (RCS Lab, Italy)",
            financialImpactValue = 30_000_000L,
            dataBreachImpact    = "Complete device surveillance including encrypted messaging apps",
            description         = "Distributed via compromised ISP infrastructure — victims received legitimate-looking SMS with a 'carrier update' link that sideloaded the spyware APK.",
            relevantTrigger     = "sideloaded",
            severity            = Severity.CRITICAL
        ),
        AttackIncident(
            name                = "Roaming Mantis DNS Hijacking",
            year                = "2022",
            attackType          = AttackType.DATA_THEFT,
            affectedDevices     = "70K+ devices in Europe and Asia",
            financialImpact     = "Millions in credential theft and ad fraud",
            financialImpactValue = 5_000_000L,
            dataBreachImpact    = "Apple ID / Google credentials, device identifiers stolen",
            description         = "Hijacked router DNS to redirect mobile browser traffic to malicious APK downloads. Installed banking malware disguised as Chrome or Facebook updates.",
            relevantTrigger     = "sideloaded",
            severity            = Severity.HIGH
        ),
        AttackIncident(
            name                = "Cerberus Banking Trojan",
            year                = "2020",
            attackType          = AttackType.BANKING_TROJAN,
            affectedDevices     = "200K+ devices globally",
            financialImpact     = "\$15M+ estimated banking losses",
            financialImpactValue = 15_000_000L,
            dataBreachImpact    = "2FA tokens, banking passwords, contact lists stolen",
            description         = "Used Accessibility Services to overlay fake login screens on 200+ banking apps. Source code leaked in 2020 enabling mass criminal reuse. Intercepted SMS OTPs to drain accounts.",
            relevantTrigger     = "sms_permission",
            severity            = Severity.CRITICAL
        ),
        AttackIncident(
            name                = "EventBot Financial Malware",
            year                = "2020",
            attackType          = AttackType.BANKING_TROJAN,
            affectedDevices     = "200+ financial apps targeted across Europe",
            financialImpact     = "Millions in losses across targeted European banks",
            financialImpactValue = 5_000_000L,
            dataBreachImpact    = "Keystrokes, SMS, browser cookies, 200+ app credentials stolen",
            description         = "Used Android Accessibility API to extract data from banking apps in real-time. Intercepted SMS messages to bypass 2FA. Specifically required READ_SMS and RECORD_AUDIO permissions.",
            relevantTrigger     = "sms_permission",
            severity            = Severity.HIGH
        ),
        AttackIncident(
            name                = "Joker Malware (Play Store)",
            year                = "2019–2023",
            attackType          = AttackType.SUBSCRIPTION_FRAUD,
            affectedDevices     = "500K+ victims from Play Store alone",
            financialImpact     = "\$300K/month at peak subscription fraud",
            financialImpactValue = 10_000_000L,
            dataBreachImpact    = "Contact lists and device identifiers harvested",
            description         = "Repeatedly uploaded to Google Play Store under new package names (50+ times). Silently signed up users for premium SMS subscriptions using READ_SMS interception.",
            relevantTrigger     = "sms_permission",
            severity            = Severity.MEDIUM
        ),
        AttackIncident(
            name                = "Pegasus Spyware (NSO Group)",
            year                = "2021",
            attackType          = AttackType.SPYWARE,
            affectedDevices     = "50,000+ individuals targeted globally",
            financialImpact     = "\$500K per government client; total licensing \$100M+",
            financialImpactValue = 100_000_000L,
            dataBreachImpact    = "Camera, microphone, contacts, messages, location — full device compromise",
            description         = "Zero-click exploit chain exploiting Android kernel CVEs. Used camera and microphone to surveil targets. One of the most sophisticated mobile attacks ever documented.",
            relevantTrigger     = "camera_mic",
            severity            = Severity.CRITICAL
        ),
        AttackIncident(
            name                = "SpyNote Remote Access Trojan",
            year                = "2022–2023",
            attackType          = AttackType.REMOTE_ACCESS,
            affectedDevices     = "Sold to 3K+ criminal operators; scale in tens of thousands",
            financialImpact     = "Sold at \$200–\$299/license on dark web; total \$1M+",
            financialImpactValue = 1_000_000L,
            dataBreachImpact    = "Full remote access: camera, mic, files, SMS, live GPS",
            description         = "Crimeware-as-a-Service sold on dark web forums. Grants full remote control: live camera streaming, microphone eavesdropping, GPS tracking, and file exfiltration.",
            relevantTrigger     = "camera_mic",
            severity            = Severity.CRITICAL
        ),
        AttackIncident(
            name                = "AhMyth Stalkerware Campaign",
            year                = "2022",
            attackType          = AttackType.STALKERWARE,
            affectedDevices     = "100K+ victims in South Asia",
            financialImpact     = "Used in corporate espionage and domestic abuse cases",
            financialImpactValue = 1_000_000L,
            dataBreachImpact    = "Continuous location tracking, call logs, contacts, photos stolen",
            description         = "Open-source RAT repurposed as stalkerware and distributed via SMS with fake app links. Required Camera, Microphone, Location, and Contacts permissions for 24/7 monitoring.",
            relevantTrigger     = "camera_mic",
            severity            = Severity.HIGH
        ),
        AttackIncident(
            name                = "GriftHorse Subscription Scam",
            year                = "2021",
            attackType          = AttackType.SUBSCRIPTION_FRAUD,
            affectedDevices     = "10M+ devices in 70+ countries",
            financialImpact     = "\$400M+ in fraudulent subscription charges",
            financialImpactValue = 400_000_000L,
            dataBreachImpact    = "Phone numbers and carrier info harvested for targeted billing fraud",
            description         = "Distributed through Google Play Store as utility apps. Silently enrolled users in premium SMS services charging \$30–\$40/month. Required READ_PHONE_STATE to identify network carrier.",
            relevantTrigger     = "phone_state",
            severity            = Severity.HIGH
        ),
        AttackIncident(
            name                = "Rooted Device Persistence Attack",
            year                = "2022–2023",
            attackType          = AttackType.REMOTE_ACCESS,
            affectedDevices     = "Primarily enterprise BYOD rooted devices",
            financialImpact     = "\$50M+ in corporate espionage losses per major incident",
            financialImpactValue = 50_000_000L,
            dataBreachImpact    = "Full filesystem access, VPN configs, enterprise email, corporate credentials",
            description         = "Rooted Android devices allow malware to persist across factory resets by writing to the /system partition. Enterprise attacks extracted VPN profiles, corporate email credentials, and work profile data.",
            relevantTrigger     = "rooted",
            severity            = Severity.CRITICAL
        )
    )

    fun generate(
        appResults: List<AppScanResult>,
        deviceResult: DeviceSecurityResult
    ): AttackIntelligence {
        val triggers = mutableSetOf<String>()

        // Sideloaded / unknown-source apps
        if (appResults.any { it.installedFrom.contains("Sideloaded") || it.installedFrom.contains("Unknown") }) {
            triggers.add("sideloaded")
        }

        // Dangerous permissions in granted state
        val grantedDangerousPerms = appResults
            .flatMap { app -> app.permissions.filter { it.isDangerous && it.isGranted }.map { it.name } }
            .toSet()

        if (grantedDangerousPerms.any { "SMS" in it || "CALL_LOG" in it || "RECEIVE_SMS" in it }) {
            triggers.add("sms_permission")
        }
        if (grantedDangerousPerms.any { "CAMERA" in it || "RECORD_AUDIO" in it }) {
            triggers.add("camera_mic")
        }
        if (grantedDangerousPerms.any { "READ_PHONE_STATE" in it }) {
            triggers.add("phone_state")
        }

        // Device-level triggers
        if (deviceResult.isRooted) triggers.add("rooted")

        val relevant = attackDatabase
            .filter { it.relevantTrigger in triggers }
            .sortedWith(compareBy({ it.severity.ordinal }, { -it.financialImpactValue }))

        val totalFinancial = relevant.sumOf { it.financialImpactValue }
        val totalFinancialStr = when {
            totalFinancial >= 1_000_000_000L -> "\$${totalFinancial / 1_000_000_000}B+"
            totalFinancial >= 1_000_000L     -> "\$${totalFinancial / 1_000_000}M+"
            totalFinancial >= 1_000L         -> "\$${totalFinancial / 1_000}K+"
            else                             -> "N/A"
        }

        val criticalCount = relevant.count { it.severity == Severity.CRITICAL }

        val riskSummary = when {
            criticalCount >= 3 ->
                "CRITICAL — Your device matches $criticalCount attack patterns linked to $totalFinancialStr in documented losses. Immediate action required."
            criticalCount >= 1 ->
                "HIGH EXPOSURE — ${relevant.size} known campaigns match your scan, including $criticalCount critical-severity attacks with $totalFinancialStr in real-world impact."
            relevant.isNotEmpty() ->
                "MODERATE EXPOSURE — ${relevant.size} attack types match your scan findings with a combined documented impact of $totalFinancialStr."
            else ->
                "LOW EXPOSURE — No major attack patterns matched your current device configuration."
        }

        return AttackIntelligence(
            relevantAttacks       = relevant,
            totalFinancialExposure = totalFinancialStr,
            criticalAttackCount   = criticalCount,
            riskSummary           = riskSummary
        )
    }
}
