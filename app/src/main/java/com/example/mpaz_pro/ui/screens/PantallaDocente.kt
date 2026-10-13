package com.example.mpaz_pro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mpaz_pro.data.local.InformeEntity
import com.example.mpaz_pro.ui.components.AppBottomBar
import com.example.mpaz_pro.ui.components.AppDrawer
import com.example.mpaz_pro.ui.components.BarraSuperiorMpaz
import com.example.mpaz_pro.viewmodel.MpazViewModel

@Composable
fun PantallaDocente(
    viewModel: MpazViewModel,
    alCerrarSesion: () -> Unit
) {
    val contexto = LocalContext.current
    val objetivos by viewModel.todosLosObjetivos.collectAsState()
    val informes by viewModel.informes.collectAsState()
    var pestañaSeleccionada by remember { mutableStateOf(0) }
    val titulosPestañas = listOf("Objetivos 2° Básico", "Monitoreo", "Informes")

    var informeParaEditar by remember { mutableStateOf<InformeEntity?>(null) }

    AppDrawer(
        pantallaActual = "Panel Principal",
        nombreUsuario = "Prof. Mario Silva",
        rolUsuario = "Docente 2° Básico",
        onPantallaSeleccionada = { pestañaSeleccionada = 0 },
        onCerrarSesion = alCerrarSesion
    ) { abrirMenu ->
        Scaffold(
            topBar = {
                BarraSuperiorMpaz(
                    titulo = "Docente 2° Básico",
                    subtitulo = "Prof. Mario Silva - Marcela Paz",
                    alAbrirMenu = { abrirMenu() },
                    alCerrarSesion = alCerrarSesion
                )
            },
            bottomBar = {
                AppBottomBar(
                    elementoSeleccionado = when (pestañaSeleccionada) {
                        0 -> "modulos"
                        1 -> "inicio"
                        else -> "informes"
                    },
                    alSeleccionarElemento = { id ->
                        when (id) {
                            "modulos" -> pestañaSeleccionada = 0
                            "inicio" -> pestañaSeleccionada = 1
                            "informes" -> pestañaSeleccionada = 2
                            "perfil" -> alCerrarSesion()
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                PrimaryTabRow(selectedTabIndex = pestañaSeleccionada) {
                    titulosPestañas.forEachIndexed { idx, titulo ->
                        Tab(
                            selected = pestañaSeleccionada == idx,
                            onClick = { pestañaSeleccionada = idx },
                            text = { Text(titulo, fontWeight = FontWeight.SemiBold) }
                        )
                    }
                }

                when (pestañaSeleccionada) {
                    0 -> {
                        // 1. Pestaña Objetivos con Switch para Habilitar al Curso
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(objetivos, key = { it.id }) { obj ->
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(text = "${obj.codigo}: ${obj.titulo}", fontWeight = FontWeight.Bold)
                                            Text(text = obj.nombreAsignatura, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                                            Text(
                                                text = if (obj.asignadoAlCurso) "Visible para estudiantes" else "Oculto para estudiantes",
                                                style = MaterialTheme.typography.labelSmall
                                            )
                                        }
                                        Switch(
                                            checked = obj.asignadoAlCurso,
                                            onCheckedChange = { viewModel.alternarAsignacionObjetivo(obj.id, it) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                    1 -> {
                        // 2. Pestaña Monitoreo en Tiempo Real
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(informes, key = { it.id }) { inf ->
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(inf.nombreEstudiante, fontWeight = FontWeight.Bold)
                                            Text("${inf.porcentajeLogro}% Logro", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("${inf.curso} • ${inf.asignatura}", style = MaterialTheme.typography.bodySmall)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        LinearProgressIndicator(
                                            progress = { inf.porcentajeLogro / 100f },
                                            modifier = Modifier.fillMaxWidth().height(8.dp),
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                    2 -> {
                        // 3. Pestaña Informes con Compartir y Editar Observación
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(informes, key = { it.id }) { inf ->
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(text = inf.nombreEstudiante, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                        Text(text = "${inf.curso} • ${inf.asignatura}", style = MaterialTheme.typography.bodySmall)
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(text = "Obs: ${inf.observacionDocente}", style = MaterialTheme.typography.bodyMedium)

                                        Spacer(modifier = Modifier.height(10.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            OutlinedButton(
                                                onClick = {
                                                    viewModel.cargarInformeParaObservacion(inf.id, inf.observacionDocente)
                                                    informeParaEditar = inf
                                                },
                                                modifier = Modifier.padding(end = 8.dp)
                                            ) {
                                                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Observación")
                                            }
                                            Button(
                                                onClick = { viewModel.compartirInformeNativo(contexto, inf) }
                                            ) {
                                                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Compartir")
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

    // Diálogo para Editar Observación Docente
    if (informeParaEditar != null) {
        val formObs by viewModel.formularioObs.collectAsState()
        val erroresObs by viewModel.erroresObs.collectAsState()

        AlertDialog(
            onDismissRequest = { informeParaEditar = null },
            title = { Text("Registrar Observación Pedagógica") },
            text = {
                Column {
                    OutlinedTextField(
                        value = formObs.observacion,
                        onValueChange = { viewModel.actualizarTextoObservacion(it) },
                        label = { Text("Observación del Docente") },
                        isError = erroresObs.observacion != null,
                        supportingText = { erroresObs.observacion?.let { Text(it) } },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = { viewModel.guardarObservacionDocente { informeParaEditar = null } }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { informeParaEditar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
