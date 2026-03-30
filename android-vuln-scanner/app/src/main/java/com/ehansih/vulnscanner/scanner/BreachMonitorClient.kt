package com.ehansih.vulnscanner.scanner

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.ehansih.vulnscanner.data.models.AppLogger
import com.ehansih.vulnscanner.data.models.BreachRecord
import com.ehansih.vulnscanner.data.models.VirusTotalResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.security.MessageDigest
import java.util.concurrent.TimeUnit

class BreachMonitorClient(private val context: Context) {

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    // ── HIBP Breach Check ─────────────────────────────────────────────────────

    /**
     * Checks the HaveIBeenPwned API v3 for breaches associated with the given email.
     *
     * Requires a valid HIBP API key. Returns an empty list if the key is blank,
     * if the email is not found in any breach (HTTP 404), or on any error.
     *
     * @param email  The email address to check
     * @param apiKey HIBP API v3 key (obtain at https://haveibeenpwned.com/API/Key)
     * @return List of [BreachRecord] — empty if no breaches or key not configured
     */
    suspend fun checkBreach(email: String, apiKey: String): List<BreachRecord> {
        if (apiKey.isBlank()) {
            AppLogger.w("BreachMonitorClient", "HIBP API key not configured — skipping breach check")
            return emptyList()
        }
        if (email.isBlank() || !email.contains("@")) {
            AppLogger.w("BreachMonitorClient", "Invalid email address — skipping breach check")
            return emptyList()
        }

        return withContext(Dispatchers.IO) {
            try {
                val encodedEmail = java.net.URLEncoder.encode(email, "UTF-8")
                val url = "https://haveibeenpwned.com/api/v3/breachedaccount/$encodedEmail?truncateResponse=false"

                val request = Request.Builder()
                    .url(url)
                    .header("hibp-api-key", apiKey)
                    .header("User-Agent", "VulnScanner-Android/1.0")
                    .get()
                    .build()

                AppLogger.i("BreachMonitorClient", "Checking HIBP for email: ${email.take(3)}***@***")

                val response = httpClient.newCall(request).execute()

                when (response.code) {
                    200 -> {
                        val body = response.body?.string() ?: return@withContext emptyList()
                        val breaches = parseHibpResponse(body)
                        AppLogger.i("BreachMonitorClient", "HIBP: found ${breaches.size} breach(es) for email")
                        breaches
                    }
                    404 -> {
                        AppLogger.i("BreachMonitorClient", "HIBP: email not found in any breach (404)")
                        emptyList()
                    }
                    401 -> {
                        AppLogger.e("BreachMonitorClient", "HIBP: Unauthorised (401) — invalid API key")
                        emptyList()
                    }
                    403 -> {
                        AppLogger.e("BreachMonitorClient", "HIBP: Forbidden (403) — check HIBP API key permissions")
                        emptyList()
                    }
                    429 -> {
                        val retryAfter = response.header("Retry-After") ?: "unknown"
                        AppLogger.w("BreachMonitorClient", "HIBP: Rate limited (429) — retry after $retryAfter seconds")
                        emptyList()
                    }
                    503 -> {
                        AppLogger.w("BreachMonitorClient", "HIBP: Service unavailable (503)")
                        emptyList()
                    }
                    else -> {
                        AppLogger.w("BreachMonitorClient", "HIBP: Unexpected HTTP ${response.code}")
                        emptyList()
                    }
                }
            } catch (e: java.net.UnknownHostException) {
                AppLogger.e("BreachMonitorClient", "HIBP: DNS resolution failed — no internet or service blocked")
                emptyList()
            } catch (e: java.net.SocketTimeoutException) {
                AppLogger.e("BreachMonitorClient", "HIBP: Request timed out")
                emptyList()
            } catch (e: Exception) {
                AppLogger.e("BreachMonitorClient", "HIBP: Unexpected error: ${e.message}", e)
                emptyList()
            }
        }
    }

