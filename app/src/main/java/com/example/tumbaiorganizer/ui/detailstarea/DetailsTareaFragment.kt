package com.example.tumbaiorganizer.ui.detailstarea

import android.R.attr.key
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tumbaiorganizer.MainActivity
import com.example.tumbaiorganizer.Model.Tarea
import com.example.tumbaiorganizer.R
import com.example.tumbaiorganizer.databinding.DetailsTareaFragmentBinding
import com.example.tumbaiorganizer.databinding.FragmentHomeBinding
import com.example.tumbaiorganizer.ui.home.HomeViewModel
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class DetailsTareaFragment : Fragment() {

    companion object {
        fun newInstance() = DetailsTareaFragment()
    }

    private lateinit var viewModel: DetailsTareaViewModel
    private var IDTarea = 0;

    private val tokenAPI by lazy {  activity?.intent?.getStringExtra("token")}

    private var _binding: DetailsTareaFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =  ViewModelProvider(this).get(DetailsTareaViewModel::class.java)

        _binding = DetailsTareaFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnBorrar.setOnClickListener {
            val okHttpClient = OkHttpClient()
            val request = Request.Builder()
                .url("http://20.114.118.119/organizzdorapi/public/api/task/$IDTarea")
                .addHeader("Authorization", "Bearer " + tokenAPI)
                .delete()
                .build()

            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Toast.makeText(binding.root.context,"Ha ocurrido un problema con el servidor",Toast.LENGTH_SHORT).show()
                } else {
                    if (response.body!!.string() == "1") {
                        Toast.makeText(binding.root.context,"La tarea ha sido eliminada",Toast.LENGTH_SHORT).show()
                        getActivity()?.supportFragmentManager?.popBackStack()
                    } else {
                        Toast.makeText(binding.root.context,"La tarea ya no existe",Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }

        return root;

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailsTareaViewModel::class.java)
        // TODO: Use the ViewModel
        val bundle = this.arguments
        if (bundle != null) {
            IDTarea = bundle.getInt("ID")
            Toast.makeText(binding.root.context, "ID: $IDTarea",Toast.LENGTH_SHORT);
            //Get tareas
            val okHttpClient = OkHttpClient()
            val request = Request.Builder()
                .url("http://20.114.118.119/organizzdorapi/public/api/task/$IDTarea")
                .addHeader("Authorization", "Bearer " + tokenAPI)
                .get()
                .build()

            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Toast.makeText(binding.root.context,"Ha ocurrido un problema con el servidor",Toast.LENGTH_SHORT).show()
                } else {
                    val tareaJSON = JSONObject(response.body!!.string())
                    //Log.d("JSONResponse",response.body!!.string())
                    val sdf = SimpleDateFormat("yyyy-MM-dd")
                    var tareaObject = Tarea(
                        tareaJSON.getString("Id_tareas").toInt(),
                        tareaJSON.getString("Nombre_Tarea"),
                        tareaJSON.getString("Descripcion"),
                        sdf.parse(tareaJSON.getString("Fecha_Finalizacion")),
                        tareaJSON.getInt("estado"),
                        tareaJSON.getInt("prioridad"),
                        tareaJSON.getInt("Id_categoria")
                    )

                    binding.txtEditActividad.setText(tareaObject.Actividad)
                    binding.txtEditDescripcion.setText(tareaObject.Descripcion)
                    binding.cvEditFechaEntrega.date = sdf.parse(tareaObject.getAPIDateFormat()).time

                }
            }
        } else {
            Toast.makeText(binding.root.context,"Ha ocurrido un error en la app al pasar la información",Toast.LENGTH_SHORT).show();
            Log.d("Bundle","null")
        }
    }

}