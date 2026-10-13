package com.example.mpaz_pro.ui.screens.utp

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mpaz_pro.data.local.entity.EstadoObjetivo
import com.example.mpaz_pro.data.local.entity.ObjetivoAprendizajeEntity
import com.example.mpaz_pro.ui.components.AppBottomBar
import com.example.mpaz_pro.ui.components.AppDrawer
import com.example.mpaz_pro.ui.components.BarraSuperiorMpaz
import com.example.mpaz_pro.ui.components.InsigniaEstado
import com.example.mpaz_pro.viewmodel.UtpViewModel

@Composable
fun PantallaPrincipalUtp(
    viewModel: UtpViewModel,
    alNavegarAEditar: (String) -> Unit,
    alCrearNuevoObjetivo: () -> Unit,
    alCerrarSesion: () -> Unit
) {
    val asignaturas by viewModel.asignaturas.collectAsState()
    val objetivos by viewModel.todosLosObjetivos.collectAsState()
    val idAsignaturaSeleccionada by viewModel.idAsignaturaSeleccionada.collectAsState()
    var elementoInferiorSeleccionado by remember { mutableStateOf("inicio") }

    val objetivosFiltrados = remember(objetivos, idAsignaturaSeleccionada) {
        if (idAsignaturaSeleccionada == null) objetivos
        else objetivos.filter { it.idAsignatura == idAsignaturaSeleccionada }
    }

    AppDrawer(
        pantallaActual = "Panel Principal",
        nombreUsuario = "Jefatura UTP",
        rolUsuario = "Administración Curricular",
        onPantallaSeleccionada = { opcion ->
            when (opcion) {
                "Panel Principal" -> viewModel.seleccionarAsignatura(null)
                "Módulos de Aprendizaje" -> viewModel.seleccionarAsignatura("asig_lenguaje")
                "Seguimiento e Informes" -> {}
            }
        },
        onCerrarSesion = alCerrarSesion
    ) { abrirMenu ->
        Scaffold(
            topBar = {
                BarraSuperiorMpaz(
                    titulo = "Jefatura UTP - MPAZ PRO",
                    subtitulo = "Administración Curricular 2° Básico",
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
                            "inicio" -> viewModel.seleccionarAsignatura(null)
                            "modulos" -> viewModel.seleccionarAsignatura("asig_lenguaje")
                            "perfil" -> alCerrarSesion()
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        viewModel.reiniciarFormulario(idAsignaturaSeleccionada ?: "asig_lenguaje")
                        alCrearNuevoObjetivo()
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Nuevo Objetivo de Aprendizaje")
                }
            }
        ) { rellenoInterno ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(rellenoInterno)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Filtro Curricular por Asignatura (2° Básico)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    ScrollableTabRow(
                        selectedTabIndex = asignaturas.indexOfFirst { it.id == idAsignaturaSeleccionada }.let { if (it >= 0) it + 1 else 0 },
                        edgePadding = 0.dp
                    ) {
                        Tab(
                            selected = idAsignaturaSeleccionada == null,
                            onClick = { viewModel.seleccionarAsignatura(null) },
                            text = { Text("Todas (4 Asignaturas)") }
                        )
                        asignaturas.forEach { asig ->
                            Tab(
                                selected = idAsignaturaSeleccionada == asig.id,
                                onClick = { viewModel.seleccionarAsignatura(asig.id) },
                                text = { Text(asig.nombre) }
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "Objetivos Curriculares (${objetivosFiltrados.size})",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                items(objetivosFiltrados, key = { it.id }) { objetivo ->
                    TarjetaObjetivoUtp(
                        objetivo = objetivo,
                        alHacerClicEditar = {
                            viewModel.cargarObjetivoParaEditar(objetivo)
                            alNavegarAEditar(objetivo.id)
                        },
                        alAutorizar = {
                            viewModel.cambiarEstadoObjetivo(objetivo.id, EstadoObjetivo.AUTORIZADO)
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
        }
    }
}

@Composable
fun TarjetaObjetivoUtp(
    objetivo: ObjetivoAprendizajeEntity,
    alHacerClicEditar: () -> Unit,
    alAutorizar: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
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

                InsigniaEstado(estado = objetivo.estado)
            }

            Spacer(modifier = Modifier.height(8.dp))

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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Ref: ${objetivo.textoReferencia}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = "Eje: ${objetivo.ejeCurricular}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (objetivo.estado != EstadoObjetivo.AUTORIZADO) {
                    OutlinedButton(
                        onClick = alAutorizar,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Autorizar")
                    }
                }

                FilledTonalButton(onClick = alHacerClicEditar) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Editar")
                }
            }
        }
    }
}
