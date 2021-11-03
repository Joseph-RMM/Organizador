package com.example.tumbaiorganizer.ui.detailscat

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tumbaiorganizer.Model.Categoria
import com.example.tumbaiorganizer.Model.Tarea
import com.example.tumbaiorganizer.R
import com.example.tumbaiorganizer.databinding.DetailsCategoriaFragmentBinding
import com.example.tumbaiorganizer.databinding.DetailsTareaFragmentBinding
import com.example.tumbaiorganizer.databinding.FragmentHomeBinding
import com.example.tumbaiorganizer.ui.home.HomeViewModel
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.text.SimpleDateFormat

class DetailsCategoriaFragment : Fragment() {

    companion object {
        fun newInstance() = DetailsCategoriaFragment()
    }

    private lateinit var viewModel: DetailsCategoriaViewModel
    private var _binding : DetailsCategoriaFragmentBinding? = null
    private val binding get() = _binding!!
    private val tokenAPI by lazy {  activity?.intent?.getStringExtra("token")}
    private var IDCategoria = 0;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(DetailsCategoriaViewModel::class.java)

        _binding = DetailsCategoriaFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.btnEditarCat.setOnClickListener {
            //Actualizar ViewModel
            viewModel.categoria.Nombre = binding.txtNombreCat.text.toString()


            //Enviar al API
            val okHttpClient = OkHttpClient()
            val formBody = FormBody.Builder()
                .add("Nombre",viewModel.categoria.Nombre)
                .build()
            val request = Request.Builder()
                .url("http://"+ getString(R.string.server_ip) +"/organizzdorapi/public/api/categories/$IDCategoria")
                .addHeader("Authorization", "Bearer " + tokenAPI)
                .put(formBody)
                .build()

            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Toast.makeText(binding.root.context,"Ha ocurrido un problema al guardar la información",Toast.LENGTH_LONG).show()
                } else {
                    //lblUsername.text = response.body!!.string()
                    Log.d("JSONResponse",response.body!!.string())
                    Toast.makeText(binding.root.context,"Categoría actualizada",Toast.LENGTH_SHORT).show()

                }
            }
        }

        binding.btnEliminarCat.setOnClickListener {
            val okHttpClient = OkHttpClient()
            val request = Request.Builder()
                .url("http://"+ getString(R.string.server_ip) +"/organizzdorapi/public/api/categories/$IDCategoria")
                .addHeader("Authorization", "Bearer " + tokenAPI)
                .delete()
                .build()

            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Toast.makeText(binding.root.context,"Ha ocurrido un problema con el servidor",Toast.LENGTH_SHORT).show()
                } else {
                    if (response.body!!.string() == "1") {
                        Toast.makeText(binding.root.context,"La categoría ha sido eliminada",Toast.LENGTH_SHORT).show()
                        getActivity()?.supportFragmentManager?.popBackStack()
                    } else {
                        Toast.makeText(binding.root.context,"La categoría ya no existe",Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }


        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailsCategoriaViewModel::class.java)
        // TODO: Use the ViewModel

        val bundle = this.arguments
        if (bundle != null) {
            IDCategoria = bundle.getInt("ID")
            Toast.makeText(binding.root.context, "ID: $IDCategoria", Toast.LENGTH_SHORT).show();
            //Get tareas
            val okHttpClient = OkHttpClient()
            val request = Request.Builder()
                .url("http://"+ getString(R.string.server_ip) +"/organizzdorapi/public/api/categories/$IDCategoria")
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
                    binding.txtNombreCat.setText(viewModel.categoria.Nombre)
                }
            }
        } else {
            Toast.makeText(binding.root.context,"Ha ocurrido un error en la app al pasar la información", Toast.LENGTH_SHORT).show();
            Log.d("Bundle","null")
        }
    }

}