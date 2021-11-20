package com.catfoxtechnology.tumbaiorganizer

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.view.Menu
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.catfoxtechnology.tumbaiorganizer.databinding.ActivityMainBinding
import com.catfoxtechnology.tumbaiorganizer.ui.addtareaact.AddTareaActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    val lblUsername : TextView by lazy { findViewById(R.id.lblShowUsername) }
    val btnCerrarSesion : Button by lazy { findViewById(R.id.btnCerrarSesion) }
    val tokenAPI by lazy { intent.getStringExtra("token") }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        binding.appBarMain.fab.setOnClickListener { view ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //    .setAction("Action", null).show()
            val intent = Intent(this, AddTareaActivity::class.java)
                .putExtra("token",tokenAPI)
            startActivity(intent)
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        lblUsername.text = intent.getStringExtra("username").toString()
        btnCerrarSesion.setOnClickListener {
            val okHttpClient = OkHttpClient()
            val request = Request.Builder()
                .url("http://"+ getString(R.string.server_ip) +"/organizzdorapi/public/api/logout")
                .addHeader("Authorization", "Bearer " + tokenAPI)
                .post("".toRequestBody())
                .build()

            try {
                okHttpClient.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Ha ocurrido un problema al cerrar sesión",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        //lblUsername.text = response.body!!.string()
                        val json = JSONObject(response.body!!.string())
                        Toast.makeText(this, json["message"].toString(), Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
            }catch (e: java.net.ConnectException) {
                Toast.makeText(this, "No se ha podido conectar con el servidor", Toast.LENGTH_LONG).show()
            }
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}