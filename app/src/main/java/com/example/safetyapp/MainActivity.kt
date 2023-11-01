package com.example.safetyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val getStartedButton = findViewById<Button>(R.id.getStartedBtn)
        getStartedButton.setOnClickListener {

            intent = Intent (this, Login::class.java)
            startActivity(intent)
        }

    }
}