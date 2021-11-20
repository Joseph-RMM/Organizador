package com.catfoxtechnology.tumbaiorganizer.ui.detailstarea

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.catfoxtechnology.tumbaiorganizer.Model.Categoria
import com.catfoxtechnology.tumbaiorganizer.Model.Tarea
import com.catfoxtechnology.tumbaiorganizer.R
import com.catfoxtechnology.tumbaiorganizer.databinding.DetailsTareaFragmentBinding
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class DetailsTareaFragment : Fragment() {

    companion object {
        fun newInstance() = DetailsTareaFragment()
    }

    private lateinit var viewModel: DetailsTareaViewModel
    private var IDTarea = 0;
    private lateinit var selectedDate : String

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


        binding.cvEditFechaEntrega.setOnDateChangeListener(CalendarView.OnDateChangeListener { view, year, month, day ->
            selectedDate = "$year-${month + 1}-$day";
        })

        binding.btnBorrar.setOnClickListener {
            val okHttpClient = OkHttpClient()
            val request = Request.Builder()
                .url("http://"+ getString(R.string.server_ip) +"/organizzdorapi/public/api/task/$IDTarea")
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

        binding.btnEditTarea.setOnClickListener {
            //Actualizar ViewModel
            viewModel.tarea.Actividad = binding.txtEditActividad.text.toString()
            viewModel.tarea.Descripcion = binding.txtEditDescripcion.text.toString()
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            viewModel.tarea.Fecha = sdf.parse(selectedDate)


            //Enviar al API
            val okHttpClient = OkHttpClient()
            val formBody = FormBody.Builder()
                .add("Nombre_Tarea",viewModel.tarea.Actividad)
                .add("Descripcion",viewModel.tarea.Descripcion)
                .add("Fecha_Finalizacion",viewModel.tarea.getAPIDateFormat())
                .add("estado",viewModel.tarea.Estatus.toString())
                .add("prioridad",viewModel.tarea.Prioridad.toString())
                .add("Id_categoria",viewModel.tarea.Categoria.toString())
                .build()
            val request = Request.Builder()
                .url("http://"+ getString(R.string.server_ip) +"/organizzdorapi/public/api/task/$IDTarea")
                .addHeader("Authorization", "Bearer " + tokenAPI)
                .put(formBody)
                .build()

            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Toast.makeText(binding.root.context,"Ha ocurrido un problema al guardar la información",Toast.LENGTH_LONG).show()
                } else {
                    //lblUsername.text = response.body!!.string()
                    Log.d("JSONResponse",response.body!!.string())
                    Toast.makeText(binding.root.context,"Tarea actualizada",Toast.LENGTH_SHORT).show()

                }
            }
        }

        return root;

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailsTareaViewModel::class.java)


        val bundle = this.arguments
        if (bundle != null) {
            IDTarea = bundle.getInt("ID")
            Toast.makeText(binding.root.context, "ID: $IDTarea",Toast.LENGTH_SHORT);
            //Get tareas
            val okHttpClient = OkHttpClient()
            val request = Request.Builder()
                .url("http://"+ getString(R.string.server_ip) +"/organizzdorapi/public/api/task/$IDTarea")
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
                    viewModel.tarea = Tarea(
                        tareaJSON.getString("Id_tareas").toInt(),
                        tareaJSON.getString("Nombre_Tarea"),
                        tareaJSON.getString("Descripcion"),
                        sdf.parse(tareaJSON.getString("Fecha_Finalizacion")),
                        tareaJSON.getInt("estado"),
                        tareaJSON.getInt("prioridad"),
                        tareaJSON.getInt("Id_categoria")
                    )

                    binding.txtEditActividad.setText(viewModel.tarea.Actividad)
                    binding.txtEditDescripcion.setText(viewModel.tarea.Descripcion)
                    binding.cvEditFechaEntrega.date = sdf.parse(viewModel.tarea.getAPIDateFormat()).time
                    selectedDate = viewModel.tarea.getAPIDateFormat()

                }
            }
        } else {
            Toast.makeText(binding.root.context,"Ha ocurrido un error en la app al pasar la información",Toast.LENGTH_SHORT).show();
            Log.d("Bundle","null")
        }

        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url("http://"+ getString(R.string.server_ip) +"/organizzdorapi/public/api/categories/${viewModel.tarea.Categoria}")
            .addHeader("Authorization", "Bearer " + tokenAPI)
            .get()
            .build()

        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                Toast.makeText(binding.root.context,"Ha ocurrido un problema con la petición", Toast.LENGTH_SHORT).show()
            } else {
                val categoriaJSON = JSONObject(response.body!!.string())
                viewModel.categoria = Categoria(
                    categoriaJSON.getInt("Id_categoria"),
                    categoriaJSON.getString("Nombre")
                )
                binding.lblShowCat.setText("${viewModel.categoria.Nombre}")
            }
        }

    }

}