package com.example.mpaz_pro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mpaz_pro.data.local.entity.RolUsuario
import com.example.mpaz_pro.ui.screens.PantallaInicioSesion
import com.example.mpaz_pro.ui.screens.docente.PantallaAsignarDocente
import com.example.mpaz_pro.ui.screens.docente.PantallaDetalleInformeDocente
import com.example.mpaz_pro.ui.screens.docente.PantallaPrincipalDocente
import com.example.mpaz_pro.ui.screens.estudiante.PantallaContenidoEstudiante
import com.example.mpaz_pro.ui.screens.estudiante.PantallaDesafioEstudiante
import com.example.mpaz_pro.ui.screens.estudiante.PantallaEvaluacionEstudiante
import com.example.mpaz_pro.ui.screens.estudiante.PantallaPrincipalEstudiante
import com.example.mpaz_pro.ui.screens.utp.PantallaEditarObjetivoUtp
import com.example.mpaz_pro.ui.screens.utp.PantallaPrincipalUtp
import com.example.mpaz_pro.viewmodel.AutenticacionViewModel
import com.example.mpaz_pro.viewmodel.DocenteViewModel
import com.example.mpaz_pro.viewmodel.EstudianteViewModel
import com.example.mpaz_pro.viewmodel.UtpViewModel

