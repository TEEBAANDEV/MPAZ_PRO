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
    val contexto = androidx.compose.ui.platform.LocalContext.current
    val objetivos by viewModel.todosLosObjetivos.collectAsState()
    var seccionActual by remember { mutableStateOf("Panel Principal") }
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
        pantallaActual = seccionActual,
        nombreUsuario = "Jefatura UTP",
        rolUsuario = "Administración Curricular",
        onPantallaSeleccionada = { seccion ->
            seccionActual = seccion
            if (seccion == "Panel Principal") {
                pestañaAsignatura = "todas"
            }
        },
        onCerrarSesion = alCerrarSesion
    ) { abrirMenu ->
        Scaffold(
            topBar = {
                BarraSuperiorMpaz(
                    titulo = "Jefatura UTP - Marcela Paz",
                    subtitulo = "$seccionActual • 2° Básico",
                    alAbrirMenu = { abrirMenu() },
                    alCerrarSesion = alCerrarSesion
                )
            },
            floatingActionButton = {
                if (seccionActual == "Panel Principal") {
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
            }
        ) { padding ->
            when (seccionActual) {
                "Panel Principal" -> {
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
                                val estiloObj = com.example.mpaz_pro.ui.theme.ColoresAsignatura.obtenerEstilo(obj.idAsignatura)
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, estiloObj.colorBorde),
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
                                                label = { Text(obj.codigo, fontWeight = FontWeight.Bold, color = estiloObj.colorPrincipal) }
                                            )
                                            Badge(
                                                containerColor = if (obj.estado == "AUTORIZADO") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                            ) {
                                                Text(obj.estado, modifier = Modifier.padding(4.dp))
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(text = obj.titulo, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                        Text(
                                            text = "${estiloObj.emoji} ${obj.nombreAsignatura}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = estiloObj.colorPrincipal,
                                            fontWeight = FontWeight.SemiBold
                                        )
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

                "Módulos de Aprendizaje" -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Módulos de Aprendizaje - 2° Básico",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Supervisión de unidades pedagógicas para el nivel",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        val modulos = listOf(
                            Triple("lenguaje", "Lenguaje y Comunicación", "Comprensión lectora, fluidez y vocabulario básico"),
                            Triple("matematica", "Matemática", "Cálculo mental, operaciones básicas y resolución de problemas"),
                            Triple("ciencias", "Ciencias Naturales", "Observación del entorno natural, seres vivos y sus hábitats"),
                            Triple("historia", "Historia, Geografía y Cs. Sociales", "Identidad comunitaria, deberes y ubicación geográfica")
                        )

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(modulos) { (id, nombre, desc) ->
                                val objsModulo = objetivos.filter { it.idAsignatura == id }
                                val autorizados = objsModulo.count { it.estado == "AUTORIZADO" }
                                val estiloMod = com.example.mpaz_pro.ui.theme.ColoresAsignatura.obtenerEstilo(id)

                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.5.dp, estiloMod.colorBorde),
                                    colors = CardDefaults.cardColors(containerColor = estiloMod.colorFondo),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = "${estiloMod.emoji} $nombre",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = estiloMod.colorPrincipal
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = desc, style = MaterialTheme.typography.bodySmall)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "${objsModulo.size} objetivos ($autorizados autorizados)",
                                                style = MaterialTheme.typography.labelMedium,
                                                color = estiloMod.colorPrincipal,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            OutlinedButton(
                                                onClick = {
                                                    pestañaAsignatura = id
                                                    seccionActual = "Panel Principal"
                                                }
                                            ) {
                                                Text("Gestionar")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                "Seguimiento e Informes" -> {
                    val total = objetivos.size
                    val autorizados = objetivos.count { it.estado == "AUTORIZADO" }
                    val cobertura = if (total > 0) (autorizados * 100) / total else 0

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Seguimiento e Informes Curriculares",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Auditoría de cobertura para Escuela Marcela Paz",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    text = "Estado General de Cobertura 2° Básico",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Objetivos Registrados: $total")
                                    Text("Autorizados UTP: $autorizados")
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                LinearProgressIndicator(
                                    progress = { if (total > 0) autorizados.toFloat() / total.toFloat() else 0f },
                                    modifier = Modifier.fillMaxWidth().height(10.dp),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "$cobertura% de objetivos autorizados para el aula",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
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
                                    text = "Desglose por Asignatura",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                asignaturas.filter { it.first != "todas" }.forEach { (id, nombre) ->
                                    val cant = objetivos.count { it.idAsignatura == id }
                                    val aut = objetivos.count { it.idAsignatura == id && it.estado == "AUTORIZADO" }
                                    val estilo = com.example.mpaz_pro.ui.theme.ColoresAsignatura.obtenerEstilo(id)
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(estilo.emoji)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(text = nombre, style = MaterialTheme.typography.bodyMedium, color = estilo.colorPrincipal, fontWeight = FontWeight.SemiBold)
                                        }
                                        Text(text = "$aut / $cant autorizados", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }

                        Button(
                            onClick = {
                                val resumen = """
                                    ==========================================
                                    ESCUELA MARCELA PAZ - REPORTE UTP 2° BÁSICO
                                    ==========================================
                                    Total Objetivos Registrados: $total
                                    Objetivos Autorizados: $autorizados
                                    Cobertura Curricular: $cobertura%
                                    Fecha: ${java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(java.util.Date())}
                                    ==========================================
                                """.trimIndent()

                                val intent = android.content.Intent().apply {
                                    action = android.content.Intent.ACTION_SEND
                                    putExtra(android.content.Intent.EXTRA_TEXT, resumen)
                                    putExtra(android.content.Intent.EXTRA_SUBJECT, "Reporte Curricular UTP 2° Básico")
                                    type = "text/plain"
                                }
                                val chooser = android.content.Intent.createChooser(intent, "Compartir Reporte UTP")
                                chooser.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                                contexto.startActivity(chooser)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Compartir Resumen Curricular")
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
