package com.example.mpaz_pro.ui.screens.estudiante

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mpaz_pro.data.local.entity.ObjetivoAprendizajeEntity
import com.example.mpaz_pro.data.local.entity.UsuarioEntity
import com.example.mpaz_pro.ui.components.AppBottomBar
import com.example.mpaz_pro.ui.components.AppDrawer
import com.example.mpaz_pro.ui.components.BarraSuperiorMpaz
import com.example.mpaz_pro.viewmodel.EstudianteViewModel

@Composable
fun PantallaPrincipalEstudiante(
    usuarioActual: UsuarioEntity?,
    viewModel: EstudianteViewModel,
    alNavegarAContenido: (String) -> Unit,
    alCerrarSesion: () -> Unit
) {
    val objetivosAsignados by viewModel.objetivosAsignados.collectAsState()
    val nombreEstudiante = usuarioActual?.nombreCompleto ?: "Estudiante"
    var elementoInferiorSeleccionado by remember { mutableStateOf("inicio") }

    AppDrawer(
        pantallaActual = "Panel Principal",
        nombreUsuario = nombreEstudiante,
        rolUsuario = "Estudiante 2° Básico",
        onPantallaSeleccionada = { opcion ->
            when (opcion) {
                "Panel Principal" -> elementoInferiorSeleccionado = "inicio"
                "Módulos de Aprendizaje" -> elementoInferiorSeleccionado = "modulos"
                "Seguimiento e Informes" -> elementoInferiorSeleccionado = "informes"
            }
        },
        onCerrarSesion = alCerrarSesion
    ) { abrirMenu ->
        Scaffold(
            topBar = {
                BarraSuperiorMpaz(
                    titulo = "¡Hola, $nombreEstudiante! 👋",
                    subtitulo = "2° Básico • Escuela Marcela Paz",
                    alAbrirMenu = { abrirMenu() },
                    alCerrarSesion = alCerrarSesion
                )
            },
            bottomBar = {
                AppBottomBar(
                    elementoSeleccionado = elementoInferiorSeleccionado,
                    alSeleccionarElemento = { id ->
                        elementoInferiorSeleccionado = id
                        when (id) {
                            "perfil" -> alCerrarSesion()
                        }
                    }
                )
            }
        ) { rellenoInterno ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(rellenoInterno)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(54.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Color(0xFFFFD54F),
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = "¡Aprende Jugando!",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = "Elige una misión para leer con audio y resolver desafíos interactivos.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Tus Misiones Asignadas (${objetivosAsignados.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (objetivosAsignados.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                                Text(
                                    text = "No tienes misiones asignadas por tu profesor en este momento.",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                } else {
                    items(objetivosAsignados, key = { it.id }) { objetivo ->
                        TarjetaMisionEstudiante(
                            objetivo = objetivo,
                            alIniciarMision = { alNavegarAContenido(objetivo.id) }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun TarjetaMisionEstudiante(
    objetivo: ObjetivoAprendizajeEntity,
    alIniciarMision: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = objetivo.codigo,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = objetivo.ejeCurricular,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = objetivo.titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = objetivo.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "📚 Unidad: ${objetivo.tituloUnidad}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = alIniciarMision,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "¡Empezar Misión!",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
