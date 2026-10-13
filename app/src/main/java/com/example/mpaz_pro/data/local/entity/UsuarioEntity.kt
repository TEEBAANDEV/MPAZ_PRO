package com.example.mpaz_pro.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class RolUsuario {
    UTP,
    DOCENTE,
    ESTUDIANTE
}

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey val id: String,
    val nombreUsuario: String,
    val claveHash: String,
    val nombreCompleto: String,
    val rol: RolUsuario,
    val grado: String = "2° Básico",
    val curso: String = "2° Básico A"
)