    private fun parseHibpResponse(json: String): List<BreachRecord> {
        return try {
            val array = JSONArray(json)
            (0 until array.length()).map { i ->
                val obj         = array.getJSONObject(i)
                val dataClasses = obj.optJSONArray("DataClasses")
                val classes     = if (dataClasses != null) {
                    (0 until dataClasses.length()).map { dataClasses.getString(it) }
                } else emptyList()

                BreachRecord(
                    name        = obj.optString("Name", "Unknown"),
                    domain      = obj.optString("Domain", ""),
                    breachDate  = obj.optString("BreachDate", ""),
                    dataClasses = classes,
                    description = stripHtml(obj.optString("Description", "")),
                    isVerified  = obj.optBoolean("IsVerified", false)
                )
            }
        } catch (e: Exception) {
            AppLogger.e("BreachMonitorClient", "Error parsing HIBP response: ${e.message}", e)
            emptyList()
        }
    }

    // ── VirusTotal APK Hash Check ─────────────────────────────────────────────

    /**
     * Queries the VirusTotal API v3 for a file hash report.
     *
     * Returns a [VirusTotalResult] with engine detection counts if the hash is known to VT.
     * Returns null if the API key is blank, the hash is unknown (HTTP 404), or on any error.
     *
     * @param sha256 The SHA-256 hash of the APK file
     * @param apiKey VirusTotal API key (obtain at https://www.virustotal.com/gui/my-apikey)
     * @return [VirusTotalResult] or null
     */
    suspend fun checkApkHash(sha256: String, apiKey: String): VirusTotalResult? {
        if (apiKey.isBlank()) {
            AppLogger.w("BreachMonitorClient", "VirusTotal API key not configured — skipping hash check")
            return null
        }
        if (sha256.isBlank() || sha256.length != 64) {
            AppLogger.w("BreachMonitorClient", "Invalid SHA-256 hash — skipping VT check")
            return null
        }

        return withContext(Dispatchers.IO) {
            try {
                val url = "https://www.virustotal.com/api/v3/files/$sha256"

                val request = Request.Builder()
                    .url(url)
                    .header("x-apikey", apiKey)
                    .header("User-Agent", "VulnScanner-Android/1.0")
                    .get()
                    .build()

                AppLogger.i("BreachMonitorClient", "Checking VirusTotal for hash: ${sha256.take(16)}...")

                val response = httpClient.newCall(request).execute()

                when (response.code) {
                    200 -> {
                        val body = response.body?.string() ?: return@withContext null
                        parseVirusTotalResponse(body, sha256)
                    }
                    404 -> {
                        AppLogger.i("BreachMonitorClient", "VT: hash not found in VirusTotal database (404)")
                        null
                    }
                    401 -> {
                        AppLogger.e("BreachMonitorClient", "VT: Unauthorised (401) — invalid API key")
                        null
                    }
                    403 -> {
                        AppLogger.e("BreachMonitorClient", "VT: Forbidden (403) — API key lacks permission")
                        null
                    }
                    429 -> {
                        AppLogger.w("BreachMonitorClient", "VT: Rate limited (429) — free API limited to 4 req/min")
                        null
                    }
                    else -> {
                        AppLogger.w("BreachMonitorClient", "VT: Unexpected HTTP ${response.code}")
                        null
                    }
                }
            } catch (e: java.net.UnknownHostException) {
                AppLogger.e("BreachMonitorClient", "VT: DNS resolution failed")
                null
            } catch (e: java.net.SocketTimeoutException) {
                AppLogger.e("BreachMonitorClient", "VT: Request timed out")
                null
            } catch (e: Exception) {
                AppLogger.e("BreachMonitorClient", "VT: Unexpected error: ${e.message}", e)
                null
            }
        }
    }

