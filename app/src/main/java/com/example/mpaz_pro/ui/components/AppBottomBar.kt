package com.example.mpaz_pro.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class ElementoNavegacionInferior(
    val id: String,
    val etiqueta: String,
    val icono: ImageVector
)

val elementosNavegacionDefault = listOf(
    ElementoNavegacionInferior("inicio", "Inicio", Icons.Default.Home),
    ElementoNavegacionInferior("modulos", "Módulos", Icons.Default.Book),
    ElementoNavegacionInferior("informes", "Informes", Icons.Default.Assessment),
    ElementoNavegacionInferior("perfil", "Perfil", Icons.Default.Person)
)

@Composable
fun AppBottomBar(
    elementoSeleccionado: String,
    elementos: List<ElementoNavegacionInferior> = elementosNavegacionDefault,
    alSeleccionarElemento: (String) -> Unit
) {
    NavigationBar {
        elementos.forEach { item ->
            NavigationBarItem(
                selected = elementoSeleccionado == item.id,
                onClick = { alSeleccionarElemento(item.id) },
                icon = {
                    Icon(
                        imageVector = item.icono,
                        contentDescription = item.etiqueta
                    )
                },
                label = {
                    Text(text = item.etiqueta)
                }
            )
        }
    }
}
