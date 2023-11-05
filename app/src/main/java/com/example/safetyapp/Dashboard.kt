package com.example.safetyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

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
                    replaceFragment(SMSFragment())
                    true
                }

                R.id.bottom_call -> {
                    replaceFragment(callFragment())
                    true
                }

                R.id.bottom_analytics -> {
                    replaceFragment(analyticsFragment())
                    true
                }

                R.id.bottom_profile -> {
                    replaceFragment(profileFragment())
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