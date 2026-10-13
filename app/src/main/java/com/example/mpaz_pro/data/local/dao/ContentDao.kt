package com.example.mpaz_pro.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mpaz_pro.data.local.entity.ContentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContentDao {
    @Query("SELECT * FROM contents WHERE objectiveId = :objectiveId ORDER BY sequenceOrder ASC")
    fun getContentsByObjective(objectiveId: String): Flow<List<ContentEntity>>

    @Query("SELECT * FROM contents WHERE objectiveId = :objectiveId ORDER BY sequenceOrder ASC LIMIT 1")
    suspend fun getFirstContentForObjective(objectiveId: String): ContentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContents(contents: List<ContentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContent(content: ContentEntity)
}
