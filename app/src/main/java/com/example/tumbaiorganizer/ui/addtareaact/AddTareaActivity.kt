package com.example.tumbaiorganizer.ui.addtareaact

import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tumbaiorganizer.Model.Tarea
import com.example.tumbaiorganizer.R
import java.text.SimpleDateFormat
import java.util.*


class AddTareaActivity : AppCompatActivity() {

    val txtActividad : TextView by lazy { findViewById(R.id.txtAddActividad) }
    val calendar : CalendarView by lazy { findViewById(R.id.cvAddFechaEntrega) }
    val btnAdd : Button by lazy { findViewById(R.id.btnAddTarea) }
    lateinit var selectedDate : String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tarea)

        calendar.setOnDateChangeListener(OnDateChangeListener { view, year, month, day ->
            selectedDate  = "$day/${month+1}/$year";
/*
            //show the selected date as a toast
            Toast.makeText(applicationContext, "$day/$month/$year", Toast.LENGTH_LONG).show()
            val c = Calendar.getInstance()
            c[year, month] = day
            val eventOccursOn = c.timeInMillis //this is what you want to use later*/
        })

        btnAdd.setOnClickListener {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            //val selectedDate: String = sdf.format(Date(calendar.date))
            var newTarea : Tarea = Tarea(2,txtActividad.text.toString(),sdf.parse(selectedDate))
            //TODO: subir "newTarea" a la base de datos. Por ahora solo se muestra en toast
            val toast = Toast.makeText(applicationContext, newTarea.toString(), Toast.LENGTH_LONG)
            toast.show()
        }
    }
}