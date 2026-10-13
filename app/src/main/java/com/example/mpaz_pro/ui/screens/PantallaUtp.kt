package com.example.mpaz_pro.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mpaz_pro.ui.components.AppBottomBar
import com.example.mpaz_pro.ui.components.AppDrawer
import com.example.mpaz_pro.ui.components.BarraSuperiorMpaz
import com.example.mpaz_pro.viewmodel.MpazViewModel

@Composable
fun PantallaUtp(
    viewModel: MpazViewModel,
    alCerrarSesion: () -> Unit
) {
    val objetivos by viewModel.todosLosObjetivos.collectAsState()
    var pestañaAsignatura by remember { mutableStateOf("todas") }
    var mostrarDialogoNuevo by remember { mutableStateOf(false) }

    val asignaturas = listOf(
        "todas" to "Todas",
        "lenguaje" to "Lenguaje",
        "matematica" to "Matemática",
        "ciencias" to "Ciencias",
        "historia" to "Historia"
    )

    val objetivosFiltrados = remember(objetivos, pestañaAsignatura) {
        if (pestañaAsignatura == "todas") objetivos
        else objetivos.filter { it.idAsignatura == pestañaAsignatura }
    }

    AppDrawer(
        pantallaActual = "Panel Principal",
        nombreUsuario = "Jefatura UTP",
        rolUsuario = "Administración Curricular",
        onPantallaSeleccionada = { pestañaAsignatura = "todas" },
        onCerrarSesion = alCerrarSesion
    ) { abrirMenu ->
        Scaffold(
            topBar = {
                BarraSuperiorMpaz(
                    titulo = "Jefatura UTP - Marcela Paz",
                    subtitulo = "Gestión Curricular 2° Básico",
                    alAbrirMenu = { abrirMenu() },
                    alCerrarSesion = alCerrarSesion
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        viewModel.prepararNuevoObjetivo()
                        mostrarDialogoNuevo = true
                    },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Nuevo Objetivo")
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Selector de Asignaturas
                ScrollableTabRow(
                    selectedTabIndex = asignaturas.indexOfFirst { it.first == pestañaAsignatura }.coerceAtLeast(0),
                    edgePadding = 16.dp
                ) {
                    asignaturas.forEach { (id, nombre) ->
                        Tab(
                            selected = pestañaAsignatura == id,
                            onClick = { pestañaAsignatura = id },
                            text = { Text(nombre, fontWeight = FontWeight.SemiBold) }
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = "Objetivos Curriculares (${objetivosFiltrados.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    items(objetivosFiltrados, key = { it.id }) { obj ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    SuggestionChip(
                                        onClick = {},
                                        label = { Text(obj.codigo, fontWeight = FontWeight.Bold) }
                                    )
                                    Badge(
                                        containerColor = if (obj.estado == "AUTORIZADO") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                    ) {
                                        Text(obj.estado, modifier = Modifier.padding(4.dp))
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = obj.titulo, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                Text(text = obj.nombreAsignatura, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = obj.descripcion, style = MaterialTheme.typography.bodyMedium)

                                if (obj.estado != "AUTORIZADO") {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = { viewModel.autorizarObjetivo(obj.id) },
                                        modifier = Modifier.align(Alignment.End)
                                    ) {
                                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Autorizar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo con Formulario de Creación de Objetivo (Patrón MiAppFormulario)
    if (mostrarDialogoNuevo) {
        val form by viewModel.formularioObjetivo.collectAsState()
        val errores by viewModel.erroresObjetivo.collectAsState()

        AlertDialog(
            onDismissRequest = { mostrarDialogoNuevo = false },
            title = { Text("Nuevo Objetivo de Aprendizaje") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = form.codigo,
                        onValueChange = { viewModel.actualizarCodigoObjetivo(it) },
                        label = { Text("Código (ej: OA 05)") },
                        isError = errores.codigo != null,
                        supportingText = { errores.codigo?.let { Text(it) } },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = form.titulo,
                        onValueChange = { viewModel.actualizarTituloObjetivo(it) },
                        label = { Text("Título") },
                        isError = errores.titulo != null,
                        supportingText = { errores.titulo?.let { Text(it) } },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = form.descripcion,
                        onValueChange = { viewModel.actualizarDescripcionObjetivo(it) },
                        label = { Text("Contenido Didáctico") },
                        isError = errores.descripcion != null,
                        supportingText = { errores.descripcion?.let { Text(it) } },
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = form.pregunta,
                        onValueChange = { viewModel.actualizarPreguntaObjetivo(it) },
                        label = { Text("Pregunta de Evaluación") },
                        isError = errores.pregunta != null,
                        supportingText = { errores.pregunta?.let { Text(it) } },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = { viewModel.guardarObjetivo { mostrarDialogoNuevo = false } }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoNuevo = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
