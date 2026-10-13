package com.example.mpaz_pro.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpaz_pro.data.local.entity.AsignaturaEntity
import com.example.mpaz_pro.data.local.entity.EstadoObjetivo
import com.example.mpaz_pro.data.local.entity.ObjetivoAprendizajeEntity
import com.example.mpaz_pro.data.repository.RepositorioApp
import com.example.mpaz_pro.domain.validation.ValidadoresFormulario
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class EstadoFormularioObjetivo(
    val id: String = "",
    val idAsignatura: String = "asig_lenguaje",
    val entradaCodigo: String = "",
    val entradaTitulo: String = "",
    val entradaDescripcion: String = "",
    val entradaUnidad: String = "Unidad 1",
    val entradaEje: String = "Lectura",
    val entradaTextoReferencia: String = "Texto Escolar Mineduc 2°",
    val estado: EstadoObjetivo = EstadoObjetivo.BORRADOR,
    val errorCodigo: String? = null,
    val errorTitulo: String? = null,
    val errorDescripcion: String? = null,
    val mensajeExito: String? = null
)

class UtpViewModel(private val repositorio: RepositorioApp) : ViewModel() {

    val asignaturas: StateFlow<List<AsignaturaEntity>> = repositorio.obtenerTodasLasAsignaturas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val todosLosObjetivos: StateFlow<List<ObjetivoAprendizajeEntity>> = repositorio.obtenerTodosLosObjetivos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _idAsignaturaSeleccionada = MutableStateFlow<String?>(null)
    val idAsignaturaSeleccionada: StateFlow<String?> = _idAsignaturaSeleccionada.asStateFlow()

    private val _estadoFormulario = MutableStateFlow(EstadoFormularioObjetivo())
    val estadoFormulario: StateFlow<EstadoFormularioObjetivo> = _estadoFormulario.asStateFlow()

    fun seleccionarAsignatura(idAsignatura: String?) {
        _idAsignaturaSeleccionada.value = idAsignatura
    }

    fun cargarObjetivoParaEditar(objetivo: ObjetivoAprendizajeEntity) {
        _estadoFormulario.update {
            it.copy(
                id = objetivo.id,
                idAsignatura = objetivo.idAsignatura,
                entradaCodigo = objetivo.codigo,
                entradaTitulo = objetivo.titulo,
                entradaDescripcion = objetivo.descripcion,
                entradaUnidad = objetivo.tituloUnidad,
                entradaEje = objetivo.ejeCurricular,
                entradaTextoReferencia = objetivo.textoReferencia,
                estado = objetivo.estado,
                errorCodigo = null,
                errorTitulo = null,
                errorDescripcion = null,
                mensajeExito = null
            )
        }
    }

    fun reiniciarFormulario(idAsignaturaPorDefecto: String = "asig_lenguaje") {
        _estadoFormulario.update {
            EstadoFormularioObjetivo(
                id = "obj_${System.currentTimeMillis()}",
                idAsignatura = idAsignaturaPorDefecto
            )
        }
    }

    fun alCambiarCodigo(valor: String) {
        val validacion = ValidadoresFormulario.validarCodigoObjetivo(valor)
        _estadoFormulario.update {
            it.copy(
                entradaCodigo = valor,
                errorCodigo = if (valor.isNotBlank() && !validacion.esValido) validacion.mensajeError else null
            )
        }
    }

    fun alCambiarTitulo(valor: String) {
        val validacion = ValidadoresFormulario.validarTitulo(valor)
        _estadoFormulario.update {
            it.copy(
                entradaTitulo = valor,
                errorTitulo = if (valor.isNotBlank() && !validacion.esValido) validacion.mensajeError else null
            )
        }
    }

    fun alCambiarDescripcion(valor: String) {
        val validacion = ValidadoresFormulario.validarDescripcion(valor)
        _estadoFormulario.update {
            it.copy(
                entradaDescripcion = valor,
                errorDescripcion = if (valor.isNotBlank() && !validacion.esValido) validacion.mensajeError else null
            )
        }
    }

    fun alCambiarUnidad(valor: String) {
        _estadoFormulario.update { it.copy(entradaUnidad = valor) }
    }

    fun alCambiarEje(valor: String) {
        _estadoFormulario.update { it.copy(entradaEje = valor) }
    }

    fun alCambiarTextoReferencia(valor: String) {
        _estadoFormulario.update { it.copy(entradaTextoReferencia = valor) }
    }

    fun alCambiarEstado(nuevoEstado: EstadoObjetivo) {
        _estadoFormulario.update { it.copy(estado = nuevoEstado) }
    }

    fun guardarObjetivo(alTenerExito: () -> Unit) {
        val formulario = _estadoFormulario.value
        val validacionCodigo = ValidadoresFormulario.validarCodigoObjetivo(formulario.entradaCodigo)
        val validacionTitulo = ValidadoresFormulario.validarTitulo(formulario.entradaTitulo)
        val validacionDescripcion = ValidadoresFormulario.validarDescripcion(formulario.entradaDescripcion)

        if (!validacionCodigo.esValido || !validacionTitulo.esValido || !validacionDescripcion.esValido) {
            _estadoFormulario.update {
                it.copy(
                    errorCodigo = validacionCodigo.mensajeError,
                    errorTitulo = validacionTitulo.mensajeError,
                    errorDescripcion = validacionDescripcion.mensajeError
                )
            }
            return
        }

        viewModelScope.launch {
            val objetivo = ObjetivoAprendizajeEntity(
                id = if (formulario.id.isNotBlank()) formulario.id else "obj_${System.currentTimeMillis()}",
                idAsignatura = formulario.idAsignatura,
                codigo = formulario.entradaCodigo.uppercase().trim(),
                titulo = formulario.entradaTitulo.trim(),
                descripcion = formulario.entradaDescripcion.trim(),
                tituloUnidad = formulario.entradaUnidad.trim(),
                ejeCurricular = formulario.entradaEje.trim(),
                textoReferencia = formulario.entradaTextoReferencia.trim(),
                estado = formulario.estado,
                responsableUtp = "Jefatura UTP Marcela Paz",
                fechaCreacion = "2026-03-24"
            )
            repositorio.insertarObjetivo(objetivo)
            _estadoFormulario.update { it.copy(mensajeExito = "Objetivo guardado exitosamente con estado ${formulario.estado.name}") }
            alTenerExito()
        }
    }

    fun actualizarEstadoRapido(idObjetivo: String, nuevoEstado: EstadoObjetivo) {
        viewModelScope.launch {
            repositorio.actualizarEstadoObjetivo(idObjetivo, nuevoEstado)
        }
    }
}
