package com.example.tumbaiorganizer.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tumbaiorganizer.Model.Tarea
import java.util.*
import kotlin.collections.ArrayList

class HomeViewModel : ViewModel() {
    val listaTareas =  arrayListOf<Tarea>();


    fun getTareas() {
        //TODO: Get Tareas from API
    }

    fun addTarea(Actividad : String, Fecha : Date) {
        listaTareas.add(Tarea(2, Actividad, Fecha))
    }


    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

}