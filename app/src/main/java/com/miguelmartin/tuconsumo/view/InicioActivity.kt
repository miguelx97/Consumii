package com.miguelmartin.tuconsumo.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.miguelmartin.tuconsumo.R

class InicioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
