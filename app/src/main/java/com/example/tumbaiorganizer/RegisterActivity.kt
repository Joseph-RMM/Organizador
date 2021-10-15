package com.example.tumbaiorganizer

import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException


class RegisterActivity : AppCompatActivity() {
    val txtUsuario : EditText by lazy { findViewById(R.id.txtUsuarioRegister) }
    val txtCorreo : EditText by lazy { findViewById(R.id.txtCorreoRegister) }
    val txtPass : EditText by lazy { findViewById(R.id.txtPassRegister) }
    val txtRepeatPass : EditText by lazy { findViewById(R.id.txtRepeatPassRegister) }
    val lblInfo : TextView by lazy { findViewById(R.id.lblInfoRegister) }
    val btnRegistrar : TextView by lazy { findViewById(R.id.btnRegistrarPage) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        if (Build.VERSION.SDK_INT > 9) {
            val policy = ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        supportActionBar?.hide()

        lblInfo.text=""

        btnRegistrar.setOnClickListener {
            //Obtener datos
            var Username = txtUsuario.text.toString()
            var Correo = txtCorreo.text.toString()
            var Pass = txtPass.text.toString()
            var RepeatPass = txtRepeatPass.text.toString()

            //Hacer peticion a la API
            val okHttpClient = OkHttpClient()
            //val mediaType = "application/json; charset=utf-8".toMediaType()
            //val requestBody = "".toRequestBody()

            /*
            val formBody = FormBody.Builder()
                .add("name",Username)
                .add("email",Correo)
                .add("password",Pass)
                .add("password_confirmation",Pass)
                .build()

            val request = Request.Builder()
                .url("http://127.0.0.1:8000/api/register")
                .post(formBody)
                .build()

            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful){
                    lblInfo.text ="Ha habido un problema con el servidor :c"
                } else {
                    lblInfo.text = response.body!!.string()

                    val json = JSONObject(response.body!!.string())
                    lblInfo.text = json["id"].toString()
                }
            }*/

            val client = OkHttpClient()


            val formBody = FormBody.Builder()
                .add("name", Username)
                .add("job",Correo)
                .build()
            val request = Request.Builder()
                .url("https://reqres.in/api/users")
                .post(formBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                //lblInfo.text = response.body!!.string()
                val json = JSONObject(response.body!!.string())
                lblInfo.text = json["id"].toString()
            }


        }

    }
}