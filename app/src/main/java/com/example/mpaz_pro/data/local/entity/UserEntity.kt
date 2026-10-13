package com.example.mpaz_pro.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole {
    UTP,
    DOCENTE,
    ESTUDIANTE
}

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val username: String,
    val passwordHash: String,
    val fullName: String,
    val role: UserRole,
    val grade: String = "2° Básico",
    val course: String = "2° Básico A"
)
