package com.ehansih.vulnscanner.data.api

import com.ehansih.vulnscanner.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private val NVD_BASE_URL = BuildConfig.NVD_BASE_URL

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            // NVD requests an API key header for higher rate limits (optional but polite)
            val req = chain.request().newBuilder()
                .addHeader("User-Agent", "VulnScanner-Android/1.0")
                .build()
            chain.proceed(req)
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    val nvdApi: NvdApi = Retrofit.Builder()
        .baseUrl(NVD_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NvdApi::class.java)
}
