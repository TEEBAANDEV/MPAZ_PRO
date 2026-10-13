package com.example.mpaz_pro.domain.validation

object ValidadoresFormulario {

    fun validarUsuario(entrada: String): ResultadoValidacion {
        val textoLimpio = entrada.trim()
        return when {
            textoLimpio.isEmpty() -> ResultadoValidacion(false, "El nombre de usuario es obligatorio.")
            textoLimpio.length < 3 -> ResultadoValidacion(false, "El usuario debe tener al menos 3 caracteres.")
            !textoLimpio.matches(Regex("^[a-zA-Z0-9_.-]+$")) -> ResultadoValidacion(false, "Solo se permiten letras, números, puntos o guiones.")
            else -> ResultadoValidacion(true)
        }
    }

    fun validarClave(entrada: String): ResultadoValidacion {
        return when {
            entrada.isEmpty() -> ResultadoValidacion(false, "La contraseña es obligatoria.")
            entrada.length < 4 -> ResultadoValidacion(false, "La contraseña debe tener al menos 4 caracteres.")
            else -> ResultadoValidacion(true)
        }
    }

    fun validarCodigoObjetivo(entrada: String): ResultadoValidacion {
        val textoLimpio = entrada.trim()
        return when {
            textoLimpio.isEmpty() -> ResultadoValidacion(false, "El código del OA es obligatorio (ej: OA 04).")
            !textoLimpio.uppercase().matches(Regex("^OA\\s*\\d{1,2}$")) -> {
                ResultadoValidacion(false, "Formato inválido. Debe ser como 'OA 04' o 'OA 1'.")
            }
            else -> ResultadoValidacion(true)
        }
    }

    fun validarTitulo(entrada: String, nombreCampo: String = "El título"): ResultadoValidacion {
        val textoLimpio = entrada.trim()
        return when {
            textoLimpio.isEmpty() -> ResultadoValidacion(false, "$nombreCampo no puede estar vacío.")
            textoLimpio.length < 5 -> ResultadoValidacion(false, "$nombreCampo debe tener al menos 5 caracteres.")
            else -> ResultadoValidacion(true)
        }
    }

    fun validarDescripcion(entrada: String, longitudMinima: Int = 10): ResultadoValidacion {
        val textoLimpio = entrada.trim()
        return when {
            textoLimpio.isEmpty() -> ResultadoValidacion(false, "La descripción pedagógica es requerida.")
            textoLimpio.length < longitudMinima -> ResultadoValidacion(false, "La descripción debe tener al menos $longitudMinima caracteres.")
            else -> ResultadoValidacion(true)
        }
    }

    fun validarIntentosMaximos(entrada: String): ResultadoValidacion {
        val numero = entrada.toIntOrNull()
        return when {
            numero == null -> ResultadoValidacion(false, "Ingrese un número válido de intentos.")
            numero < 1 -> ResultadoValidacion(false, "El número de intentos debe ser al menos 1.")
            numero > 10 -> ResultadoValidacion(false, "El máximo permitido son 10 intentos.")
            else -> ResultadoValidacion(true)
        }
    }

    fun validarFormatoFecha(entrada: String): ResultadoValidacion {
        val textoLimpio = entrada.trim()
        return when {
            textoLimpio.isEmpty() -> ResultadoValidacion(false, "La fecha no puede estar vacía.")
            !textoLimpio.matches(Regex("^\\d{4}-\\d{2}-\\d{2}$")) -> {
                ResultadoValidacion(false, "Formato requerido: AAAA-MM-DD (ej: 2026-04-30).")
            }
            else -> ResultadoValidacion(true)
        }
    }

    fun validarObservacionDocente(entrada: String): ResultadoValidacion {
        val textoLimpio = entrada.trim()
        return when {
            textoLimpio.isEmpty() -> ResultadoValidacion(false, "Debe ingresar una observación pedagógica antes de guardar.")
            textoLimpio.length < 8 -> ResultadoValidacion(false, "La observación debe tener al menos 8 caracteres explicativos.")
            else -> ResultadoValidacion(true)
        }
    }
}
