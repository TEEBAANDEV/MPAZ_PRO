package com.example.mpaz_pro.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "informes_pedagogicos")
data class InformePedagogicoEntity(
    @PrimaryKey val id: String,
    val idEstudiante: String,
    val nombreEstudiante: String,
    val nombreCurso: String,
    val nombreAsignatura: String,
    val codigoObjetivo: String,
    val tituloObjetivo: String,
    val puntajeGlobal: Int,
    val puntajeMaximo: Int,
    val porcentaje: Int,
    val fortalezas: String,
    val areasRefuerzo: String,
    val observacionDocente: String,
    val fechaEmision: String,
    val estaValidadoPorDocente: Boolean = false
)
