package com.example.mpaz_pro.ui.navigation

sealed class DestinoNavegacion(val ruta: String) {
    object InicioSesion : DestinoNavegacion("inicio_sesion")

    // Perfil UTP
    object UtpPrincipal : DestinoNavegacion("utp_principal")
    object UtpEditarObjetivo : DestinoNavegacion("utp_editar_objetivo/{idObjetivo}") {
        fun crearRuta(idObjetivo: String) = "utp_editar_objetivo/$idObjetivo"
    }

    // Perfil Docente
    object DocentePrincipal : DestinoNavegacion("docente_principal")
    object DocenteAsignar : DestinoNavegacion("docente_asignar/{idObjetivo}") {
        fun crearRuta(idObjetivo: String) = "docente_asignar/$idObjetivo"
    }
    object DocenteDetalleInforme : DestinoNavegacion("docente_informe/{idInforme}") {
        fun crearRuta(idInforme: String) = "docente_informe/$idInforme"
    }

    // Perfil Estudiante (2° Básico)
    object EstudiantePrincipal : DestinoNavegacion("estudiante_principal")
    object EstudianteContenido : DestinoNavegacion("estudiante_contenido/{idObjetivo}") {
        fun crearRuta(idObjetivo: String) = "estudiante_contenido/$idObjetivo"
    }
    object EstudianteDesafio : DestinoNavegacion("estudiante_desafio/{idObjetivo}") {
        fun crearRuta(idObjetivo: String) = "estudiante_desafio/$idObjetivo"
    }
    object EstudianteEvaluacion : DestinoNavegacion("estudiante_evaluacion/{idObjetivo}") {
        fun crearRuta(idObjetivo: String) = "estudiante_evaluacion/$idObjetivo"
    }
}
