package com.example.tumbaiorganizer.ui.detailstarea

import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.tumbaiorganizer.Model.Categoria
import com.example.tumbaiorganizer.Model.Tarea
import com.example.tumbaiorganizer.R
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class DetailsTareaViewModel : ViewModel() {
    var tarea :Tarea = Tarea(0,"Error","Ha ocurrido un error al cargar los datos",Date(),0,0,1)
    var categoria : Categoria = Categoria(1,"General")



}