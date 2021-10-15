package com.example.tumbaiorganizer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView

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
        supportActionBar?.hide()

        lblInfo.text=""

        btnRegistrar.setOnClickListener {
            //Obtener datos
            var Username = txtUsuario.text
            var Correo = txtCorreo.text
            var Pass = txtPass.text
            var RepeatPass = txtRepeatPass.text

            //Hacer peticion a la API


        }

    }
}