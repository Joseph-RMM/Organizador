package com.example.tumbaiorganizer.ui.detailstarea

import androidx.lifecycle.ViewModel
import com.example.tumbaiorganizer.Model.Tarea
import java.text.SimpleDateFormat
import java.util.*

class DetailsTareaViewModel : ViewModel() {
    var tarea :Tarea = Tarea(0,"Error","Ha ocurrido un error al cargar los datos",Date(),0,0,1)
}