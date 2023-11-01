package com.example.safetyapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Login : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    lateinit var inputEmail: TextInputEditText
    lateinit var inputPassword: TextInputEditText
    lateinit var loginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)

        inputEmail = findViewById(R.id.emailTextView)
        inputPassword = findViewById(R.id.passwordTextView)
        loginBtn = findViewById(R.id.loginButton)
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference



        // Check if the user is already logged in
        if (firebaseAuth.currentUser != null) {
            startHomeActivity()
        }

        loginBtn.setOnClickListener {
            val email = inputEmail.text.toString()
            val password = inputPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email or password!", Toast.LENGTH_SHORT).show()
            } else {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            // User authenticated successfully, fetch user data from Firebase Realtime Database
                            fetchUserDataFromDatabase(firebaseAuth.currentUser?.uid ?: "")

                        } else {
                            Toast.makeText(this, "Invalid Email or password!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }

        }

        val signupButton = findViewById<Button>(R.id.signUpBtn)
        signupButton.setOnClickListener {
            intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }
    }


    private fun fetchUserDataFromDatabase(userId: String) {
        // Fetch user data from Firebase Realtime Database using userId
        val userRef = database.child("users").child(userId)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                // Handle the fetched user data here
                if (snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        // User data fetched successfully, start home activity
                        startHomeActivity()
                        finish()

                    } else {
                        // User data is null, handle the error
                        Toast.makeText(this, "User data is null", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    // User data does not exist, handle the error
                    Toast.makeText(this, "User does not exist", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
                Toast.makeText(this, "Database error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun startHomeActivity() {
        val intent = Intent(this, Dashboard::class.java)
        startActivity(intent)
        finish()
    }
}

data class User(
    val userId: String = "",
    val username: String = "",
    val email: String = ""
)

