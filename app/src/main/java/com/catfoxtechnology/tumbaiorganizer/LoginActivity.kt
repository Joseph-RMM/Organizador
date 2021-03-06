package com.catfoxtechnology.tumbaiorganizer

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    val txtCorreo : EditText by lazy { findViewById(R.id.txtCorreoLogin) }
    val txtPass : EditText by lazy { findViewById(R.id.txtPassLogin) }
    val lblInfo : TextView by lazy { findViewById(R.id.lblInfoLogin) }
    val btnLogin : Button by lazy { findViewById(R.id.btnLogin) }
    val btnRegistrar : Button by lazy { findViewById(R.id.btnRegistrar) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        supportActionBar?.hide()
        lblInfo.text=""

        btnLogin.setOnClickListener {
            var Correo = txtCorreo.text.toString()
            var Pass = txtPass.text.toString()

            if (Correo != "" && Pass != "") {
                //Hacer peticion a la API
                val okHttpClient = OkHttpClient()

                val formBody = FormBody.Builder()
                    .add("email",Correo)
                    .add("password",Pass)
                    .build()

                val request = Request.Builder()
                    .url("http://"+ getString(R.string.server_ip) +"/organizzdorapi/public/api/login")
                    .post(formBody)
                    .build()

                try {
                    okHttpClient.newCall(request).execute().use { response ->
                        if (!response.isSuccessful) {
                            lblInfo.text = "Datos incorrectos o cuenta inexistente"
                        } else {
                            //lblInfo.text = response.body!!.string()
                            try {
                                val json = JSONObject(response.body!!.string())
                                //lblInfo.text = json["token"].toString()
                                //Toast.makeText(this,json["token"].toString(),Toast.LENGTH_LONG).show()
                                val userJson = json.getJSONObject("user");
                                //Log.d("Username",userJson["name"].toString())
                                lblInfo.text = ""
                                val mainIntent = Intent(this, MainActivity::class.java)
                                    .putExtra("token", json["token"].toString())
                                    .putExtra("username", userJson["name"].toString())
                                    .putExtra("email", userJson["email"].toString())
                                    .putExtra("id", userJson.getInt("id").toString())
                                startActivity(mainIntent)
                            } catch (e: Exception) {
                                lblInfo.text = "Datos incorrectos"
                            }

                        }
                    }
                } catch (e: Exception) {
                    lblInfo.text = "No se ha podido conectar con el servidor"
                }
            } else {
                lblInfo.text = "No deje campos vac??os"
            }
        }

        btnRegistrar.setOnClickListener {
            val registrarIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registrarIntent)
        }
    }
}