package com.catfoxtechnology.tumbaiorganizer.ui.addtareaact

import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.*
import android.widget.CalendarView.OnDateChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.catfoxtechnology.tumbaiorganizer.Model.Categoria
import com.catfoxtechnology.tumbaiorganizer.Model.Tarea
import com.catfoxtechnology.tumbaiorganizer.R
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONTokener
import java.text.SimpleDateFormat


class AddTareaActivity : AppCompatActivity() {

    val txtActividad : TextView by lazy { findViewById(R.id.txtAddActividad) }
    val txtDescripcion : TextView by lazy { findViewById(R.id.txtAddDescripcion) }
    val calendar : CalendarView by lazy { findViewById(R.id.cvAddFechaEntrega) }
    val btnAdd : Button by lazy { findViewById(R.id.btnAddTarea) }
    val lblInfo : TextView by lazy { findViewById(R.id.lblInfoAddTarea) }
    val spinCat : Spinner by lazy { findViewById(R.id.spinnerCategorias) }
    val tokenAPI by lazy { intent.getStringExtra("token") }
    lateinit var selectedDate : String;
    val listaCategorias = arrayListOf<Categoria>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tarea)

        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        lblInfo.text =""
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        var today = java.util.Calendar.getInstance().time
        selectedDate = sdf.format(today)
        Log.d("Date setted","$selectedDate")
        ShowCats()
/*
        spinCat.onItemSelectedListener = object :
        AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
                Log.d("SelectedID","${listaCategorias[spinCat.selectedItemPosition].Id_categoria}")
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }*/


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
            if (txtActividad.text.toString() == "" || txtDescripcion.text.toString() == "") {
                lblInfo.text = "No has llenado todos los campos"
            } else {

                val sdf = SimpleDateFormat("yyyy-MM-dd")

                //val selectedDate: String = sdf.format(Date(calendar.date))
                var catID: Categoria = listaCategorias[spinCat.selectedItemPosition]
                var newTarea: Tarea = Tarea(
                    0,
                    txtActividad.text.toString(),
                    txtDescripcion.text.toString(),
                    sdf.parse(selectedDate),
                    1, 1,
                    catID.Id_categoria
                )

                Log.d("Cat ID", "${catID.Id_categoria}")

                //val toast = Toast.makeText(applicationContext, newTarea.getAPIDateFormat(), Toast.LENGTH_LONG).show()
                //toast.show()
                Log.d("TareaInfo", newTarea.Estatus.toString())
                //Hacer peticion a la API
                val okHttpClient = OkHttpClient()

                val formBody = FormBody.Builder()
                    .add("Nombre_Tarea", newTarea.Actividad)
                    .add("Descripcion", newTarea.Descripcion)
                    .add("Fecha_Finalizacion", newTarea.getAPIDateFormat())
                    .add("estado", newTarea.Estatus.toString())
                    .add("prioridad", newTarea.Prioridad.toString())
                    .add("Id_categoria", newTarea.Categoria.toString())
                    .build()

                val request = Request.Builder()
                    .url("http://" + getString(R.string.server_ip) + "/organizzdorapi/public/api/task")
                    .addHeader("Authorization", "Bearer " + tokenAPI)
                    .post(formBody)
                    .build()

                try {
                    okHttpClient.newCall(request).execute().use { response ->
                        if (!response.isSuccessful) {
                            lblInfo.text = "Máximo 100 caracteres"
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
        }


    }

     fun CreateGeneralCat() {
         Log.d("No Cats found","Generating General Cat")
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
                     lblInfo.setText("Datos incorrectos o cuenta inexistente")
                 } else {
                     Log.d("API Response:", response.body!!.string())
                 }
             }
         } catch (e: java.net.ConnectException) {
             lblInfo.setText("No se ha podido conectar con el server")
         }
     }

    fun ShowCats(){
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url("http://"+ getString(R.string.server_ip) +"/organizzdorapi/public/api/categories")
            .addHeader("Authorization", "Bearer " + tokenAPI)
            .get()
            .build()

        try {
            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    lblInfo.text = "Ha ocurrido un problema con el servidor"
                } else {
                    lblInfo.text = ""

                    val json = JSONTokener(response.body!!.string())
                    val jsonArray = JSONArray(json)

                    var i = 0;
                    while (i< jsonArray.length()) {
                        var categoriaJSON = jsonArray.getJSONObject(i)
                        var categoriaObject = Categoria(
                            categoriaJSON.getInt("Id_categoria"),
                            categoriaJSON.getString("Nombre")
                        )
                        listaCategorias.add(categoriaObject)
                        i++
                    }
                    if (listaCategorias.isEmpty()) {
                        CreateGeneralCat()
                        ShowCats()
                    } else {
                        val arrayAdapter = ArrayAdapter(
                            this,
                            android.R.layout.simple_spinner_item,
                            listaCategorias
                        );

                        spinCat.adapter = arrayAdapter
                    }

                }
            }
        } catch (e: java.net.ConnectException) {
            lblInfo.text = "No se ha podido conectar con el server"
        }
    }
}