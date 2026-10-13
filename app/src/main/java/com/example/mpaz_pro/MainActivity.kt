package com.example.mpaz_pro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.mpaz_pro.ui.navigation.GrafoNavegacion
import com.example.mpaz_pro.ui.theme.MPAZ_PROTheme
import com.example.mpaz_pro.viewmodel.AutenticacionViewModel
import com.example.mpaz_pro.viewmodel.MpazViewModel

class MainActivity : ComponentActivity() {

    private val autenticacionViewModel: AutenticacionViewModel by viewModels {
        val app = application as AplicacionMpaz
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AutenticacionViewModel(app.repositorio) as T
            }
        }
    }

    private val mpazViewModel: MpazViewModel by viewModels {
        val app = application as AplicacionMpaz
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MpazViewModel(app.repositorio, app.ayudanteTextoAVoz, app.ayudanteVibracion) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MPAZ_PROTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val controladorNavegacion = rememberNavController()
                    GrafoNavegacion(
                        controladorNavegacion = controladorNavegacion,
                        autenticacionViewModel = autenticacionViewModel,
                        mpazViewModel = mpazViewModel
                    )
                }
            }
        }
    }
}
