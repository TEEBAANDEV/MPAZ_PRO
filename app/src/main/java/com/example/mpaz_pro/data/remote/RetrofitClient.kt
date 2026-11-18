package com.example.mpaz_pro.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL_MICROSERVICIO = "http://100.55.19.106:8080/"
    private const val BASE_URL_API_EXTERNA = "https://dummyjson.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpMicroservicio = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val builder = original.newBuilder()
            TokenManager.jwtToken?.let { token ->
                builder.addHeader("Authorization", "Bearer $token")
            }
            chain.proceed(builder.build())
        }
        .addInterceptor(loggingInterceptor)
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .build()

    private val okHttpExterno = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(8, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .build()

    val mpazApi: MpazApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_MICROSERVICIO)
            .client(okHttpMicroservicio)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MpazApiService::class.java)
    }

    val externalApi: ExternalApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_API_EXTERNA)
            .client(okHttpExterno)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExternalApiService::class.java)
    }
}
