package com.nokia.vulnscanner.data.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

// NVD REST API v2  –  https://nvd.nist.gov/developers/vulnerabilities
interface NvdApi {

    @GET("cves/2.0")
    suspend fun searchCves(
        @Query("keywordSearch")  keyword: String,
        @Query("resultsPerPage") resultsPerPage: Int = 20,
        @Query("startIndex")     startIndex: Int    = 0
    ): NvdResponse

    @GET("cves/2.0")
    suspend fun searchByPlatform(
        @Query("cpeName")        cpeName: String,
        @Query("resultsPerPage") resultsPerPage: Int = 20
    ): NvdResponse
}

// ── Response models ───────────────────────────────────────────────────────────

data class NvdResponse(
    @SerializedName("totalResults")  val totalResults: Int    = 0,
    @SerializedName("vulnerabilities") val vulnerabilities: List<NvdItem> = emptyList()
)

data class NvdItem(
    @SerializedName("cve") val cve: NvdCve
)

data class NvdCve(
    @SerializedName("id")          val id: String,
    @SerializedName("descriptions") val descriptions: List<NvdDescription> = emptyList(),
    @SerializedName("metrics")     val metrics: NvdMetrics? = null,
    @SerializedName("published")   val published: String = "",
    @SerializedName("references")  val references: List<NvdReference> = emptyList()
)

data class NvdDescription(
    @SerializedName("lang")  val lang: String,
    @SerializedName("value") val value: String
)

data class NvdMetrics(
    @SerializedName("cvssMetricV31") val v31: List<CvssMetric>? = null,
    @SerializedName("cvssMetricV30") val v30: List<CvssMetric>? = null,
    @SerializedName("cvssMetricV2")  val v2:  List<CvssMetric>? = null
)

data class CvssMetric(
    @SerializedName("cvssData") val cvssData: CvssData
)

data class CvssData(
    @SerializedName("baseScore")    val baseScore: Double = 0.0,
    @SerializedName("baseSeverity") val baseSeverity: String = "NONE"
)

data class NvdReference(
    @SerializedName("url") val url: String
)

// ── Helper extension ──────────────────────────────────────────────────────────

fun NvdCve.bestCvssScore(): Double {
    val m = metrics ?: return 0.0
    return (m.v31?.firstOrNull()?.cvssData?.baseScore
        ?: m.v30?.firstOrNull()?.cvssData?.baseScore
        ?: m.v2?.firstOrNull()?.cvssData?.baseScore
        ?: 0.0)
}

fun NvdCve.englishDescription(): String =
    descriptions.firstOrNull { it.lang == "en" }?.value ?: "No description available."
