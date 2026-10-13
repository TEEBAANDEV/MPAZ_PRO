package com.example.mpaz_pro.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Convertidores {
    private val gson = Gson()

    @TypeConverter
    fun desdeListaCadena(valor: List<String>?): String {
        return gson.toJson(valor ?: emptyList<String>())
    }

    @TypeConverter
    fun haciaListaCadena(valor: String?): List<String> {
        if (valor.isNullOrEmpty()) return emptyList()
        val tipoLista = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(valor, tipoLista)
    }
}
