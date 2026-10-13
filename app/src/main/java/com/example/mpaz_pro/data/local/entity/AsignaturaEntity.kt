package com.example.mpaz_pro.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "asignaturas")
data class AsignaturaEntity(
    @PrimaryKey val id: String,
    val nombre: String,
    val codigo: String,
    val descripcion: String,
    val colorHex: Long,
    val nombreIcono: String
)
