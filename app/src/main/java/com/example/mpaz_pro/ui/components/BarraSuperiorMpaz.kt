package com.example.mpaz_pro.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

/**
 * Barra superior unificada de la aplicación MPAZ PRO.
 * Integra:
 * - Botón de menú hamburguesa para el menú lateral deslizante (MenuDeslizanteCompose).
 * - Menú contextual desplegable NuevoMenu (AppMenu2.0 y MiApp2.0).
 * - Navegación atrás y cierre de sesión.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperiorMpaz(
    titulo: String,
    subtitulo: String? = null,
    alNavegarAtras: (() -> Unit)? = null,
    alAbrirMenu: (() -> Unit)? = null,
    alCerrarSesion: (() -> Unit)? = null,
    accionesPersonalizadas: @Composable (RowScope.() -> Unit)? = null,
    modificador: Modifier = Modifier
) {
    var menuOpcionesAbierto by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            if (subtitulo != null) {
                Column {
                    Text(
                        text = titulo,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = subtitulo,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        navigationIcon = {
            if (alNavegarAtras != null) {
                IconButton(onClick = alNavegarAtras) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver atrás"
                    )
                }
            } else if (alAbrirMenu != null) {
                IconButton(onClick = alAbrirMenu) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Abrir Menú Deslizante"
                    )
                }
            }
        },
        actions = {
            accionesPersonalizadas?.invoke(this)

            if (alCerrarSesion != null) {
                IconButton(onClick = { menuOpcionesAbierto = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menú de Opciones"
                    )
                }
                NuevoMenu(
                    isExpanded = menuOpcionesAbierto,
                    onItemClick = { opcion ->
                        if (opcion == "Cerrar Sesión") {
                            alCerrarSesion()
                        }
                    },
                    onDismiss = {
                        menuOpcionesAbierto = false
                    }
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modificador
    )
}
