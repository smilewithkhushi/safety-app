package com.example.safetyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserProfile : AppCompatActivity() {

    lateinit var usernameEditText: TextInputEditText
    lateinit var genderEditText: TextInputEditText
    lateinit var memberEditText: TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)



        val editProfileBtn = findViewById<Button>(R.id.editProfile)
        val saveProfileBtn = findViewById<Button>(R.id.saveProfile)
        val firebaseAuth = FirebaseAuth.getInstance()

        saveProfileBtn.setOnClickListener {
            val username = usernameEditText.text.toString()
            val gender = genderEditText.text.toString()
            val family = memberEditText.text.toString()

            if (username.isEmpty()){
                Toast.makeText(this, "Fill in the required fields", Toast.LENGTH_SHORT).show()
            } else {
                            val database = FirebaseDatabase.getInstance()
                            val dbRef = database.reference.child("users")
                            val userId = firebaseAuth.currentUser?.uid ?: ""
                            val data = UserProfile.Profile(username, gender, family)

                            val userRef = dbRef.child(userId)
                            userRef.setValue(data)

                            Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show()

                        }
                    }
            }

    data class Profile(
        val username: String,
        val gender : String,
        val member : String
    )
}
