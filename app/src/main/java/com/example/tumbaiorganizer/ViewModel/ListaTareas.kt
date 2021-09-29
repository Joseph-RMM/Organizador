package com.example.tumbaiorganizer.ViewModel

import com.example.tumbaiorganizer.Model.Tarea
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class ListaTareas {
    val listaTareas = arrayListOf<Tarea>()

    fun getTareas() {
        //TODO: Get Tareas from API
    }

    fun addTarea(Actividad : String, Fecha : Date) {
        listaTareas.add(Tarea(2, Actividad, Fecha))
    }
}