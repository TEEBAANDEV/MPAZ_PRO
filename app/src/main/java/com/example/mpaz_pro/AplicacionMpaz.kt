package com.example.mpaz_pro

import android.app.Application
import com.example.mpaz_pro.data.local.BaseDatosApp
import com.example.mpaz_pro.data.local.InicializadorBaseDatos
import com.example.mpaz_pro.data.repository.RepositorioApp
import com.example.mpaz_pro.native.AyudanteTextoAVoz
import com.example.mpaz_pro.native.AyudanteVibracion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AplicacionMpaz : Application() {

    lateinit var baseDatos: BaseDatosApp
        private set

    lateinit var repositorio: RepositorioApp
        private set

    lateinit var ayudanteTextoAVoz: AyudanteTextoAVoz
        private set

    lateinit var ayudanteVibracion: AyudanteVibracion
        private set

    override fun onCreate() {
        super.onCreate()
        baseDatos = BaseDatosApp.obtenerBaseDatos(this)
        repositorio = RepositorioApp(baseDatos.appDao())
        ayudanteTextoAVoz = AyudanteTextoAVoz(this)
        ayudanteVibracion = AyudanteVibracion(this)

        CoroutineScope(Dispatchers.IO).launch {
            InicializadorBaseDatos.inicializar(baseDatos.appDao())
        }
    }

    override fun onTerminate() {
        ayudanteTextoAVoz.cerrar()
        super.onTerminate()
    }
}
