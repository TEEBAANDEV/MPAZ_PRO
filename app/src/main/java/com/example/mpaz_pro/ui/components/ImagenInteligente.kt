package com.example.mpaz_pro.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.mpaz_pro.data.CamaraUriFactory

@Composable
fun ImagenInteligente(
    imagenUri: Any?,
    modifier: Modifier = Modifier,
    tamano: Dp = 100.dp,
    iconoPorDefecto: ImageVector = Icons.Default.Person,
    tamanoIcono: Dp = 54.dp,
    colorBorde: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.75f),
    descripcion: String = "Imagen de perfil"
) {
    val tieneImagen = when (imagenUri) {
        null -> false
        is String -> imagenUri.isNotBlank()
        is Uri -> imagenUri != Uri.EMPTY
        else -> true
    }

    Box(
        modifier = modifier
            .size(tamano)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(3.dp, colorBorde, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (tieneImagen) {
            AsyncImage(
                model = imagenUri,
                contentDescription = descripcion,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = iconoPorDefecto,
                contentDescription = descripcion,
                modifier = Modifier.size(tamanoIcono),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun AvatarConLapizPerfil(
    fotoUri: String?,
    onFotoCambiada: (Uri?) -> Unit,
    modifier: Modifier = Modifier,
    tamano: Dp = 110.dp,
    iconoPorDefecto: ImageVector = Icons.Default.Person,
    permiteEditar: Boolean = true
) {
    val context = LocalContext.current
    var mostrarDialogo by remember { mutableStateOf(false) }
    var uriPendienteCamara by remember { mutableStateOf<Uri?>(null) }

    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uriSeleccionado: Uri? ->
        if (uriSeleccionado != null) {
            onFotoCambiada(uriSeleccionado)
            Toast.makeText(context, "Foto de perfil actualizada desde la galería", Toast.LENGTH_SHORT).show()
        }
    }

    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { capturaExitosa: Boolean ->
        if (capturaExitosa && uriPendienteCamara != null) {
            onFotoCambiada(uriPendienteCamara)
            Toast.makeText(context, "Foto de perfil tomada con éxito", Toast.LENGTH_SHORT).show()
        } else {
            uriPendienteCamara = null
        }
    }

    val permisoCamaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido: Boolean ->
        if (concedido) {
            try {
                val uri = CamaraUriFactory.crearUri(context)
                uriPendienteCamara = uri
                camaraLauncher.launch(uri)
            } catch (e: Exception) {
                Toast.makeText(context, "Error al abrir la cámara: ${e.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "Debes conceder el permiso de cámara en Ajustes para tomar una fotografía.", Toast.LENGTH_LONG).show()
        }
    }

    fun abrirCamaraSegura() {
        val permisoConcedido = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (permisoConcedido) {
            try {
                val uri = CamaraUriFactory.crearUri(context)
                uriPendienteCamara = uri
                camaraLauncher.launch(uri)
            } catch (e: Exception) {
                Toast.makeText(context, "Error al abrir la cámara: ${e.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            permisoCamaraLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Box(
        modifier = modifier
            .size(tamano + 16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Avatar principal
        ImagenInteligente(
            imagenUri = fotoUri,
            tamano = tamano,
            iconoPorDefecto = iconoPorDefecto,
            tamanoIcono = tamano * 0.55f,
            modifier = Modifier.clickable(enabled = permiteEditar) {
                mostrarDialogo = true
            }
        )

        // Botón flotante con ícono de lápiz junto al perfil
        if (permiteEditar) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 4.dp, bottom = 4.dp)
                    .size(36.dp)
                    .shadow(4.dp, CircleShape)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    .clickable {
                        mostrarDialogo = true
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Cambiar foto de perfil",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = {
                Text(
                    text = "Foto de perfil",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Personaliza tu imagen de perfil en MPAZ PRO:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Button(
                        onClick = {
                            mostrarDialogo = false
                            galeriaLauncher.launch("image/*")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Collections, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Elegir desde galería")
                    }

                    OutlinedButton(
                        onClick = {
                            mostrarDialogo = false
                            abrirCamaraSegura()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Tomar fotografía")
                    }

                    if (!fotoUri.isNullOrBlank()) {
                        TextButton(
                            onClick = {
                                mostrarDialogo = false
                                onFotoCambiada(null)
                                Toast.makeText(context, "Foto de perfil restablecida", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Quitar foto actual")
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