@Composable
fun GrafoNavegacion(
    controladorNavegacion: NavHostController,
    autenticacionViewModel: AutenticacionViewModel,
    utpViewModel: UtpViewModel,
    docenteViewModel: DocenteViewModel,
    estudianteViewModel: EstudianteViewModel,
    modificador: Modifier = Modifier
) {
    val estadoAutenticacion by autenticacionViewModel.estadoUi.collectAsState()
    val objetivosAsignados by estudianteViewModel.objetivosAsignados.collectAsState()

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
                        RolUsuario.UTP -> controladorNavegacion.navigate(DestinoNavegacion.UtpPrincipal.ruta) {
                            popUpTo(DestinoNavegacion.InicioSesion.ruta) { inclusive = true }
                        }
                        RolUsuario.DOCENTE -> controladorNavegacion.navigate(DestinoNavegacion.DocentePrincipal.ruta) {
                            popUpTo(DestinoNavegacion.InicioSesion.ruta) { inclusive = true }
                        }
                        RolUsuario.ESTUDIANTE -> controladorNavegacion.navigate(DestinoNavegacion.EstudiantePrincipal.ruta) {
                            popUpTo(DestinoNavegacion.InicioSesion.ruta) { inclusive = true }
                        }
                    }
                }
            )
        }

        // --- Perfil UTP ---
        composable(DestinoNavegacion.UtpPrincipal.ruta) {
            PantallaPrincipalUtp(
                viewModel = utpViewModel,
                alNavegarAEditar = { idObjetivo ->
                    controladorNavegacion.navigate(DestinoNavegacion.UtpEditarObjetivo.crearRuta(idObjetivo))
                },
                alCrearNuevoObjetivo = {
                    controladorNavegacion.navigate(DestinoNavegacion.UtpEditarObjetivo.crearRuta("nuevo"))
                },
                alCerrarSesion = {
                    autenticacionViewModel.cerrarSesion()
                    controladorNavegacion.navigate(DestinoNavegacion.InicioSesion.ruta) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = DestinoNavegacion.UtpEditarObjetivo.ruta,
            arguments = listOf(navArgument("idObjetivo") { type = NavType.StringType })
        ) {
            PantallaEditarObjetivoUtp(
                viewModel = utpViewModel,
                alNavegarAtras = { controladorNavegacion.popBackStack() }
            )
        }

        // --- Perfil Docente ---
        composable(DestinoNavegacion.DocentePrincipal.ruta) {
            PantallaPrincipalDocente(
                viewModel = docenteViewModel,
                alNavegarAAsignar = { idObjetivo ->
                    controladorNavegacion.navigate(DestinoNavegacion.DocenteAsignar.crearRuta(idObjetivo))
                },
                alNavegarAInforme = { idInforme ->
                    controladorNavegacion.navigate(DestinoNavegacion.DocenteDetalleInforme.crearRuta(idInforme))
                },
                alCerrarSesion = {
                    autenticacionViewModel.cerrarSesion()
                    controladorNavegacion.navigate(DestinoNavegacion.InicioSesion.ruta) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = DestinoNavegacion.DocenteAsignar.ruta,
            arguments = listOf(navArgument("idObjetivo") { type = NavType.StringType })
        ) {
            PantallaAsignarDocente(
                viewModel = docenteViewModel,
                alNavegarAtras = { controladorNavegacion.popBackStack() }
            )
        }

        composable(
            route = DestinoNavegacion.DocenteDetalleInforme.ruta,
            arguments = listOf(navArgument("idInforme") { type = NavType.StringType })
        ) { backStackEntry ->
            val idInforme = backStackEntry.arguments?.getString("idInforme") ?: ""
            PantallaDetalleInformeDocente(
                idInforme = idInforme,
                viewModel = docenteViewModel,
                alNavegarAtras = { controladorNavegacion.popBackStack() }
            )
        }

        // --- Perfil Estudiante (2° Básico) ---
        composable(DestinoNavegacion.EstudiantePrincipal.ruta) {
            PantallaPrincipalEstudiante(
                usuarioActual = estadoAutenticacion.usuarioActual,
                viewModel = estudianteViewModel,
                alNavegarAContenido = { idObjetivo ->
                    controladorNavegacion.navigate(DestinoNavegacion.EstudianteContenido.crearRuta(idObjetivo))
                },
                alCerrarSesion = {
                    autenticacionViewModel.cerrarSesion()
                    controladorNavegacion.navigate(DestinoNavegacion.InicioSesion.ruta) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = DestinoNavegacion.EstudianteContenido.ruta,
            arguments = listOf(navArgument("idObjetivo") { type = NavType.StringType })
        ) { backStackEntry ->
            val idObjetivo = backStackEntry.arguments?.getString("idObjetivo") ?: ""
            PantallaContenidoEstudiante(
                idObjetivo = idObjetivo,
                viewModel = estudianteViewModel,
                alNavegarADesafios = { id ->
                    controladorNavegacion.navigate(DestinoNavegacion.EstudianteDesafio.crearRuta(id))
                },
                alNavegarAtras = { controladorNavegacion.popBackStack() }
            )
        }

        composable(
            route = DestinoNavegacion.EstudianteDesafio.ruta,
            arguments = listOf(navArgument("idObjetivo") { type = NavType.StringType })
        ) { backStackEntry ->
            val idObjetivo = backStackEntry.arguments?.getString("idObjetivo") ?: ""
            PantallaDesafioEstudiante(
                idObjetivo = idObjetivo,
                viewModel = estudianteViewModel,
                alNavegarAEvaluacion = { id ->
                    controladorNavegacion.navigate(DestinoNavegacion.EstudianteEvaluacion.crearRuta(id))
                },
                alNavegarAtras = { controladorNavegacion.popBackStack() }
            )
        }

        composable(
            route = DestinoNavegacion.EstudianteEvaluacion.ruta,
            arguments = listOf(navArgument("idObjetivo") { type = NavType.StringType })
        ) { backStackEntry ->
            val idObjetivo = backStackEntry.arguments?.getString("idObjetivo") ?: ""
            PantallaEvaluacionEstudiante(
                idObjetivo = idObjetivo,
                usuarioActual = estadoAutenticacion.usuarioActual,
                objetivosAsignados = objetivosAsignados,
                viewModel = estudianteViewModel,
                alNavegarAInicio = {
                    controladorNavegacion.navigate(DestinoNavegacion.EstudiantePrincipal.ruta) {
                        popUpTo(DestinoNavegacion.EstudiantePrincipal.ruta) { inclusive = true }
                    }
                },
                alNavegarAtras = { controladorNavegacion.popBackStack() }
            )
        }
    }
}

