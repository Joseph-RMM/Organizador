package com.example.tumbaiorganizer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginActivity : AppCompatActivity() {
    val btnRegistrar : Button by lazy { findViewById(R.id.btnRegistrar) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        btnRegistrar.setOnClickListener {
            val registrarIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registrarIntent)
        }
    }
}