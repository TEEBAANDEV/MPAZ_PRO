package com.example.mpaz_pro.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mpaz_pro.data.local.dao.*
import com.example.mpaz_pro.data.local.entity.*

@Database(
    entities = [
        UserEntity::class,
        SubjectEntity::class,
        LearningObjectiveEntity::class,
        ContentEntity::class,
        ChallengeEntity::class,
        ExamQuestionEntity::class,
        StudentProgressEntity::class,
        PedagogicalReportEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun subjectDao(): SubjectDao
    abstract fun learningObjectiveDao(): LearningObjectiveDao
    abstract fun contentDao(): ContentDao
    abstract fun challengeDao(): ChallengeDao
    abstract fun examQuestionDao(): ExamQuestionDao
    abstract fun studentProgressDao(): StudentProgressDao
    abstract fun pedagogicalReportDao(): PedagogicalReportDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mpaz_pro_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
