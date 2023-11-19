package com.example.safetyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class EditUserProfile : AppCompatActivity() {

    lateinit var usernameEditText: TextInputEditText
    lateinit var genderEditText: TextInputEditText
    lateinit var phoneEditText: TextInputEditText

    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user_profile)

        val usernameEditText = findViewById<EditText>(R.id.username)
        val genderEditText = findViewById<EditText>(R.id.gender)
        val phoneEditText = findViewById<EditText>(R.id.phone)
        val dobEditText = findViewById<EditText>(R.id.dob)
        val addressEditText = findViewById<EditText>(R.id.address)

        val saveProfileBtn = findViewById<Button>(com.example.safetyapp.R.id.saveProfile)
        val viewProfileBtn = findViewById<Button>(com.example.safetyapp.R.id.viewProfile)
        val firebaseAuth = FirebaseAuth.getInstance()

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()


        saveProfileBtn.setOnClickListener {
            val userId = auth.currentUser?.uid
            val username = usernameEditText.text.toString().trim()
            val dob = dobEditText.text.toString().trim()
            val gender = genderEditText.text.toString().trim()
            val address = addressEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()

            if (userId != null && userId.isNotEmpty()) {
                val dbRef = database.reference.child("users")
                val userRef = dbRef.child(userId ?: "")

                val userData = HashMap<String, Any>()
                userData["username"] = username
                userData["gender"] = gender
                userData["phone"] = phone
                userData["dateofbirth"] = dob
                userData["address"] = address

                userRef.setValue(userData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Profile Updated Successfully!", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error saving the profile, try again!", Toast.LENGTH_SHORT)
                            .show()

                    }
            }
        }

        viewProfileBtn.setOnClickListener {
            viewUserProfile()
        }

    }


    private fun viewUserProfile() {
        val intent = Intent(this, ShowUserProfile::class.java)
        startActivity(intent)
        finish()
    }
}