package com.catfoxtechnology.tumbaiorganizer.ui.slideshow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.catfoxtechnology.tumbaiorganizer.R
import com.catfoxtechnology.tumbaiorganizer.databinding.FragmentSlideshowBinding
import okhttp3.OkHttpClient
import okhttp3.Request

class SlideshowFragment : Fragment() {

    private lateinit var slideshowViewModel: SlideshowViewModel
    private var _binding: FragmentSlideshowBinding? = null

    private val tokenAPI by lazy {  activity?.intent?.getStringExtra("token")}
    private val userName by lazy {  activity?.intent?.getStringExtra("username")}
    private val email by lazy {  activity?.intent?.getStringExtra("email")}
    private val userID by lazy {  activity?.intent?.getStringExtra("id")}
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root
/*
        val textView: TextView = binding.textSlideshow
        slideshowViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/

        binding.lblUsernameConfig.setText(userName)
        binding.lblCorreoConfig.setText(email)
        binding.btnEliminarCuenta.setOnClickListener {
            Log.d("API","entered")
            if(binding.txtEliminarCuenta.text.toString().lowercase() == "eliminar cuenta") {
                try {
                    val okHttpClient = OkHttpClient()
                    val request = Request.Builder()
                        .url("http://" + getString(R.string.server_ip) + "/organizzdorapi/public/api/user/$userID")
                        .addHeader("Authorization", "Bearer " + tokenAPI)
                        .delete()
                        .build()

                    okHttpClient.newCall(request).execute().use { response ->
                        if (!response.isSuccessful) {
                            Toast.makeText(
                                binding.root.context, "Ha ocurrido un problema con el servidor",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            if (response.body!!.string() == "1") {
                                Toast.makeText(
                                    binding.root.context,
                                    "La cuenta ha sido eliminada",
                                    Toast.LENGTH_SHORT
                                ).show()
                                activity?.finish()
                            } else {
                                Toast.makeText(
                                    binding.root.context,
                                    "La cuenta ya no existe",
                                    Toast.LENGTH_SHORT
                                ).show()
                                activity?.finish()
                            }

                        }
                    }
                } catch (e:Exception) {
                    binding.lblInfoConfig.setText("No se ha podido conectar con el servidor")
                }
            } else {
                binding.lblInfoConfig.setText("Escriba 'Eliminar cuenta'")
            }
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}