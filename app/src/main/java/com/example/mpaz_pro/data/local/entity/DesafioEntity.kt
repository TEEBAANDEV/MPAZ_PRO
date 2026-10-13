package com.example.mpaz_pro.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TipoDesafio {
    SELECCION_MULTIPLE,
    COMPLETACION,
    ASOCIACION
}

@Entity(tableName = "desafios")
data class DesafioEntity(
    @PrimaryKey val id: String,
    val idObjetivo: String,
    val titulo: String,
    val tipo: TipoDesafio,
    val enunciado: String,
    val audioEnunciado: String,
    val opciones: List<String>,
    val indiceCorrecto: Int,
    val explicacionRetroalimentacion: String,
    val dificultad: String = "Fácil", // Fácil, Medio, Desafío
    val ordenSecuencia: Int
)
