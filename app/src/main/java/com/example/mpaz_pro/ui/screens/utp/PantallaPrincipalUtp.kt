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
import com.example.mpaz_pro.ui.components.BarraSuperiorMpaz
import com.example.mpaz_pro.ui.components.InsigniaEstado
import com.example.mpaz_pro.ui.viewmodel.UtpViewModel

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

    val objetivosFiltrados = remember(objetivos, idAsignaturaSeleccionada) {
        if (idAsignaturaSeleccionada == null) objetivos
        else objetivos.filter { it.idAsignatura == idAsignaturaSeleccionada }
    }

    Scaffold(
        topBar = {
            BarraSuperiorMpaz(
                titulo = "Jefatura UTP - MPAZ PRO",
                subtitulo = "Administración Curricular de 2° Básico",
                alCerrarSesion = alCerrarSesion
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
                    text = "Asignaturas Principales (4)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = idAsignaturaSeleccionada == null,
                        onClick = { viewModel.seleccionarAsignatura(null) },
                        label = { Text("Todas") }
                    )
                    asignaturas.forEach { asignatura ->
                        FilterChip(
                            selected = idAsignaturaSeleccionada == asignatura.id,
                            onClick = { viewModel.seleccionarAsignatura(asignatura.id) },
                            label = { Text(asignatura.codigo) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Text(
                    text = "Objetivos y Unidades Didácticas (${objetivosFiltrados.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(objetivosFiltrados, key = { it.id }) { objetivo ->
                TarjetaObjetivoUtp(
                    objetivo = objetivo,
                    alHacerClicEditar = {
                        viewModel.cargarObjetivoParaEditar(objetivo)
                        alNavegarAEditar(objetivo.id)
                    },
                    alAprobarRapido = {
                        viewModel.actualizarEstadoRapido(objetivo.id, EstadoObjetivo.AUTORIZADO)
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun TarjetaObjetivoUtp(
    objetivo: ObjetivoAprendizajeEntity,
    alHacerClicEditar: () -> Unit,
    alAprobarRapido: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = objetivo.codigo,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "• ${objetivo.tituloUnidad}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                InsigniaEstado(estado = objetivo.estado)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = objetivo.titulo,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = objetivo.descripcion,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Eje: ${objetivo.ejeCurricular}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (objetivo.estado == EstadoObjetivo.EN_REVISION || objetivo.estado == EstadoObjetivo.BORRADOR) {
                        FilledTonalButton(
                            onClick = alAprobarRapido,
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Autorizar", style = MaterialTheme.typography.labelSmall)
                        }
                    }

                    IconButton(onClick = alHacerClicEditar) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar objetivo",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
