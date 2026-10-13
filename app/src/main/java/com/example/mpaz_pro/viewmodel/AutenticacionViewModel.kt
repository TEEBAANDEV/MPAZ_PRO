package com.example.mpaz_pro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpaz_pro.data.local.UsuarioEntity
import com.example.mpaz_pro.data.model.FormularioLoginErrores
import com.example.mpaz_pro.data.model.FormularioLoginModel
import com.example.mpaz_pro.data.repository.RepositorioApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EstadoAutenticacion(
    val estaCargando: Boolean = false,
    val errorGeneral: String? = null,
    val usuarioActual: UsuarioEntity? = null
)

/**
 * ViewModel de autenticación con el patrón de MiAppFormulario
 * (FormularioLoginModel y FormularioLoginErrores).
 */
class AutenticacionViewModel(private val repositorio: RepositorioApp) : ViewModel() {

    private val _formulario = MutableStateFlow(FormularioLoginModel())
    val formulario: StateFlow<FormularioLoginModel> = _formulario.asStateFlow()

    private val _errores = MutableStateFlow(FormularioLoginErrores())
    val errores: StateFlow<FormularioLoginErrores> = _errores.asStateFlow()

    private val _estadoUi = MutableStateFlow(EstadoAutenticacion())
    val estadoUi: StateFlow<EstadoAutenticacion> = _estadoUi.asStateFlow()

    fun alCambiarUsuario(valor: String) {
        _formulario.value = _formulario.value.copy(rut = valor)
        _errores.value = _errores.value.copy(rut = null)
        _estadoUi.update { it.copy(errorGeneral = null) }
    }

    fun alCambiarClave(valor: String) {
        _formulario.value = _formulario.value.copy(clave = valor)
        _errores.value = _errores.value.copy(clave = null)
        _estadoUi.update { it.copy(errorGeneral = null) }
    }

    fun validar(): Boolean {
        val f = _formulario.value
        val errRut = if (f.rut.isBlank()) "Debe ingresar su usuario o RUT" else null
        val errClave = if (f.clave.isBlank()) "Debe ingresar su clave" else null
        val errores = FormularioLoginErrores(errRut, errClave)
        _errores.value = errores
        return errores == FormularioLoginErrores()
    }

    fun iniciarSesion(alTenerExito: (String) -> Unit) {
        if (!validar()) return

        val f = _formulario.value
        viewModelScope.launch {
            _estadoUi.update { it.copy(estaCargando = true, errorGeneral = null) }
            val u = repositorio.iniciarSesion(f.rut.trim(), f.clave.trim())
            if (u != null) {
                _estadoUi.update { it.copy(estaCargando = false, usuarioActual = u) }
                alTenerExito(u.rol)
            } else {
                _estadoUi.update {
                    it.copy(
                        estaCargando = false,
                        errorGeneral = "Credenciales incorrectas. Verifique usuario y clave."
                    )
                }
            }
        }
    }

    fun accesoRapido(usuario: String, alTenerExito: (String) -> Unit) {
        viewModelScope.launch {
            _estadoUi.update { it.copy(estaCargando = true, errorGeneral = null) }
            val u = repositorio.iniciarSesion(usuario, "1234")
            if (u != null) {
                _formulario.value = FormularioLoginModel(u.nombreUsuario, "1234")
                _errores.value = FormularioLoginErrores()
                _estadoUi.update { it.copy(estaCargando = false, usuarioActual = u) }
                alTenerExito(u.rol)
            }
        }
    }

    fun cerrarSesion() {
        _formulario.value = FormularioLoginModel()
        _errores.value = FormularioLoginErrores()
        _estadoUi.value = EstadoAutenticacion()
    }
}
