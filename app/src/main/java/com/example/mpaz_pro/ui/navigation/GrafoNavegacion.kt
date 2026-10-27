package com.example.mpaz_pro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mpaz_pro.ui.screens.PantallaDocente
import com.example.mpaz_pro.ui.screens.PantallaEstudiante
import com.example.mpaz_pro.ui.screens.PantallaInicioSesion
import com.example.mpaz_pro.ui.screens.PantallaUtp
import com.example.mpaz_pro.viewmodel.AutenticacionViewModel
import com.example.mpaz_pro.viewmodel.MpazViewModel

@Composable
fun GrafoNavegacion(
    controladorNavegacion: NavHostController,
    autenticacionViewModel: AutenticacionViewModel,
    mpazViewModel: MpazViewModel,
    modificador: Modifier = Modifier
) {
    val estadoAuth by autenticacionViewModel.estadoUi.collectAsState()

    NavHost(
        navController = controladorNavegacion,
        startDestination = DestinoNavegacion.InicioSesion.ruta,
        modifier = modificador
    ) {
        composable(DestinoNavegacion.InicioSesion.ruta) {
            PantallaInicioSesion(
                viewModel = autenticacionViewModel,
                alIniciarSesionConExito = { rol ->
                    when (rol) {
                        "UTP" -> controladorNavegacion.navigate(DestinoNavegacion.Utp.ruta) {
                            popUpTo(DestinoNavegacion.InicioSesion.ruta) { inclusive = true }
                        }
                        "DOCENTE" -> controladorNavegacion.navigate(DestinoNavegacion.Docente.ruta) {
                            popUpTo(DestinoNavegacion.InicioSesion.ruta) { inclusive = true }
                        }
                        else -> controladorNavegacion.navigate(DestinoNavegacion.Estudiante.ruta) {
                            popUpTo(DestinoNavegacion.InicioSesion.ruta) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(DestinoNavegacion.Utp.ruta) {
            PantallaUtp(
                viewModel = mpazViewModel,
                usuarioActual = estadoAuth.usuarioActual,
                onActualizarFoto = { uri -> autenticacionViewModel.actualizarFotoPerfil(uri) },
                alCerrarSesion = {
                    autenticacionViewModel.cerrarSesion()
                    controladorNavegacion.navigate(DestinoNavegacion.InicioSesion.ruta) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(DestinoNavegacion.Docente.ruta) {
            PantallaDocente(
                viewModel = mpazViewModel,
                usuarioActual = estadoAuth.usuarioActual,
                onActualizarFoto = { uri -> autenticacionViewModel.actualizarFotoPerfil(uri) },
                alCerrarSesion = {
                    autenticacionViewModel.cerrarSesion()
                    controladorNavegacion.navigate(DestinoNavegacion.InicioSesion.ruta) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(DestinoNavegacion.Estudiante.ruta) {
            PantallaEstudiante(
                usuarioActual = estadoAuth.usuarioActual,
                viewModel = mpazViewModel,
                onActualizarFoto = { uri -> autenticacionViewModel.actualizarFotoPerfil(uri) },
                alCerrarSesion = {
                    autenticacionViewModel.cerrarSesion()
                    controladorNavegacion.navigate(DestinoNavegacion.InicioSesion.ruta) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
