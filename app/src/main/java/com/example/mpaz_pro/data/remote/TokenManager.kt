package com.example.mpaz_pro.data.remote

object TokenManager {
    @Volatile
    var jwtToken: String? = null
        private set

    fun guardarToken(token: String?) {
        jwtToken = token
    }

    fun limpiarToken() {
        jwtToken = null
    }
}
