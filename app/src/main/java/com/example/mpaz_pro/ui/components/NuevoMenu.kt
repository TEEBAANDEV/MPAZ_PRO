package com.example.mpaz_pro.ui.components

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun NuevoMenu(
    isExpanded: Boolean,
    onItemClick: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val opciones = listOf(
        "Escuela Marcela Paz",
        "Reglamento de Evaluación",
        "Tema y Alto Contraste",
        "Cerrar Sesión"
    )

    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = onDismiss
    ) {
        opciones.forEach { opcion ->
            DropdownMenuItem(
                text = {
                    Text(text = opcion)
                },
                onClick = {
                    onItemClick(opcion)
                    onDismiss()
                }
            )
        }
    }
}
