package com.example.mpaz_pro.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mpaz_pro.data.local.entity.LearningObjectiveEntity
import com.example.mpaz_pro.data.local.entity.ObjectiveStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface LearningObjectiveDao {
    @Query("SELECT * FROM learning_objectives")
    fun getAllObjectives(): Flow<List<LearningObjectiveEntity>>

    @Query("SELECT * FROM learning_objectives WHERE subjectId = :subjectId")
    fun getObjectivesBySubject(subjectId: String): Flow<List<LearningObjectiveEntity>>

    @Query("SELECT * FROM learning_objectives WHERE id = :id LIMIT 1")
    suspend fun getObjectiveById(id: String): LearningObjectiveEntity?

    @Query("SELECT * FROM learning_objectives WHERE status = 'AUTORIZADO' OR status = 'PUBLICADO'")
    fun getAuthorizedObjectives(): Flow<List<LearningObjectiveEntity>>

    @Query("SELECT * FROM learning_objectives WHERE isAssignedToCourse = 1 AND (status = 'AUTORIZADO' OR status = 'PUBLICADO')")
    fun getAssignedObjectivesForStudents(): Flow<List<LearningObjectiveEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObjectives(objectives: List<LearningObjectiveEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObjective(objective: LearningObjectiveEntity)

    @Update
    suspend fun updateObjective(objective: LearningObjectiveEntity)

    @Query("UPDATE learning_objectives SET status = :status WHERE id = :id")
    suspend fun updateObjectiveStatus(id: String, status: ObjectiveStatus)

    @Query("UPDATE learning_objectives SET isAssignedToCourse = :isAssigned, maxAttempts = :maxAttempts, availabilityStartDate = :startDate, availabilityEndDate = :endDate WHERE id = :id")
    suspend fun updateAssignment(id: String, isAssigned: Boolean, maxAttempts: Int, startDate: String, endDate: String)
}
