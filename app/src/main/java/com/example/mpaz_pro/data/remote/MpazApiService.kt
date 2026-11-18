package com.example.mpaz_pro.data.remote

import retrofit2.Response
import retrofit2.http.*

data class LoginRequest(val username: String, val password: String)

data class AuthResponse(
    val token: String,
    val username: String,
    val nombreCompleto: String,
    val rol: String
)

data class ObjetivoRemotoDto(
    val id: Long? = null,
    val codigo: String,
    val titulo: String,
    val descripcion: String,
    val asignatura: String,
    val estado: String
)

data class AsignacionRemotaDto(
    val id: Long? = null,
    val objetivoId: Long,
    val curso: String,
    val limiteIntentos: Int,
    val fechaInicio: String,
    val fechaFin: String
)

data class ObservacionRemotaDto(
    val id: Long? = null,
    val estudiante: String,
    val docente: String,
    val comentario: String,
    val fecha: String
)

data class NotaRemotaDto(
    val id: Long? = null,
    val estudiante: String,
    val asignatura: String,
    val tituloEvaluacion: String,
    val calificacion: Double,
    val retroalimentacion: String,
    val fecha: String
)

interface MpazApiService {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @GET("api/objetivos")
    suspend fun obtenerObjetivos(): Response<List<ObjetivoRemotoDto>>

    @POST("api/objetivos")
    suspend fun crearObjetivo(@Body objetivo: ObjetivoRemotoDto): Response<ObjetivoRemotoDto>

    @PUT("api/objetivos/{id}")
    suspend fun actualizarObjetivo(
        @Path("id") id: Long,
        @Body objetivo: ObjetivoRemotoDto
    ): Response<ObjetivoRemotoDto>

    @DELETE("api/objetivos/{id}")
    suspend fun eliminarObjetivo(@Path("id") id: Long): Response<Unit>

    @GET("api/asignaciones")
    suspend fun obtenerAsignaciones(): Response<List<AsignacionRemotaDto>>

    @POST("api/asignaciones")
    suspend fun crearAsignacion(@Body asignacion: AsignacionRemotaDto): Response<AsignacionRemotaDto>

    @GET("api/observaciones")
    suspend fun obtenerObservaciones(): Response<List<ObservacionRemotaDto>>

    @POST("api/observaciones")
    suspend fun crearObservacion(@Body observacion: ObservacionRemotaDto): Response<ObservacionRemotaDto>

    @GET("api/notas")
    suspend fun obtenerNotas(): Response<List<NotaRemotaDto>>

    @POST("api/notas")
    suspend fun crearNota(@Body nota: NotaRemotaDto): Response<NotaRemotaDto>
}
