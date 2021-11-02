package com.example.tumbaiorganizer.ui.addtareaact

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Button
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tumbaiorganizer.MainActivity
import com.example.tumbaiorganizer.Model.Tarea
import com.example.tumbaiorganizer.R
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class AddTareaActivity : AppCompatActivity() {

    val txtActividad : TextView by lazy { findViewById(R.id.txtAddActividad) }
    val txtDescripcion : TextView by lazy { findViewById(R.id.txtAddDescripcion) }
    val calendar : CalendarView by lazy { findViewById(R.id.cvAddFechaEntrega) }
    val btnAdd : Button by lazy { findViewById(R.id.btnAddTarea) }
    val btnCategoria : Button by lazy { findViewById(R.id.btnAddCategoriaTest) }
    val lblInfo : TextView by lazy { findViewById(R.id.lblInfoAddTarea) }
    val tokenAPI by lazy { intent.getStringExtra("token") }
    lateinit var selectedDate : String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tarea)

        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        lblInfo.text =""

        //Toast.makeText(this, tokenAPI+"", Toast.LENGTH_SHORT).show()

        calendar.setOnDateChangeListener(OnDateChangeListener { view, year, month, day ->
            selectedDate  = "$year-${month+1}-$day";
/*
            //show the selected date as a toast
            Toast.makeText(applicationContext, "$day/$month/$year", Toast.LENGTH_LONG).show()
            val c = Calendar.getInstance()
            c[year, month] = day
            val eventOccursOn = c.timeInMillis //this is what you want to use later*/
        })

        btnAdd.setOnClickListener {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            //val selectedDate: String = sdf.format(Date(calendar.date))
            var newTarea : Tarea = Tarea(0,
                txtActividad.text.toString(),
                txtDescripcion.text.toString(),
                sdf.parse(selectedDate),
                1,1,1
            )



            //val toast = Toast.makeText(applicationContext, newTarea.getAPIDateFormat(), Toast.LENGTH_LONG).show()
            //toast.show()
            Log.d("TareaInfo",newTarea.Estatus.toString())
            //Hacer peticion a la API
            val okHttpClient = OkHttpClient()

            val formBody = FormBody.Builder()
                .add("Nombre_Tarea",newTarea.Actividad)
                .add("Descripcion",newTarea.Descripcion)
                .add("Fecha_Finalizacion", newTarea.getAPIDateFormat())
                .add("estado",newTarea.Estatus.toString())
                .add("prioridad",newTarea.Prioridad.toString())
                .add("Id_categoria",newTarea.Categoria.toString())
                .build()

            val request = Request.Builder()
                .url("http://"+ getString(R.string.server_ip) +"/organizzdorapi/public/api/task")
                .addHeader("Authorization", "Bearer " + tokenAPI)
                .post(formBody)
                .build()

            try {
                okHttpClient.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        lblInfo.text = "Ha ocurrido un problema con el servidor"
                    } else {
                        //lblInfo.text = response.body!!.string()
                        Toast.makeText(applicationContext, "Tarea Añadida!", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }
                }
            } catch (e: java.net.ConnectException) {
                lblInfo.text = "No se ha podido conectar con el server"
            }

        }

        btnCategoria.setOnClickListener {
            val okHttpClient = OkHttpClient()

            val formBody = FormBody.Builder()
                .add("Nombre","General")
                .build()

            val request = Request.Builder()
                .url("http://"+ getString(R.string.server_ip) +"/organizzdorapi/public/api/categories")
                .addHeader("Authorization", "Bearer " + tokenAPI)
                .post(formBody)
                .build()

            try {
                okHttpClient.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        lblInfo.text = "Datos incorrectos o cuenta inexistente"
                    } else {
                        //lblInfo.text = response.body!!.string()
                    }
                }
            } catch (e: java.net.ConnectException) {
                lblInfo.text = "No se ha podido conectar con el server"
            }
        }
    }
}