package com.example.mpaz_pro.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

/**
 * Barra superior basada en los ejemplos de clase:
 * - MenuDeslizanteCompose (AppTopBar con apertura de menú deslizante)
 * - AppMenu2.0 / MiApp2.0 (TopAppBar con NuevoMenu desplegable)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    titulo: String,
    subtitulo: String? = null,
    onMenuClick: (() -> Unit)? = null,
    alVolverAtras: (() -> Unit)? = null,
    onItemMenuClick: ((String) -> Unit)? = null,
    accionesPersonalizadas: @Composable (RowScope.() -> Unit)? = null,
    modificador: Modifier = Modifier
) {
    var menuDesplegado by remember { mutableStateOf(false) }

    TopAppBar(
        modifier = modificador,
        title = {
            androidx.compose.foundation.layout.Column {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (subtitulo != null) {
                    Text(
                        text = subtitulo,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
        },
        navigationIcon = {
            if (alVolverAtras != null) {
                IconButton(onClick = alVolverAtras) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver Atrás"
                    )
                }
            } else if (onMenuClick != null) {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Abrir Menú Deslizante"
                    )
                }
            }
        },
        actions = {
            accionesPersonalizadas?.invoke(this)

            if (onItemMenuClick != null) {
                IconButton(onClick = { menuDesplegado = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Opciones del Menú"
                    )
                }
                NuevoMenu(
                    isExpanded = menuDesplegado,
                    onItemClick = { opcion ->
                        onItemMenuClick(opcion)
                    },
                    onDismiss = {
                        menuDesplegado = false
                    }
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}
