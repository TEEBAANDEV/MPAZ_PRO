package com.example.mpaz_pro.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class EstadoObjetivo {
    BORRADOR,
    EN_REVISION,
    AUTORIZADO,
    PUBLICADO,
    DESHABILITADO
}

@Entity(tableName = "objetivos_aprendizaje")
data class ObjetivoAprendizajeEntity(
    @PrimaryKey val id: String,
    val idAsignatura: String,
    val codigo: String, // ej: "OA 04"
    val titulo: String,
    val descripcion: String,
    val tituloUnidad: String,
    val ejeCurricular: String,
    val textoReferencia: String,
    val estado: EstadoObjetivo,
    val responsableUtp: String = "Jefatura UTP Marcela Paz",
    val fechaCreacion: String = "2026-03-10",
    val estaAsignadoAlCurso: Boolean = false,
    val cursoAsignado: String = "2° Básico A",
    val intentosMaximos: Int = 2,
    val fechaInicioDisponibilidad: String = "2026-03-15",
    val fechaFinDisponibilidad: String = "2026-04-30"
)
