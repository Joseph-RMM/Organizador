package com.catfoxtechnology.tumbaiorganizer.ui.detailstarea

import androidx.lifecycle.ViewModel
import com.catfoxtechnology.tumbaiorganizer.Model.Categoria
import com.catfoxtechnology.tumbaiorganizer.Model.Tarea
import java.util.*

class DetailsTareaViewModel : ViewModel() {
    var tarea :Tarea = Tarea(0,"Error","Ha ocurrido un error al cargar los datos",Date(),0,0,1)
    var categoria : Categoria = Categoria(1,"General")



}