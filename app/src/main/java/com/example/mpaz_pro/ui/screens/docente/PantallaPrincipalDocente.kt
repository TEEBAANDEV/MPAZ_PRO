package com.example.mpaz_pro.ui.screens.docente

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mpaz_pro.data.local.entity.InformePedagogicoEntity
import com.example.mpaz_pro.data.local.entity.ObjetivoAprendizajeEntity
import com.example.mpaz_pro.data.local.entity.ProgresoEstudianteEntity
import com.example.mpaz_pro.ui.components.BarraSuperiorMpaz
import com.example.mpaz_pro.ui.components.InsigniaEstado
import com.example.mpaz_pro.ui.viewmodel.DocenteViewModel

@Composable
fun PantallaPrincipalDocente(
    viewModel: DocenteViewModel,
    alNavegarAAsignar: (String) -> Unit,
    alNavegarAInforme: (String) -> Unit,
    alCerrarSesion: () -> Unit
) {
    var pestañaSeleccionada by remember { mutableStateOf(0) }
    val titulosPestañas = listOf("Objetivos UTP", "Monitoreo", "Informes")

    val objetivosAutorizados by viewModel.objetivosAutorizados.collectAsState()
    val todosLosProgresos by viewModel.todosLosProgresos.collectAsState()
    val informes by viewModel.informes.collectAsState()

    Scaffold(
        topBar = {
            BarraSuperiorMpaz(
                titulo = "Docente 2° Básico",
                subtitulo = "Prof. Mario Silva - Marcela Paz",
                alCerrarSesion = alCerrarSesion
            )
        }
    ) { rellenoInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(rellenoInterno)
        ) {
            PrimaryTabRow(selectedTabIndex = pestañaSeleccionada) {
                titulosPestañas.forEachIndexed { indice, titulo ->
                    Tab(
                        selected = pestañaSeleccionada == indice,
                        onClick = { pestañaSeleccionada = indice },
                        text = { Text(titulo, fontWeight = FontWeight.SemiBold) }
                    )
                }
            }

            when (pestañaSeleccionada) {
                0 -> PestañaObjetivosDocente(
                    objetivos = objetivosAutorizados,
                    alHacerClicConfigurar = { objetivo ->
                        viewModel.cargarObjetivoParaAsignacion(objetivo)
                        alNavegarAAsignar(objetivo.id)
                    }
                )
                1 -> PestañaMonitoreoDocente(listaProgreso = todosLosProgresos)
                2 -> PestañaInformesDocente(
                    informes = informes,
                    alHacerClicInforme = { informe ->
                        viewModel.cargarInformeParaObservacion(informe)
                        alNavegarAInforme(informe.id)
                    }
                )
            }
        }
    }
}

@Composable
fun PestañaObjetivosDocente(
    objetivos: List<ObjetivoAprendizajeEntity>,
    alHacerClicConfigurar: (ObjetivoAprendizajeEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Unidades Autorizadas por UTP (${objetivos.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Seleccione un objetivo de aprendizaje para asignar al curso y configurar intentos de evaluación.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        items(objetivos, key = { it.id }) { objetivo ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = objetivo.codigo,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        InsigniaEstado(estado = objetivo.estado)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = objetivo.titulo,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Unidad: ${objetivo.tituloUnidad}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (objetivo.estaAsignadoAlCurso) "Asignado a: ${objetivo.cursoAsignado} (${objetivo.intentosMaximos} intentos)" else "No asignado a curso",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (objetivo.estaAsignadoAlCurso) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline
                        )

                        Button(
                            onClick = { alHacerClicConfigurar(objetivo) },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Configurar", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PestañaMonitoreoDocente(listaProgreso: List<ProgresoEstudianteEntity>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Avance y Rendimiento por Objetivo",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        if (listaProgreso.isEmpty()) {
            item {
                Text(
                    text = "Aún no se registran actividades resueltas por estudiantes.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(listaProgreso, key = { it.id }) { progreso ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Estudiante: ${if (progreso.idEstudiante.contains("1")) "Lucas Martínez" else "Sofía Morales"}",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = "${progreso.porcentaje}% Logro",
                            fontWeight = FontWeight.ExtraBold,
                            color = if (progreso.porcentaje >= 60) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    LinearProgressIndicator(
                        progress = { progreso.porcentaje / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        trackColor = MaterialTheme.colorScheme.outlineVariant,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Intentos usados: ${progreso.intentosUsados}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "Puntaje: ${progreso.ultimoPuntaje}/${progreso.puntajeMaximo} pts",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Text(
                        text = "Última fecha: ${progreso.fechaUltimoIntento}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun PestañaInformesDocente(
    informes: List<InformePedagogicoEntity>,
    alHacerClicInforme: (InformePedagogicoEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Informes Pedagógicos Consolidados",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Acceso exclusivo para docentes. Seleccione un informe para registrar observaciones u exportarlo.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        items(informes, key = { it.id }) { informe ->
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { alHacerClicInforme(informe) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = informe.nombreEstudiante,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        AssistChip(
                            onClick = { alHacerClicInforme(informe) },
                            label = { Text(if (informe.estaValidadoPorDocente) "Validado" else "Pendiente obs.") }
                        )
                    }

                    Text(
                        text = "${informe.nombreAsignatura} • ${informe.codigoObjetivo}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Rendimiento: ${informe.puntajeGlobal}/${informe.puntajeMaximo} pts (${informe.porcentaje}%)",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Observación docente: ${if (informe.observacionDocente.isNotBlank()) informe.observacionDocente else "(Sin registrar aún)"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (informe.observacionDocente.isNotBlank()) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}
