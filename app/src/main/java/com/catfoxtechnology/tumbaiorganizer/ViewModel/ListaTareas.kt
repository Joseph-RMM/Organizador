package com.catfoxtechnology.tumbaiorganizer.ViewModel

import com.catfoxtechnology.tumbaiorganizer.Model.Tarea
import java.util.*

class ListaTareas {
    val listaTareas = arrayListOf<Tarea>()

    fun getTareas() {
        //TODO: Get Tareas from API
    }

    fun addTarea(Actividad : String, Descripcion : String, Fecha : Date, Estatus : Int, Prioridad : Int, Categoria : Int) {
        listaTareas.add(Tarea(2, Actividad, Descripcion, Fecha,Estatus,Prioridad,Categoria))
    }
}