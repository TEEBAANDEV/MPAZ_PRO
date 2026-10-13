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
        UsuarioEntity::class,
        AsignaturaEntity::class,
        ObjetivoAprendizajeEntity::class,
        ContenidoDidacticoEntity::class,
        DesafioEntity::class,
        PreguntaEvaluacionEntity::class,
        ProgresoEstudianteEntity::class,
        InformePedagogicoEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Convertidores::class)
abstract class BaseDatosApp : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun asignaturaDao(): AsignaturaDao
    abstract fun objetivoAprendizajeDao(): ObjetivoAprendizajeDao
    abstract fun contenidoDidacticoDao(): ContenidoDidacticoDao
    abstract fun desafioDao(): DesafioDao
    abstract fun preguntaEvaluacionDao(): PreguntaEvaluacionDao
    abstract fun progresoEstudianteDao(): ProgresoEstudianteDao
    abstract fun informePedagogicoDao(): InformePedagogicoDao

    companion object {
        @Volatile
        private var INSTANCIA: BaseDatosApp? = null

        fun obtenerBaseDatos(contexto: Context): BaseDatosApp {
            return INSTANCIA ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    contexto.applicationContext,
                    BaseDatosApp::class.java,
                    "mpaz_pro_basedatos"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCIA = instancia
                instancia
            }
        }
    }
}
