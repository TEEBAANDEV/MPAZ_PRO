package com.example.mpaz_pro.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun CampoTextoMpaz(
    valor: String,
    alCambiarValor: (String) -> Unit,
    etiqueta: String,
    modificador: Modifier = Modifier,
    marcadorPosicion: String? = null,
    iconoInicio: ImageVector? = null,
    mensajeError: String? = null,
    esLineaSimple: Boolean = true,
    opcionesTeclado: KeyboardOptions = KeyboardOptions.Default,
    transformacionVisual: VisualTransformation = VisualTransformation.None,
    iconoFinal: @Composable (() -> Unit)? = null
) {
    val tieneError = !mensajeError.isNullOrBlank()

    Column(modifier = modificador.fillMaxWidth()) {
        OutlinedTextField(
            value = valor,
            onValueChange = alCambiarValor,
            label = { Text(etiqueta) },
            placeholder = marcadorPosicion?.let { { Text(it) } },
            isError = tieneError,
            singleLine = esLineaSimple,
            visualTransformation = transformacionVisual,
            keyboardOptions = opcionesTeclado,
            leadingIcon = iconoInicio?.let {
                { Icon(imageVector = it, contentDescription = etiqueta) }
            },
            trailingIcon = {
                if (tieneError) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = "Error en campo",
                        tint = MaterialTheme.colorScheme.error
                    )
                } else if (iconoFinal != null) {
                    iconoFinal()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        )

        AnimatedVisibility(
            visible = tieneError,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = mensajeError ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}
