package com.example.tumbaiorganizer.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tumbaiorganizer.Model.Tarea
import com.example.tumbaiorganizer.R
import com.example.tumbaiorganizer.databinding.FragmentHomeBinding
import com.example.tumbaiorganizer.ui.detailstarea.DetailsTareaFragment
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val tokenAPI by lazy {  activity?.intent?.getStringExtra("token")}
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        /*
        val tvCantTareas: TextView = binding.lblCantTareas
        homeViewModel.addTarea("Prueba Kotlin", SimpleDateFormat("dd-MM-yyyy").parse("28-09-2021") )
        homeViewModel.addTarea("Actividad de inglés", SimpleDateFormat("dd-MM-yyyy").parse("01-10-2021") )
        tvCantTareas.setText(homeViewModel.listaTareas.size.toString());
        if (homeViewModel.listaTareas.size > 0) {
            //Añadir tareas a la lista
            val lvTareas : ListView = binding.lvTareas
            val arrayAdapter : ArrayAdapter<*>
            arrayAdapter = ArrayAdapter(binding.root.context, android.R.layout.simple_list_item_1,homeViewModel.listaTareas);
            lvTareas.adapter = arrayAdapter
            
            lvTareas.setOnItemClickListener { parent, view, position, index ->
                val selectedTarea : Tarea = parent.getItemAtPosition(position) as Tarea
                Toast.makeText(binding.root.context,selectedTarea.Actividad,Toast.LENGTH_SHORT).show()

                val fragmentDetails = DetailsTareaFragment()
                val manager = requireActivity().supportFragmentManager.beginTransaction()
                manager.add(R.id.nav_host_fragment_content_main,fragmentDetails)
                manager.addToBackStack(null)
                manager.commit()

            }
        }*/


        showTareas()
        binding.btnRefresh.setOnClickListener {
            showTareas()
        }



        /*homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showTareas() {
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url("http://"+ getString(R.string.server_ip) +"/organizzdorapi/public/api/task")
            .addHeader("Authorization", "Bearer " + tokenAPI)
            .get()
            .build()

        try {
            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    binding.lblTareasListJSON.text = "Ha ocurrido un problema con el servidor"
                } else {
                    binding.lblTareasListJSON.text = ""
                    homeViewModel.clearList()
                    //binding.lblTareasListJSON.text = response.body!!.string()
                    val json = JSONTokener(response.body!!.string())
                    val jsonArray = JSONArray(json)
                    //val array = json.getJSONArray("")
                    binding.lblCantTareas.text = jsonArray.length().toString()
                    //val json = JSONObject(response.body!!.string())
                    //Toast.makeText(this,json["message"].toString(),Toast.LENGTH_LONG).show()
                    var i = 0;
                    while (i < jsonArray.length()) {
                        var tareaJSON = jsonArray.getJSONObject(i)
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

                        homeViewModel.addTarea(tareaObject)
                        i++

                    }

                    //if (homeViewModel.listaTareas.size == 0) {
                    //Añadir tareas a la lista
                    val lvTareas: ListView = binding.lvTareas
                    val arrayAdapter: ArrayAdapter<*>
                    arrayAdapter = ArrayAdapter(
                        binding.root.context,
                        android.R.layout.simple_list_item_1,
                        homeViewModel.listaTareas
                    );
                    lvTareas.adapter = arrayAdapter

                    lvTareas.setOnItemClickListener { parent, view, position, index ->
                        val selectedTarea: Tarea = parent.getItemAtPosition(position) as Tarea
                        //Toast.makeText(binding.root.context,selectedTarea.Actividad,Toast.LENGTH_SHORT).show()

                        val fragmentDetails = DetailsTareaFragment()
                        var bundle: Bundle = Bundle();
                        bundle.putInt("ID", selectedTarea.ID);
                        fragmentDetails.arguments = bundle;
                        val manager = parentFragmentManager.beginTransaction()
                        manager.replace(R.id.nav_host_fragment_content_main, fragmentDetails)
                        manager.addToBackStack(null)
                        manager.commit()

                    }
                    //}
                }
            }
        } catch (e: java.net.ConnectException) {
            binding.lblTareasListJSON.text = "No se ha podido conectar con el server"
        }
    }
}