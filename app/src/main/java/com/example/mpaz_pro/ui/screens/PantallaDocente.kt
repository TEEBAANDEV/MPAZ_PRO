package com.example.mpaz_pro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

    var informeParaEditar by remember { mutableStateOf<InformeEntity?>(null) }

    val pantallaActual = when (pestañaSeleccionada) {
        0 -> "Panel Principal"
        1 -> "Módulos de Aprendizaje"
        2 -> "Seguimiento e Informes"
        else -> "Perfil del Docente"
    }

    AppDrawer(
        pantallaActual = pantallaActual,
        nombreUsuario = "Prof. Mario Silva",
        rolUsuario = "Docente 2° Básico",
        onPantallaSeleccionada = { seccion ->
            when (seccion) {
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
                        2 -> "informes"
                        else -> "perfil"
                    },
                    alSeleccionarElemento = { id ->
                        when (id) {
                            "inicio" -> pestañaSeleccionada = 0
                            "modulos" -> pestañaSeleccionada = 1
                            "informes" -> pestañaSeleccionada = 2
                            "perfil" -> pestañaSeleccionada = 3
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
                                Text(
                                    text = "Monitoreo de Aprendizaje en Vivo (${informes.size} Alumnos)",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            items(informes, key = { it.id }) { inf ->
                                val estiloAsig = com.example.mpaz_pro.ui.theme.ColoresAsignatura.obtenerEstilo(inf.asignatura)
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, estiloAsig.colorBorde),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(inf.nombreEstudiante, fontWeight = FontWeight.Bold)
                                            Text("${inf.porcentajeLogro}% Logro", fontWeight = FontWeight.ExtraBold, color = estiloAsig.colorPrincipal)
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "${estiloAsig.emoji} ${inf.asignatura}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = estiloAsig.colorPrincipal,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(" • ${inf.curso}", style = MaterialTheme.typography.bodySmall)
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        LinearProgressIndicator(
                                            progress = { inf.porcentajeLogro / 100f },
                                            modifier = Modifier.fillMaxWidth().height(8.dp),
                                            color = estiloAsig.colorPrincipal
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
                                    text = "Gestión de Módulos y Objetivos Activos",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
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
                                            Text(text = "${obj.codigo}: ${obj.titulo}", fontWeight = FontWeight.Bold)
                                            Text(
                                                text = "${estiloObj.emoji} ${obj.nombreAsignatura}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = estiloObj.colorPrincipal,
                                                fontWeight = FontWeight.SemiBold
                                            )
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
                    2 -> {

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
                    3 -> {

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
                                    contentDescription = "Foto Docente",
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
                                    Text(text = "Información del Usuario", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
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
}
