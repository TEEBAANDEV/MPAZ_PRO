package com.example.mpaz_pro.ui.navigation

sealed class DestinoNavegacion(val ruta: String) {
    object InicioSesion : DestinoNavegacion("inicio_sesion")
    object Utp : DestinoNavegacion("utp")
    object Docente : DestinoNavegacion("docente")
    object Estudiante : DestinoNavegacion("estudiante")
}
