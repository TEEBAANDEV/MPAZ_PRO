package com.example.mpaz_pro.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subjects")
data class SubjectEntity(
    @PrimaryKey val id: String,
    val name: String,
    val code: String,
    val description: String,
    val colorHex: Long,
    val iconName: String
)
