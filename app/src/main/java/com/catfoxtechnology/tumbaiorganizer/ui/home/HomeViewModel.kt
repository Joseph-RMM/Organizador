package com.catfoxtechnology.tumbaiorganizer.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.catfoxtechnology.tumbaiorganizer.Model.Tarea
import java.util.*

class HomeViewModel : ViewModel() {
    val listaTareas =  arrayListOf<Tarea>();


    fun getTareas() {
        //TODO: Get Tareas from API
    }

    fun addTarea(Actividad : String, Descripcion : String, Fecha : Date, Estatus : Int, Prioridad : Int, Categoria : Int) {
        listaTareas.add(Tarea(2, Actividad, Descripcion, Fecha,Estatus,Prioridad,Categoria))
    }

    fun addTarea(tarea: Tarea) {
        listaTareas.add(tarea)
    }

    fun clearList() {
        listaTareas.clear()
    }


    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

}