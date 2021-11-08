package com.example.tumbaiorganizer.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tumbaiorganizer.Model.Categoria
import com.example.tumbaiorganizer.R
import com.example.tumbaiorganizer.databinding.FragmentGalleryBinding
import com.example.tumbaiorganizer.ui.detailscat.DetailsCategoriaFragment
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONTokener

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private var _binding: FragmentGalleryBinding? = null
    private val tokenAPI by lazy {  activity?.intent?.getStringExtra("token")}
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        showCategorias()

        binding.btnAddCategoria.setOnClickListener {
            val okHttpClient = OkHttpClient()

            val formBody = FormBody.Builder()
                .add("Nombre",binding.txtNewCatName.text.toString())
                .build()

            val request = Request.Builder()
                .url("http://"+ getString(R.string.server_ip) +"/organizzdorapi/public/api/categories")
                .addHeader("Authorization", "Bearer " + tokenAPI)
                .post(formBody)
                .build()

            try {
                okHttpClient.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        binding.lblInfo.setText("Datos incorrectos o cuenta inexistente")
                    } else {
                        Log.d("API Response:", response.body!!.string())
                        showCategorias()
                        binding.txtNewCatName.setText("")
                    }
                }
            } catch (e: java.net.ConnectException) {
                binding.lblInfo.setText("No se ha podido conectar con el server")
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showCategorias() {
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url("http://"+ getString(R.string.server_ip) +"/organizzdorapi/public/api/categories")
            .addHeader("Authorization", "Bearer " + tokenAPI)
            .get()
            .build()

        try {
            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    binding.lblInfo.text = "Ha ocurrido un problema con el servidor"
                } else {
                    binding.lblInfo.text = ""
                    galleryViewModel.clearList()

                    val json = JSONTokener(response.body!!.string())
                    val jsonArray = JSONArray(json)

                    var i = 0;
                    while (i< jsonArray.length()) {
                        var categoriaJSON = jsonArray.getJSONObject(i)
                        var categoriaObject = Categoria(
                            categoriaJSON.getInt("Id_categoria"),
                            categoriaJSON.getString("Nombre")
                        )
                        galleryViewModel.addCategoria(categoriaObject)
                        i++
                    }

                    val arrayAdapter: ArrayAdapter<*>
                    arrayAdapter = ArrayAdapter(
                        binding.root.context,
                        android.R.layout.simple_list_item_1,
                        galleryViewModel.listaCategorias
                    );

                    binding.lvCategorias.adapter = arrayAdapter

                    binding.lvCategorias.setOnItemClickListener { parent, view, position, index ->
                        val selectedCategoria : Categoria = parent.getItemAtPosition(position) as Categoria
                        //Toast.makeText(binding.root.context,selectedTarea.Actividad,Toast.LENGTH_SHORT).show()

                        val fragmentDetails = DetailsCategoriaFragment()
                        var bundle: Bundle = Bundle();
                        bundle.putInt("ID", selectedCategoria.Id_categoria);
                        fragmentDetails.arguments = bundle;


                        val manager = parentFragmentManager.beginTransaction()
                        manager.replace(R.id.nav_host_fragment_content_main, fragmentDetails)
                        manager.addToBackStack(null)
                        manager.commit()

                    }
                   //  binding.lblInfo.text = response.body!!.string()
                   /* val json = JSONTokener(response.body!!.string())
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

                    //if (homeViewModel.listaTareas.size > 0) {
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
                        val manager = requireActivity().supportFragmentManager.beginTransaction()
                        manager.add(R.id.nav_host_fragment_content_main, fragmentDetails)
                        manager.addToBackStack(null)
                        manager.commit()

                    }
                    //}*/
                }
            }
        } catch (e: java.net.ConnectException) {
            binding.lblInfo.text = "No se ha podido conectar con el server"
        }
    }
}