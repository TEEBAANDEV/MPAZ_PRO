package com.example.mpaz_pro.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mpaz_pro.data.local.entity.StudentProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentProgressDao {
    @Query("SELECT * FROM student_progress WHERE studentId = :studentId")
    fun getProgressByStudent(studentId: String): Flow<List<StudentProgressEntity>>

    @Query("SELECT * FROM student_progress WHERE studentId = :studentId AND objectiveId = :objectiveId LIMIT 1")
    suspend fun getProgressForObjective(studentId: String, objectiveId: String): StudentProgressEntity?

    @Query("SELECT * FROM student_progress WHERE objectiveId = :objectiveId")
    fun getAllProgressForObjective(objectiveId: String): Flow<List<StudentProgressEntity>>

    @Query("SELECT * FROM student_progress")
    fun getAllProgress(): Flow<List<StudentProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: StudentProgressEntity)

    @Update
    suspend fun updateProgress(progress: StudentProgressEntity)
}
