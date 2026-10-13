package com.example.mpaz_pro.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mpaz_pro.data.local.UsuarioEntity
import com.example.mpaz_pro.ui.components.AppDrawer
import com.example.mpaz_pro.ui.components.BarraSuperiorMpaz
import com.example.mpaz_pro.viewmodel.MpazViewModel

import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import com.example.mpaz_pro.ui.components.AppBottomBar
import com.example.mpaz_pro.ui.components.ElementoNavegacionInferior

@Composable
fun PantallaEstudiante(
    usuarioActual: UsuarioEntity?,
    viewModel: MpazViewModel,
    alCerrarSesion: () -> Unit
) {
    val objetivos by viewModel.objetivosAsignados.collectAsState()
    val estaHablando by viewModel.estaHablando.collectAsState()
    val nombreAlumno = usuarioActual?.nombreCompleto ?: "Lucas"

    var seccionActual by remember { mutableStateOf("Misiones") }
    var indiceObjetivoActual by remember { mutableStateOf(0) }
    var respuestaSeleccionada by remember { mutableStateOf<Int?>(null) }
    var mensajeResultado by remember { mutableStateOf<String?>(null) }
    var estrellasGanadas by remember { mutableStateOf(5) }

    val objetivoActivo = objetivos.getOrNull(indiceObjetivoActual)

    val elementosEstudiante = listOf(
        ElementoNavegacionInferior("misiones", "Misiones", Icons.Default.Book),
        ElementoNavegacionInferior("logros", "Mis Logros", Icons.Default.Star),
        ElementoNavegacionInferior("perfil", "Mi Perfil", Icons.Default.Person)
    )

    AppDrawer(
        pantallaActual = when (seccionActual) {
            "Misiones" -> "Módulos de Aprendizaje"
            "Mis Logros" -> "Seguimiento e Informes"
            else -> "Panel Principal"
        },
        nombreUsuario = nombreAlumno,
        rolUsuario = "Estudiante 2° Básico",
        onPantallaSeleccionada = { seccion ->
            when (seccion) {
                "Panel Principal", "Módulos de Aprendizaje" -> seccionActual = "Misiones"
                "Seguimiento e Informes" -> {
                    viewModel.detenerAudio()
                    seccionActual = "Mis Logros"
                }
            }
        },
        onCerrarSesion = {
            viewModel.detenerAudio()
            alCerrarSesion()
        }
    ) { abrirMenu ->
        Scaffold(
            topBar = {
                BarraSuperiorMpaz(
                    titulo = "¡Hola, $nombreAlumno! ⭐ $estrellasGanadas",
                    subtitulo = "$seccionActual • 2° Básico",
                    alAbrirMenu = { abrirMenu() },
                    alCerrarSesion = {
                        viewModel.detenerAudio()
                        alCerrarSesion()
                    }
                )
            },
            bottomBar = {
                AppBottomBar(
                    elementoSeleccionado = when (seccionActual) {
                        "Misiones" -> "misiones"
                        "Mis Logros" -> "logros"
                        else -> "perfil"
                    },
                    elementos = elementosEstudiante,
                    alSeleccionarElemento = { id ->
                        when (id) {
                            "misiones" -> seccionActual = "Misiones"
                            "logros" -> {
                                viewModel.detenerAudio()
                                seccionActual = "Mis Logros"
                            }
                            "perfil" -> {
                                viewModel.detenerAudio()
                                seccionActual = "Mi Perfil"
                            }
                        }
                    }
                )
            }
        ) { padding ->
            when (seccionActual) {
                "Mis Logros" -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(56.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "⭐ $estrellasGanadas Estrellas Ganadas",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "¡Excelente dedicación en tus lecturas, $nombreAlumno!",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Medallas de 2° Básico",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("📖 Lectura con Voz (TTS):")
                                    Text("¡Medalla de Oro! 🥇", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("🎯 Desafíos Respondidos:")
                                    Text("${objetivos.size} misiones", fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("📳 Respuesta Háptica:")
                                    Text("Activa", fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Button(
                            onClick = { seccionActual = "Misiones" },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Volver a Mis Misiones de Aprendizaje")
                        }
                    }
                }

                "Mi Perfil" -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(48.dp)
                            )
                        }

                        Text(
                            text = nombreAlumno,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Estudiante • 2° Básico A",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(text = "Ficha del Alumno", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                                HorizontalDivider()
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Escuela:", color = MaterialTheme.colorScheme.outline)
                                    Text("Escuela Marcela Paz", fontWeight = FontWeight.Medium)
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Profesor Jefe:", color = MaterialTheme.colorScheme.outline)
                                    Text("Prof. Mario Silva", fontWeight = FontWeight.Medium)
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Estrellas Acumuladas:", color = MaterialTheme.colorScheme.outline)
                                    Text("⭐ $estrellasGanadas", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        OutlinedButton(
                            onClick = {
                                viewModel.detenerAudio()
                                alCerrarSesion()
                            },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Cerrar Sesión")
                        }
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                Text(
                    text = "Elige tu Misión de Aprendizaje:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                if (objetivos.isNotEmpty()) {
                    ScrollableTabRow(
                        selectedTabIndex = indiceObjetivoActual.coerceIn(0, objetivos.size - 1),
                        edgePadding = 0.dp
                    ) {
                        objetivos.forEachIndexed { idx, obj ->
                            val estiloTab = com.example.mpaz_pro.ui.theme.ColoresAsignatura.obtenerEstilo(obj.idAsignatura)
                            Tab(
                                selected = indiceObjetivoActual == idx,
                                onClick = {
                                    viewModel.detenerAudio()
                                    indiceObjetivoActual = idx
                                    respuestaSeleccionada = null
                                    mensajeResultado = null
                                },
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(estiloTab.emoji)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "${obj.codigo}: ${estiloTab.nombreCorto}",
                                            fontWeight = if (indiceObjetivoActual == idx) FontWeight.Bold else FontWeight.Normal,
                                            color = if (indiceObjetivoActual == idx) estiloTab.colorPrincipal else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            )
                        }
                    }
                }

                if (objetivoActivo == null) {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                            Text("No hay misiones asignadas en este momento.")
                        }
                    }
                } else {
                    val estiloActivo = com.example.mpaz_pro.ui.theme.ColoresAsignatura.obtenerEstilo(objetivoActivo.idAsignatura)

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(2.dp, estiloActivo.colorBorde),
                        colors = CardDefaults.cardColors(containerColor = estiloActivo.colorFondo),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${estiloActivo.emoji} ${objetivoActivo.nombreAsignatura} • 2° Básico",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = estiloActivo.colorPrincipal
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = objetivoActivo.titulo,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        if (estaHablando) {
                                            viewModel.detenerAudio()
                                        } else {
                                            viewModel.reproducirAudio("${objetivoActivo.titulo}. ${objetivoActivo.descripcion}. Pregunta: ${objetivoActivo.pregunta}")
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (estaHablando) Icons.Default.Stop else Icons.Default.VolumeUp,
                                        contentDescription = "Escuchar en voz alta",
                                        tint = estiloActivo.colorPrincipal,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = objetivoActivo.descripcion,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, estiloActivo.colorBorde),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "🎯 Desafío Interactivo:",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = objetivoActivo.pregunta,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            val listaOpciones = objetivoActivo.opciones.split(",").map { it.trim() }

                            listaOpciones.forEachIndexed { opIdx, opcion ->
                                val esSeleccionada = respuestaSeleccionada == opIdx
                                OutlinedButton(
                                    onClick = {
                                        respuestaSeleccionada = opIdx
                                        viewModel.responderPregunta(opIdx, objetivoActivo) { acierto ->
                                            if (acierto) {
                                                estrellasGanadas += 1
                                                mensajeResultado = "¡CORRECTO! 🎉 Sumaste +1 estrella ⭐ (Tienes $estrellasGanadas)"
                                            } else {
                                                mensajeResultado = "¡Casi! 💡 Vuelve a escuchar la lectura con el botón de voz 🔊 e inténtalo de nuevo."
                                            }
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = if (esSeleccionada) {
                                        ButtonDefaults.outlinedButtonColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer
                                        )
                                    } else ButtonDefaults.outlinedButtonColors()
                                ) {
                                    Text(
                                        text = opcion,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            AnimatedVisibility(visible = mensajeResultado != null) {
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (mensajeResultado?.contains("CORRECTO") == true)
                                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                        else
                                            MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(if (mensajeResultado?.contains("CORRECTO") == true) "🌟" else "💡")
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = mensajeResultado ?: "",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
}
}

