package com.example.mpaz_pro.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ObjectiveStatus {
    BORRADOR,
    EN_REVISION,
    AUTORIZADO,
    PUBLICADO,
    DESHABILITADO
}

@Entity(tableName = "learning_objectives")
data class LearningObjectiveEntity(
    @PrimaryKey val id: String,
    val subjectId: String,
    val code: String, // e.g., "OA 04"
    val title: String,
    val description: String,
    val unitTitle: String,
    val curriculumAxis: String,
    val referenceText: String,
    val status: ObjectiveStatus,
    val utpResponsible: String = "Jefatura UTP Marcela Paz",
    val creationDate: String = "2026-03-10",
    val isAssignedToCourse: Boolean = false,
    val assignedCourse: String = "2° Básico A",
    val maxAttempts: Int = 2,
    val availabilityStartDate: String = "2026-03-15",
    val availabilityEndDate: String = "2026-04-30"
)
