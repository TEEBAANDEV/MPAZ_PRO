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

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

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
    var dialogoEscuelaAbierto by remember { mutableStateOf(false) }
    var dialogoReglamentoAbierto by remember { mutableStateOf(false) }
    var dialogoAltoContrasteAbierto by remember { mutableStateOf(false) }

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
                        when (opcion) {
                            "Escuela Marcela Paz" -> dialogoEscuelaAbierto = true
                            "Reglamento de Evaluación" -> dialogoReglamentoAbierto = true
                            "Modo Alto Contraste", "Tema y Alto Contraste" -> dialogoAltoContrasteAbierto = true
                            "Cerrar Sesión" -> alCerrarSesion()
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

    if (dialogoEscuelaAbierto) {
        AlertDialog(
            onDismissRequest = { dialogoEscuelaAbierto = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = "Escuela Escritora Marcela Paz",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Establecimiento educacional de San Bernardo comprometido con el desarrollo pedagógico y la inclusión en Segundo Básico.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    Text("• Comuna: San Bernardo, Región Metropolitana", style = MaterialTheme.typography.bodySmall)
                    Text("• Nivel escolar: 2.º Básico A y B", style = MaterialTheme.typography.bodySmall)
                    Text("• Proyecto: MPAZ PRO 2.º (Apoyo Didáctico)", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                }
            },
            confirmButton = {
                Button(onClick = { dialogoEscuelaAbierto = false }) {
                    Text("Entendido")
                }
            }
        )
    }

    if (dialogoReglamentoAbierto) {
        AlertDialog(
            onDismissRequest = { dialogoReglamentoAbierto = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Assignment,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = "Reglamento de Evaluación",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Criterios de Evaluación Formativa 2° Básico (Decreto 67 / 2018):",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text("⭐ 80% - 100%: Logro Destacado (Autonomía completa)", style = MaterialTheme.typography.bodySmall)
                    Text("👍 60% - 79%: Logro Adecuado (Comprensión consolidada)", style = MaterialTheme.typography.bodySmall)
                    Text("💡 Menos de 60%: En Desarrollo (Reforzamiento guiado con audio)", style = MaterialTheme.typography.bodySmall)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    Text(
                        text = "Evaluación orientada al aprendizaje, apoyada con lectura en voz alta (Text-To-Speech) y respuesta háptica.",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            },
            confirmButton = {
                Button(onClick = { dialogoReglamentoAbierto = false }) {
                    Text("Cerrar")
                }
            }
        )
    }

    if (dialogoAltoContrasteAbierto) {
        val darkThemeSystem = androidx.compose.foundation.isSystemInDarkTheme()
        val esOscuro = com.example.mpaz_pro.ui.theme.AdministradorTema.forzarOscuro ?: darkThemeSystem
        val esAltoContraste = com.example.mpaz_pro.ui.theme.AdministradorTema.altoContraste

        AlertDialog(
            onDismissRequest = { dialogoAltoContrasteAbierto = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = "Tema y Accesibilidad Visual",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Configuración para el aula: adapta la iluminación y el contraste para estudiantes y docentes.",
                        style = MaterialTheme.typography.bodySmall
                    )

                    HorizontalDivider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (esOscuro) "🌙 Modo Oscuro" else "☀️ Modo Claro",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = if (esOscuro) "Fondo oscuro para descanso visual" else "Fondo claro estándar de alta luminosidad",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                        Switch(
                            checked = esOscuro,
                            onCheckedChange = { com.example.mpaz_pro.ui.theme.AdministradorTema.forzarOscuro = it }
                        )
                    }

                    HorizontalDivider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "🔲 Alto Contraste (WCAG)",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = if (esAltoContraste) "Bordes sólidos y máximo contraste activos" else "Paleta didáctica estándar",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                        Switch(
                            checked = esAltoContraste,
                            onCheckedChange = { com.example.mpaz_pro.ui.theme.AdministradorTema.altoContraste = it }
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = { dialogoAltoContrasteAbierto = false }) {
                    Text("Aplicar")
                }
            }
        )
    }
}
