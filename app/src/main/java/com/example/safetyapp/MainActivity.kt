package com.example.safetyapp

import android.R
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.safetyapp.R.layout.activity_main)

        val getStartedButton = findViewById<Button>(com.example.safetyapp.R.id.getStartedBtn)
        getStartedButton.setOnClickListener {

            intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }


}
