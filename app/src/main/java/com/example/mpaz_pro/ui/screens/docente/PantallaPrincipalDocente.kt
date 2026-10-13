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
import com.example.mpaz_pro.ui.components.AppBottomBar
import com.example.mpaz_pro.ui.components.AppDrawer
import com.example.mpaz_pro.ui.components.BarraSuperiorMpaz
import com.example.mpaz_pro.ui.components.InsigniaEstado
import com.example.mpaz_pro.viewmodel.DocenteViewModel

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

    AppDrawer(
        pantallaActual = "Panel Principal",
        nombreUsuario = "Prof. Mario Silva",
        rolUsuario = "Docente 2° Básico",
        onPantallaSeleccionada = { opcion ->
            when (opcion) {
                "Panel Principal" -> pestañaSeleccionada = 0
                "Módulos de Aprendizaje" -> pestañaSeleccionada = 1
                "Seguimiento e Informes" -> pestañaSeleccionada = 2
            }
        },
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
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = objetivo.codigo,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        if (objetivo.estaAsignadoAlCurso) {
                            Badge(containerColor = MaterialTheme.colorScheme.primary) {
                                Text("Asignado al Curso", modifier = Modifier.padding(4.dp))
                            }
                        } else {
                            Badge(containerColor = MaterialTheme.colorScheme.outline) {
                                Text("Sin Asignar", modifier = Modifier.padding(4.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = objetivo.titulo,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = objetivo.descripcion,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Intentos máx.: ${objetivo.intentosMaximos} | Período: ${objetivo.fechaInicioDisponibilidad} al ${objetivo.fechaFinDisponibilidad}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    FilledTonalButton(
                        onClick = { alHacerClicConfigurar(objetivo) },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Configurar y Asignar")
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
                text = "Trazabilidad en Tiempo Real (2° Básico A)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Seguimiento individual de porcentaje de logro, evaluaciones e intentos.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        if (listaProgreso.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                        Text("Aún no hay registros de actividades de estudiantes.")
                    }
                }
            }
        } else {
            items(listaProgreso, key = { "${it.idEstudiante}_${it.idObjetivo}" }) { progreso ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Estudiante ID: ${progreso.idEstudiante}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${progreso.porcentaje}% Logro",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (progreso.porcentaje >= 60) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { progreso.porcentaje / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp),
                            color = if (progreso.porcentaje >= 60) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Desafíos: ${progreso.desafiosCompletados} completados",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "Intentos usados: ${progreso.intentosUsados}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Text(
                            text = "Última actividad: ${progreso.fechaUltimoIntento}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
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
                text = "Informes Pedagógicos Automatizados (${informes.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Consolidado para apoderados, UTP y expediente Marcela Paz.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        items(informes, key = { it.id }) { informe ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { alHacerClicInforme(informe) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = informe.nombreEstudiante,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Text(
                                text = "${informe.porcentaje}% Logro",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${informe.nombreCurso} • ${informe.nombreAsignatura}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Observación docente: ${informe.observacionDocente}",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Puntaje: ${informe.puntajeGlobal}/${informe.puntajeMaximo}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            text = "Fecha: ${informe.fechaEmision}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }
}
