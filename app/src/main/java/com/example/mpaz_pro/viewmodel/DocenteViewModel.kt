package com.example.mpaz_pro.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpaz_pro.data.local.entity.InformePedagogicoEntity
import com.example.mpaz_pro.data.local.entity.ObjetivoAprendizajeEntity
import com.example.mpaz_pro.data.local.entity.ProgresoEstudianteEntity
import com.example.mpaz_pro.data.repository.RepositorioApp
import com.example.mpaz_pro.domain.validation.ValidadoresFormulario
import com.example.mpaz_pro.native.AyudanteCompartirInforme
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class EstadoFormularioAsignacion(
    val idObjetivo: String = "",
    val codigoObjetivo: String = "",
    val tituloObjetivo: String = "",
    val curso: String = "2┬░ B├ísico A",
    val entradaIntentosMaximos: String = "2",
    val entradaFechaInicio: String = "2026-03-25",
    val entradaFechaFin: String = "2026-04-30",
    val estaAsignado: Boolean = true,
    val errorIntentosMaximos: String? = null,
    val errorFechaInicio: String? = null,
    val errorFechaFin: String? = null,
    val mensajeExito: String? = null
)

data class EstadoFormularioObservacion(
    val idInforme: String = "",
    val entradaObservacion: String = "",
    val errorObservacion: String? = null,
    val seGuardoConExito: Boolean = false
)

class DocenteViewModel(private val repositorio: RepositorioApp) : ViewModel() {

    val objetivosAutorizados: StateFlow<List<ObjetivoAprendizajeEntity>> =
        repositorio.obtenerObjetivosAutorizados()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val todosLosProgresos: StateFlow<List<ProgresoEstudianteEntity>> =
        repositorio.obtenerTodosLosProgresos()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val informes: StateFlow<List<InformePedagogicoEntity>> =
        repositorio.obtenerTodosLosInformes()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _formularioAsignacion = MutableStateFlow(EstadoFormularioAsignacion())
    val formularioAsignacion: StateFlow<EstadoFormularioAsignacion> = _formularioAsignacion.asStateFlow()

    private val _formularioObservacion = MutableStateFlow(EstadoFormularioObservacion())
    val formularioObservacion: StateFlow<EstadoFormularioObservacion> = _formularioObservacion.asStateFlow()

    fun cargarObjetivoParaAsignacion(objetivo: ObjetivoAprendizajeEntity) {
        _formularioAsignacion.update {
            it.copy(
                idObjetivo = objetivo.id,
                codigoObjetivo = objetivo.codigo,
                tituloObjetivo = objetivo.titulo,
                curso = objetivo.cursoAsignado,
                entradaIntentosMaximos = objetivo.intentosMaximos.toString(),
                entradaFechaInicio = objetivo.fechaInicioDisponibilidad,
                entradaFechaFin = objetivo.fechaFinDisponibilidad,
                estaAsignado = objetivo.estaAsignadoAlCurso,
                errorIntentosMaximos = null,
                errorFechaInicio = null,
                errorFechaFin = null,
                mensajeExito = null
            )
        }
    }

    fun alCambiarIntentosMaximos(valor: String) {
        val validacion = ValidadoresFormulario.validarIntentosMaximos(valor)
        _formularioAsignacion.update {
            it.copy(
                entradaIntentosMaximos = valor,
                errorIntentosMaximos = if (valor.isNotBlank() && !validacion.esValido) validacion.mensajeError else null
            )
        }
    }

    fun alCambiarFechaInicio(valor: String) {
        val validacion = ValidadoresFormulario.validarFormatoFecha(valor)
        _formularioAsignacion.update {
            it.copy(
                entradaFechaInicio = valor,
                errorFechaInicio = if (valor.isNotBlank() && !validacion.esValido) validacion.mensajeError else null
            )
        }
    }

    fun alCambiarFechaFin(valor: String) {
        val validacion = ValidadoresFormulario.validarFormatoFecha(valor)
        _formularioAsignacion.update {
            it.copy(
                entradaFechaFin = valor,
                errorFechaFin = if (valor.isNotBlank() && !validacion.esValido) validacion.mensajeError else null
            )
        }
    }

    fun alAlternarAsignacion(asignado: Boolean) {
        _formularioAsignacion.update { it.copy(estaAsignado = asignado) }
    }

    fun guardarAsignacion(alTenerExito: () -> Unit) {
        val estado = _formularioAsignacion.value
        val validacionIntentos = ValidadoresFormulario.validarIntentosMaximos(estado.entradaIntentosMaximos)
        val validacionInicio = ValidadoresFormulario.validarFormatoFecha(estado.entradaFechaInicio)
        val validacionFin = ValidadoresFormulario.validarFormatoFecha(estado.entradaFechaFin)

        if (!validacionIntentos.esValido || !validacionInicio.esValido || !validacionFin.esValido) {
            _formularioAsignacion.update {
                it.copy(
                    errorIntentosMaximos = validacionIntentos.mensajeError,
                    errorFechaInicio = validacionInicio.mensajeError,
                    errorFechaFin = validacionFin.mensajeError
                )
            }
            return
        }

        viewModelScope.launch {
            repositorio.actualizarAsignacion(
                id = estado.idObjetivo,
                estaAsignado = estado.estaAsignado,
                intentosMaximos = estado.entradaIntentosMaximos.toInt(),
                fechaInicio = estado.entradaFechaInicio.trim(),
                fechaFin = estado.entradaFechaFin.trim()
            )
            _formularioAsignacion.update { it.copy(mensajeExito = "Asignaci├│n guardada para el curso 2┬░ B├ísico") }
            alTenerExito()
        }
    }

    fun cargarInformeParaObservacion(informe: InformePedagogicoEntity) {
        _formularioObservacion.update {
            EstadoFormularioObservacion(
                idInforme = informe.id,
                entradaObservacion = informe.observacionDocente,
                errorObservacion = null,
                seGuardoConExito = false
            )
        }
    }

    fun alCambiarObservacion(valor: String) {
        val validacion = ValidadoresFormulario.validarObservacionDocente(valor)
        _formularioObservacion.update {
            it.copy(
                entradaObservacion = valor,
                errorObservacion = if (valor.isNotBlank() && !validacion.esValido) validacion.mensajeError else null,
                seGuardoConExito = false
            )
        }
    }

    fun guardarObservacion(alTenerExito: () -> Unit) {
        val formulario = _formularioObservacion.value
        val validacion = ValidadoresFormulario.validarObservacionDocente(formulario.entradaObservacion)

        if (!validacion.esValido) {
            _formularioObservacion.update { it.copy(errorObservacion = validacion.mensajeError) }
            return
        }

        viewModelScope.launch {
            repositorio.actualizarObservacionDocente(formulario.idInforme, formulario.entradaObservacion.trim())
            _formularioObservacion.update { it.copy(seGuardoConExito = true, errorObservacion = null) }
            alTenerExito()
        }
    }

    fun compartirInformeDeFormaNativa(contexto: Context, informe: InformePedagogicoEntity) {
        AyudanteCompartirInforme.compartirInforme(contexto, informe)
    }
}
