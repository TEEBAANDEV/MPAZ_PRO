package com.example.mpaz_pro.ui.screens.utp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mpaz_pro.data.local.entity.EstadoObjetivo
import com.example.mpaz_pro.ui.components.BarraSuperiorMpaz
import com.example.mpaz_pro.ui.components.CampoTextoMpaz
import com.example.mpaz_pro.ui.components.InsigniaEstado
import com.example.mpaz_pro.viewmodel.UtpViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEditarObjetivoUtp(
    viewModel: UtpViewModel,
    alNavegarAtras: () -> Unit
) {
    val estadoFormulario by viewModel.estadoFormulario.collectAsState()
    val estadoDesplazamiento = rememberScrollState()
    var menuEstadoDesplegado by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            BarraSuperiorMpaz(
                titulo = if (estadoFormulario.id.isNotBlank()) "Editar Objetivo" else "Nuevo Objetivo",
                subtitulo = "Jefatura UTP - Marcela Paz",
                alNavegarAtras = alNavegarAtras
            )
        }
    ) { rellenoInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(rellenoInterno)
                .verticalScroll(estadoDesplazamiento)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Datos Curriculares y Trazabilidad",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // Formulario Completo con Validación Visual por Campo Desacoplada
            CampoTextoMpaz(
                valor = estadoFormulario.entradaCodigo,
                alCambiarValor = { viewModel.alCambiarCodigo(it) },
                etiqueta = "Código Curricular Oficial",
                marcadorPosicion = "ej: OA 04",
                iconoInicio = Icons.Default.Tag,
                mensajeError = estadoFormulario.errorCodigo
            )

            CampoTextoMpaz(
                valor = estadoFormulario.entradaTitulo,
                alCambiarValor = { viewModel.alCambiarTitulo(it) },
                etiqueta = "Título del Objetivo de Aprendizaje",
                marcadorPosicion = "ej: Lectura guiada de cuentos breves",
                iconoInicio = Icons.Default.Bookmark,
                mensajeError = estadoFormulario.errorTitulo
            )

            CampoTextoMpaz(
                valor = estadoFormulario.entradaDescripcion,
                alCambiarValor = { viewModel.alCambiarDescripcion(it) },
                etiqueta = "Descripción del Objetivo (Mineduc)",
                marcadorPosicion = "Describa detalladamente el aprendizaje esperado para 2° básico",
                iconoInicio = Icons.Default.Description,
                esLineaSimple = false,
                mensajeError = estadoFormulario.errorDescripcion
            )

            CampoTextoMpaz(
                valor = estadoFormulario.entradaUnidad,
                alCambiarValor = { viewModel.alCambiarUnidad(it) },
                etiqueta = "Unidad Didáctica",
                marcadorPosicion = "ej: Unidad 1: Mundos de Papel",
                iconoInicio = Icons.Default.Layers
            )

            CampoTextoMpaz(
                valor = estadoFormulario.entradaEje,
                alCambiarValor = { viewModel.alCambiarEje(it) },
                etiqueta = "Eje Curricular",
                marcadorPosicion = "ej: Lectura y Comprensión",
                iconoInicio = Icons.Default.Book
            )

            CampoTextoMpaz(
                valor = estadoFormulario.entradaTextoReferencia,
                alCambiarValor = { viewModel.alCambiarTextoReferencia(it) },
                etiqueta = "Texto de Referencia / Licencia Mineduc",
                marcadorPosicion = "ej: Guía docente Mineduc 2° Básico",
                iconoInicio = Icons.Default.Book
            )

            // Selector de Estado Curricular
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Estado del Contenido Pedagógico:",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    ExposedDropdownMenuBox(
                        expanded = menuEstadoDesplegado,
                        onExpandedChange = { menuEstadoDesplegado = !menuEstadoDesplegado }
                    ) {
                        OutlinedTextField(
                            value = estadoFormulario.estado.name,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuEstadoDesplegado) },
                            modifier = Modifier
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = menuEstadoDesplegado,
                            onDismissRequest = { menuEstadoDesplegado = false }
                        ) {
                            EstadoObjetivo.values().forEach { st ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(st.name)
                                            InsigniaEstado(estado = st)
                                        }
                                    },
                                    onClick = {
                                        viewModel.alCambiarEstado(st)
                                        menuEstadoDesplegado = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = estadoFormulario.mensajeExito != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = estadoFormulario.mensajeExito ?: "",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.guardarObjetivo(alNavegarAtras) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Guardar Cambios Curriculares",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