    private fun parseVirusTotalResponse(json: String, sha256: String): VirusTotalResult? {
        return try {
            val root          = JSONObject(json)
            val data          = root.optJSONObject("data") ?: return null
            val attributes    = data.optJSONObject("attributes") ?: return null
            val stats         = attributes.optJSONObject("last_analysis_stats")
            val results       = attributes.optJSONObject("last_analysis_results")

            val malicious     = stats?.optInt("malicious", 0) ?: 0
            val suspicious    = stats?.optInt("suspicious", 0) ?: 0
            val totalEngines  = (stats?.optInt("malicious", 0) ?: 0) +
                                (stats?.optInt("suspicious", 0) ?: 0) +
                                (stats?.optInt("undetected", 0) ?: 0) +
                                (stats?.optInt("harmless", 0) ?: 0) +
                                (stats?.optInt("timeout", 0) ?: 0)

            val detectionNames = mutableListOf<String>()
            if (results != null) {
                val keys = results.keys()
                while (keys.hasNext()) {
                    val engine = keys.next()
                    val engineResult = results.optJSONObject(engine)
                    val category     = engineResult?.optString("category", "") ?: ""
                    val resultName   = engineResult?.optString("result", "") ?: ""
                    if (category == "malicious" && resultName.isNotBlank()) {
                        detectionNames.add("$engine: $resultName")
                    }
                }
            }

            // Extract a meaningful package name / app name from metadata if available
            val names       = attributes.optJSONObject("names")
            val firstKey    = names?.keys()?.asSequence()?.firstOrNull()
            val appNameHint = firstKey ?: sha256.take(16)

            AppLogger.i(
                "BreachMonitorClient",
                "VT result: malicious=$malicious suspicious=$suspicious total=$totalEngines"
            )

            VirusTotalResult(
                sha256         = sha256,
                packageName    = appNameHint,
                appName        = appNameHint,
                maliciousCount = malicious,
                suspiciousCount = suspicious,
                totalEngines   = totalEngines,
                detectionNames = detectionNames
            )
        } catch (e: Exception) {
            AppLogger.e("BreachMonitorClient", "Error parsing VT response: ${e.message}", e)
            null
        }
    }

    // ── APK SHA-256 computation ───────────────────────────────────────────────

    /**
     * Computes the SHA-256 hash of the base APK for a given installed package.
     *
     * @param context     Application context
     * @param packageName Package name of the installed app
     * @return Lowercase hex SHA-256 string, or null if the APK cannot be read
     */
    suspend fun computeApkSha256(context: Context, packageName: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val pm  = context.packageManager
                val info = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    pm.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0L))
                } else {
                    @Suppress("DEPRECATION")
                    pm.getPackageInfo(packageName, 0)
                }

                val apkPath = info.applicationInfo?.sourceDir ?: run {
                    AppLogger.w("BreachMonitorClient", "No sourceDir for package $packageName")
                    return@withContext null
                }

                val apkFile = File(apkPath)
                if (!apkFile.exists() || !apkFile.canRead()) {
                    AppLogger.w("BreachMonitorClient", "APK file not accessible: $apkPath")
                    return@withContext null
                }

                AppLogger.d("BreachMonitorClient", "Computing SHA-256 for $packageName (${apkFile.length()} bytes)")

                val digest = MessageDigest.getInstance("SHA-256")
                apkFile.inputStream().use { input ->
                    val buffer = ByteArray(65536)
                    var bytesRead: Int
                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        digest.update(buffer, 0, bytesRead)
                    }
                }

                val hash = digest.digest().joinToString("") { "%02x".format(it) }
                AppLogger.d("BreachMonitorClient", "SHA-256 for $packageName: ${hash.take(16)}...")
                hash
            } catch (e: PackageManager.NameNotFoundException) {
                AppLogger.e("BreachMonitorClient", "Package not found: $packageName")
                null
            } catch (e: SecurityException) {
                AppLogger.e("BreachMonitorClient", "Security exception reading APK for $packageName: ${e.message}", e)
                null
            } catch (e: Exception) {
                AppLogger.e("BreachMonitorClient", "Error computing SHA-256 for $packageName: ${e.message}", e)
                null
            }
        }
    }

    // ── Utility ───────────────────────────────────────────────────────────────

    /** Strip simple HTML tags from HIBP description text */
    private fun stripHtml(html: String): String {
        return html
            .replace(Regex("<[^>]+>"), "")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&amp;", "&")
            .replace("&quot;", "\"")
            .replace("&#39;", "'")
            .trim()
    }
}
