package com.example.mpaz_pro.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        UsuarioEntity::class,
        ObjetivoEntity::class,
        InformeEntity::class,
        NotaEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class BaseDatosApp : RoomDatabase() {

    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCIA: BaseDatosApp? = null

        fun obtenerBaseDatos(contexto: Context): BaseDatosApp {
            return INSTANCIA ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    contexto.applicationContext,
                    BaseDatosApp::class.java,
                    "mpaz_pro_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCIA = instancia
                instancia
            }
        }
    }
}
