package com.example.safetyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class Dashboard : AppCompatActivity() {

    private lateinit var bottomNavigationView : BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener {
            menuItem ->
            when(menuItem.itemId){
                R.id.bottom_home -> {
                    replaceFragment(HomeFragment())
                    true
                }

                R.id.bottom_sms -> {
//                    intent = Intent(this, Login::smsActivity.java)
//                    startActivity(intent)
//                    finish()
                    true
                }

                R.id.bottom_call -> {
//                    intent = Intent(this, Login::class.java)
//                    startActivity(intent)
//                    finish()
                    true
                }

                R.id.bottom_analytics -> {
//                    intent = Intent(this, Login::class.java)
//                    startActivity(intent)
//                    finish()
                    true
                }

                R.id.bottom_profile -> {
                    intent = Intent(this, UserProfile::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false

        }
        }
        replaceFragment(HomeFragment())

    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }

}