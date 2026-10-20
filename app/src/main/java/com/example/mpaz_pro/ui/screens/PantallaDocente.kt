package com.example.mpaz_pro.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mpaz_pro.data.local.InformeEntity
import com.example.mpaz_pro.data.local.NotaEntity
import com.example.mpaz_pro.ui.components.AppBottomBar
import com.example.mpaz_pro.ui.components.AppDrawer
import com.example.mpaz_pro.ui.components.BarraSuperiorMpaz
import com.example.mpaz_pro.ui.components.ElementoNavegacionInferior
import com.example.mpaz_pro.viewmodel.MpazViewModel

@Composable
fun PantallaDocente(
    viewModel: MpazViewModel,
    alCerrarSesion: () -> Unit
) {
    val contexto = LocalContext.current
    val objetivos by viewModel.todosLosObjetivos.collectAsState()
    val informes by viewModel.informes.collectAsState()
    val notas by viewModel.todasLasNotas.collectAsState()

    var pestañaSeleccionada by remember { mutableStateOf(0) }
    var asignaturaFiltroNotas by remember { mutableStateOf("todas") }
    var informeParaEditar by remember { mutableStateOf<InformeEntity?>(null) }
    var mostrarDialogoNota by remember { mutableStateOf(false) }
    var notaParaEliminar by remember { mutableStateOf<NotaEntity?>(null) }

    val asignaturas = listOf(
        "todas" to "Todas",
        "lenguaje" to "Lenguaje",
        "matematica" to "Matemática",
        "ciencias" to "Ciencias",
        "historia" to "Historia"
    )

    val pantallaActual = when (pestañaSeleccionada) {
        0 -> "Panel Principal"
        1 -> "Módulos de Aprendizaje"
        2 -> "Libro de Calificaciones"
        3 -> "Seguimiento e Informes"
        else -> "Perfil del Docente"
    }

    val elementosDocente = listOf(
        ElementoNavegacionInferior("inicio", "Inicio", Icons.Default.Home),
        ElementoNavegacionInferior("modulos", "Módulos", Icons.Default.Book),
        ElementoNavegacionInferior("notas", "Notas", Icons.Default.Star),
        ElementoNavegacionInferior("informes", "Informes", Icons.Default.Assessment),
        ElementoNavegacionInferior("perfil", "Perfil", Icons.Default.Person)
    )

    AppDrawer(
        pantallaActual = pantallaActual,
        nombreUsuario = "Prof. Mario Silva",
        rolUsuario = "Docente 2° Básico",
        onPantallaSeleccionada = { seccion ->
            when (seccion) {
                "Panel Principal" -> pestañaSeleccionada = 0
                "Módulos de Aprendizaje" -> pestañaSeleccionada = 1
                "Libro de Calificaciones" -> pestañaSeleccionada = 2
                "Seguimiento e Informes" -> pestañaSeleccionada = 3
            }
        },
        onCerrarSesion = alCerrarSesion
    ) { abrirMenu ->
        Scaffold(
            topBar = {
                BarraSuperiorMpaz(
                    titulo = "Docente 2° Básico",
                    subtitulo = "$pantallaActual • Prof. Mario Silva",
                    alAbrirMenu = { abrirMenu() },
                    alCerrarSesion = alCerrarSesion
                )
            },
            bottomBar = {
                AppBottomBar(
                    elementoSeleccionado = when (pestañaSeleccionada) {
                        0 -> "inicio"
                        1 -> "modulos"
                        2 -> "notas"
                        3 -> "informes"
                        else -> "perfil"
                    },
                    elementos = elementosDocente,
                    alSeleccionarElemento = { id ->
                        when (id) {
                            "inicio" -> pestañaSeleccionada = 0
                            "modulos" -> pestañaSeleccionada = 1
                            "notas" -> pestañaSeleccionada = 2
                            "informes" -> pestañaSeleccionada = 3
                            "perfil" -> pestañaSeleccionada = 4
                        }
                    }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                when (pestañaSeleccionada) {
                    0 -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = "Escuela Escritora Marcela Paz",
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        Text(
                                            text = "2° Básico A • Aula Didáctica Integrada",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(
                                            text = "Total de objetivos curriculares activos: ${objetivos.count { it.asignadoAlCurso }}",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = "Evaluaciones registradas: ${notas.size}",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Resumen por Asignatura",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            items(listOf("lenguaje" to "Lenguaje y Comunicación", "matematica" to "Matemática", "ciencias" to "Ciencias Naturales", "historia" to "Historia y Geografía")) { (id, nombre) ->
                                val estiloAsig = com.example.mpaz_pro.ui.theme.ColoresAsignatura.obtenerEstilo(id)
                                val cantActivos = objetivos.count { it.idAsignatura == id && it.asignadoAlCurso }
                                val notasAsig = notas.filter { it.idAsignatura == id }
                                val promAsig = if (notasAsig.isNotEmpty()) notasAsig.map { it.calificacion }.average() else 0.0

                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, estiloAsig.colorBorde),
                                    colors = CardDefaults.cardColors(containerColor = estiloAsig.colorFondo),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(estiloAsig.emoji)
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(text = nombre, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = estiloAsig.colorPrincipal)
                                            }
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(text = "$cantActivos objetivos habilitados en aula", style = MaterialTheme.typography.bodySmall)
                                        }
                                        SuggestionChip(
                                            onClick = {
                                                asignaturaFiltroNotas = id
                                                pestañaSeleccionada = 2
                                            },
                                            label = {
                                                Text(
                                                    text = if (notasAsig.isNotEmpty()) "Prom: ${String.format(java.util.Locale.US, "%.1f", promAsig)}" else "Ver notas",
                                                    fontWeight = FontWeight.Bold,
                                                    color = estiloAsig.colorPrincipal
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    1 -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                Text(
                                    text = "Gestión de Objetivos de Aprendizaje",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Active o desactive las misiones disponibles para el estudiante",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            items(objetivos, key = { it.id }) { obj ->
                                val estiloObj = com.example.mpaz_pro.ui.theme.ColoresAsignatura.obtenerEstilo(obj.idAsignatura)
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, estiloObj.colorBorde),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(estiloObj.emoji)
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(text = obj.codigo, fontWeight = FontWeight.Bold, color = estiloObj.colorPrincipal)
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(text = obj.titulo, style = MaterialTheme.typography.titleSmall)
                                            }
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(text = obj.nombreAsignatura, style = MaterialTheme.typography.bodySmall, color = estiloObj.colorPrincipal)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(text = obj.descripcion, style = MaterialTheme.typography.bodyMedium)
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Switch(
                                            checked = obj.asignadoAlCurso,
                                            onCheckedChange = { activado ->
                                                viewModel.alternarAsignacionObjetivo(obj.id, activado)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    2 -> {
                        val notasFiltradas = if (asignaturaFiltroNotas == "todas") {
                            notas
                        } else {
                            notas.filter { it.idAsignatura == asignaturaFiltroNotas }
                        }

                        val promedio = if (notasFiltradas.isNotEmpty()) notasFiltradas.map { it.calificacion }.average() else 0.0
                        val aprobados = notasFiltradas.count { it.calificacion >= 4.0 }
                        val porcentajeAprobacion = if (notasFiltradas.isNotEmpty()) (aprobados * 100) / notasFiltradas.size else 0

                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Text(
                                                    text = "Libro de Calificaciones",
                                                    style = MaterialTheme.typography.titleLarge,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = "2° Básico A • Decreto 67 Mineduc",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                                )
                                            }
                                            Button(
                                                onClick = {
                                                    val asigId = if (asignaturaFiltroNotas == "todas") "lenguaje" else asignaturaFiltroNotas
                                                    val asigNombre = asignaturas.firstOrNull { it.first == asigId }?.second ?: "Lenguaje y Comunicación"
                                                    viewModel.prepararNuevaNota(idAsignatura = asigId, nombreAsignatura = asigNombre)
                                                    mostrarDialogoNota = true
                                                }
                                            ) {
                                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Nueva Nota")
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))
                                        HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))
                                        Spacer(modifier = Modifier.height(10.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text("Evaluaciones", style = MaterialTheme.typography.labelSmall)
                                                Text("${notasFiltradas.size}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                            }
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text("Promedio", style = MaterialTheme.typography.labelSmall)
                                                Text(
                                                    text = String.format(java.util.Locale.US, "%.1f", promedio),
                                                    style = MaterialTheme.typography.titleMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (promedio >= 4.0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                                )
                                            }
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text("Aprobación", style = MaterialTheme.typography.labelSmall)
                                                Text("$porcentajeAprobacion%", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }

                            item {
                                ScrollableTabRow(
                                    selectedTabIndex = asignaturas.indexOfFirst { it.first == asignaturaFiltroNotas }.coerceAtLeast(0),
                                    edgePadding = 0.dp
                                ) {
                                    asignaturas.forEach { (id, nombre) ->
                                        Tab(
                                            selected = asignaturaFiltroNotas == id,
                                            onClick = { asignaturaFiltroNotas = id },
                                            text = { Text(nombre, fontWeight = FontWeight.SemiBold) }
                                        )
                                    }
                                }
                            }

                            if (notasFiltradas.isEmpty()) {
                                item {
                                    Card(modifier = Modifier.fillMaxWidth()) {
                                        Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                                            Text("No hay calificaciones registradas en esta asignatura.", style = MaterialTheme.typography.bodyMedium)
                                        }
                                    }
                                }
                            } else {
                                items(notasFiltradas, key = { it.id }) { nota ->
                                    val estiloNota = com.example.mpaz_pro.ui.theme.ColoresAsignatura.obtenerEstilo(nota.idAsignatura)
                                    val colorNota = when {
                                        nota.calificacion >= 6.0 -> Color(0xFF16A34A)
                                        nota.calificacion >= 5.0 -> Color(0xFF2563EB)
                                        nota.calificacion >= 4.0 -> Color(0xFFD97706)
                                        else -> Color(0xFFDC2626)
                                    }

                                    Card(
                                        shape = RoundedCornerShape(12.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, estiloNota.colorBorde),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(estiloNota.emoji)
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(
                                                        text = nota.nombreAsignatura,
                                                        style = MaterialTheme.typography.labelMedium,
                                                        color = estiloNota.colorPrincipal,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }

                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .background(colorNota.copy(alpha = 0.15f))
                                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                                ) {
                                                    Text(
                                                        text = String.format(java.util.Locale.US, "%.1f", nota.calificacion),
                                                        fontWeight = FontWeight.Bold,
                                                        color = colorNota,
                                                        style = MaterialTheme.typography.titleMedium
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(text = nota.tituloEvaluacion, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                            Text(text = "Alumno: ${nota.nombreEstudiante} • Fecha: ${nota.fecha}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "Retroalimentación: ${nota.retroalimentacion}",
                                                style = MaterialTheme.typography.bodyMedium
                                            )

                                            Spacer(modifier = Modifier.height(10.dp))
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.End
                                            ) {
                                                OutlinedButton(
                                                    onClick = {
                                                        viewModel.prepararEdicionNota(nota)
                                                        mostrarDialogoNota = true
                                                    },
                                                    modifier = Modifier.padding(end = 8.dp)
                                                ) {
                                                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("Editar")
                                                }

                                                OutlinedButton(
                                                    onClick = {
                                                        val textoCompartir = """
                                                            ESCUELA MARCELA PAZ - CALIFICACIÓN 2° BÁSICO
                                                            Estudiante: ${nota.nombreEstudiante} (${nota.curso})
                                                            Asignatura: ${nota.nombreAsignatura}
                                                            Evaluación: ${nota.tituloEvaluacion}
                                                            Nota: ${String.format(java.util.Locale.US, "%.1f", nota.calificacion)}
                                                            Retroalimentación: ${nota.retroalimentacion}
                                                            Fecha: ${nota.fecha}
                                                        """.trimIndent()
                                                        val intent = Intent().apply {
                                                            action = Intent.ACTION_SEND
                                                            putExtra(Intent.EXTRA_TEXT, textoCompartir)
                                                            type = "text/plain"
                                                        }
                                                        contexto.startActivity(Intent.createChooser(intent, "Compartir Calificación"))
                                                    },
                                                    modifier = Modifier.padding(end = 8.dp)
                                                ) {
                                                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("Compartir")
                                                }

                                                IconButton(onClick = { notaParaEliminar = nota }) {
                                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    3 -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                Text(
                                    text = "Informes Pedagógicos y Observaciones",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            items(informes, key = { it.id }) { inf ->
                                val estiloInf = com.example.mpaz_pro.ui.theme.ColoresAsignatura.obtenerEstilo(inf.asignatura)
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, estiloInf.colorBorde),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(text = inf.nombreEstudiante, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "${estiloInf.emoji} ${inf.asignatura}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = estiloInf.colorPrincipal,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(" • ${inf.curso}", style = MaterialTheme.typography.bodySmall)
                                        }
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

                    4 -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
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
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(48.dp)
                                )
                            }

                            Text(
                                text = "Prof. Mario Silva",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Docente Titular • 2° Básico A",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Text(text = "Ficha Institucional", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                                    HorizontalDivider()
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("Establecimiento:", color = MaterialTheme.colorScheme.outline)
                                        Text("Escuela Marcela Paz", fontWeight = FontWeight.Medium)
                                    }
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("Comuna:", color = MaterialTheme.colorScheme.outline)
                                        Text("San Bernardo", fontWeight = FontWeight.Medium)
                                    }
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("Usuario de Acceso:", color = MaterialTheme.colorScheme.outline)
                                        Text("docente", fontWeight = FontWeight.Medium)
                                    }
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("Rol Curricular:", color = MaterialTheme.colorScheme.outline)
                                        Text("Docente de Aula", fontWeight = FontWeight.Medium)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            OutlinedButton(
                                onClick = alCerrarSesion,
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Cerrar Sesión")
                            }
                        }
                    }
                }
            }
        }
    }

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

    if (mostrarDialogoNota) {
        val formNota by viewModel.formularioNota.collectAsState()
        val erroresNota by viewModel.erroresNota.collectAsState()

        val listaAsignaturasModal = listOf(
            "lenguaje" to "Lenguaje y Comunicación",
            "matematica" to "Matemática",
            "ciencias" to "Ciencias Naturales",
            "historia" to "Historia y Geografía"
        )

        AlertDialog(
            onDismissRequest = { mostrarDialogoNota = false },
            title = {
                Text(
                    text = if (formNota.idNota.startsWith("nota_") && notas.any { it.id == formNota.idNota }) "Editar Calificación" else "Ingresar Calificación",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Asignatura:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listaAsignaturasModal.forEach { (id, nombre) ->
                            val estiloAsig = com.example.mpaz_pro.ui.theme.ColoresAsignatura.obtenerEstilo(id)
                            FilterChip(
                                selected = formNota.idAsignatura == id,
                                onClick = { viewModel.actualizarAsignaturaNota(id, nombre) },
                                label = { Text(estiloAsig.emoji, style = MaterialTheme.typography.bodySmall) }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = formNota.tituloEvaluacion,
                        onValueChange = { viewModel.actualizarTituloEvaluacion(it) },
                        label = { Text("Nombre de la Evaluación") },
                        isError = erroresNota.titulo != null,
                        supportingText = { erroresNota.titulo?.let { Text(it) } },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = formNota.calificacionTexto,
                        onValueChange = { viewModel.actualizarCalificacionTexto(it) },
                        label = { Text("Nota (1.0 a 7.0)") },
                        isError = erroresNota.calificacion != null,
                        supportingText = { erroresNota.calificacion?.let { Text(it) } },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = formNota.retroalimentacion,
                        onValueChange = { viewModel.actualizarRetroalimentacionNota(it) },
                        label = { Text("Retroalimentación Formativa") },
                        isError = erroresNota.retroalimentacion != null,
                        supportingText = { erroresNota.retroalimentacion?.let { Text(it) } },
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = { viewModel.guardarNota { mostrarDialogoNota = false } }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoNota = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (notaParaEliminar != null) {
        val nota = notaParaEliminar!!
        AlertDialog(
            onDismissRequest = { notaParaEliminar = null },
            title = { Text("Eliminar Calificación") },
            text = { Text("¿Desea eliminar la evaluación '${nota.tituloEvaluacion}' con nota ${nota.calificacion}?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.eliminarNota(nota.id)
                        notaParaEliminar = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { notaParaEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
