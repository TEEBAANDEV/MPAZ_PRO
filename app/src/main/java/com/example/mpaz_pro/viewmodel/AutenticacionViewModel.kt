package com.example.mpaz_pro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpaz_pro.data.local.entity.RolUsuario
import com.example.mpaz_pro.data.local.entity.UsuarioEntity
import com.example.mpaz_pro.data.repository.RepositorioApp
import com.example.mpaz_pro.domain.validation.ValidadoresFormulario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EstadoAutenticacion(
    val entradaUsuario: String = "",
    val entradaClave: String = "",
    val errorUsuario: String? = null,
    val errorClave: String? = null,
    val estaCargando: Boolean = false,
    val errorGeneral: String? = null,
    val usuarioActual: UsuarioEntity? = null
)

class AutenticacionViewModel(private val repositorio: RepositorioApp) : ViewModel() {

    private val _estadoUi = MutableStateFlow(EstadoAutenticacion())
    val estadoUi: StateFlow<EstadoAutenticacion> = _estadoUi.asStateFlow()

    fun alCambiarUsuario(valor: String) {
        val validacion = ValidadoresFormulario.validarUsuario(valor)
        _estadoUi.update {
            it.copy(
                entradaUsuario = valor,
                errorUsuario = if (valor.isNotBlank() && !validacion.esValido) validacion.mensajeError else null,
                errorGeneral = null
            )
        }
    }

    fun alCambiarClave(valor: String) {
        val validacion = ValidadoresFormulario.validarClave(valor)
        _estadoUi.update {
            it.copy(
                entradaClave = valor,
                errorClave = if (valor.isNotBlank() && !validacion.esValido) validacion.mensajeError else null,
                errorGeneral = null
            )
        }
    }

    fun iniciarSesion(alTenerExito: (RolUsuario) -> Unit) {
        val estadoActual = _estadoUi.value
        val validacionUsuario = ValidadoresFormulario.validarUsuario(estadoActual.entradaUsuario)
        val validacionClave = ValidadoresFormulario.validarClave(estadoActual.entradaClave)

        if (!validacionUsuario.esValido || !validacionClave.esValido) {
            _estadoUi.update {
                it.copy(
                    errorUsuario = validacionUsuario.mensajeError,
                    errorClave = validacionClave.mensajeError
                )
            }
            return
        }

        viewModelScope.launch {
            _estadoUi.update { it.copy(estaCargando = true, errorGeneral = null) }
            val usuario = repositorio.iniciarSesion(
                estadoActual.entradaUsuario.trim(),
                estadoActual.entradaClave.trim()
            )
            if (usuario != null) {
                _estadoUi.update { it.copy(estaCargando = false, usuarioActual = usuario, errorGeneral = null) }
                alTenerExito(usuario.rol)
            } else {
                _estadoUi.update {
                    it.copy(
                        estaCargando = false,
                        errorGeneral = "Credenciales incorrectas. Verifique usuario y contrase├▒a."
                    )
                }
            }
        }
    }

    fun accesoRapidoComo(nombreUsuario: String, alTenerExito: (RolUsuario) -> Unit) {
        viewModelScope.launch {
            _estadoUi.update { it.copy(estaCargando = true, errorGeneral = null) }
            val usuario = repositorio.iniciarSesion(nombreUsuario, "1234")
            if (usuario != null) {
                _estadoUi.update {
                    it.copy(
                        entradaUsuario = usuario.nombreUsuario,
                        entradaClave = "1234",
                        errorUsuario = null,
                        errorClave = null,
                        usuarioActual = usuario,
                        estaCargando = false
                    )
                }
                alTenerExito(usuario.rol)
            } else {
                _estadoUi.update { it.copy(estaCargando = false, errorGeneral = "Usuario demo no encontrado") }
            }
        }
    }

    fun cerrarSesion() {
        _estadoUi.update { EstadoAutenticacion() }
    }
}
