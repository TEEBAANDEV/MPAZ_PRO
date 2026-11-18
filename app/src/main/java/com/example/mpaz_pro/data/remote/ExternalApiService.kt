package com.example.mpaz_pro.data.remote

import retrofit2.Response
import retrofit2.http.GET

data class FraseExternaDto(
    val id: Int,
    val quote: String,
    val author: String
)

interface ExternalApiService {
    @GET("quotes/random")
    suspend fun obtenerFraseMotivacional(): Response<FraseExternaDto>
}
