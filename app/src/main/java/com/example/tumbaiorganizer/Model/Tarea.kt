package com.example.tumbaiorganizer.Model

import android.os.Build
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

data class Tarea(
    var ID : Int,
    var Actividad : String,
    var Descripcion : String,
    var Fecha : Date,
    var Estatus : Int,
    var Prioridad : Int,
    var Categoria : Int
) {
    override fun toString(): String {
        var response = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val format = SimpleDateFormat("dd-MMMM-yyy")
            return "$Actividad - ${format.format(Fecha)}"

        } else {
            response = "$Actividad - $Fecha"
        }
        return response
    }

    fun getAPIDateFormat() : String {
        var response = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val format = SimpleDateFormat("yyy-MM-dd")
            return "${format.format(Fecha)}"

        } else {
            response = "$Fecha"
        }
        return response
    }
}