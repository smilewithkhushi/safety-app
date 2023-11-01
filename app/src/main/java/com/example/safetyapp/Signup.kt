package com.example.safetyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Signup : AppCompatActivity() {

    lateinit var usernameEditText : TextInputEditText
    lateinit var emailEditText : TextInputEditText
    lateinit var passwordEditText: TextInputEditText
    lateinit var confirmEditText: TextInputEditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        usernameEditText = findViewById(R.id.usernameTextView)
        emailEditText = findViewById(R.id.emailTextView)
        passwordEditText = findViewById(R.id.passwordTextView)
        confirmEditText = findViewById(R.id.cnfrmTextView)

        val signupBtn = findViewById<Button>(R.id.signUpButton)
        val loginBtn = findViewById<Button>(R.id.loginButton)
        val firebaseAuth = FirebaseAuth.getInstance()

        signupBtn.setOnClickListener {
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirm = confirmEditText.text.toString()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()){
                Toast.makeText(this, "Fill in the required fields", Toast.LENGTH_SHORT).show()
            }
            else if (confirm != password){
                Toast.makeText(this, "The passwords do not match!", Toast.LENGTH_SHORT).show()
            }
            else if (password.length<6){
                Toast.makeText(this, "The password should be at least 6 characters long!", Toast.LENGTH_SHORT).show()
            }
            else{
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        task ->
                    if (task.isSuccessful){
                        val database = FirebaseDatabase.getInstance()
                        val dbRef = database.reference.child("users")
                        val userId = firebaseAuth.currentUser?.uid ?: ""
                        val data = User(username, email)

                        val userRef = dbRef.child(userId)
                        userRef.setValue(data)

                        Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show()

                    }
                    else{
                        Toast.makeText(this, "Sign up failed! Try again", Toast.LENGTH_SHORT).show()
                    }
                }
            }



        }

        loginBtn.setOnClickListener {
            intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }
    data class User(
        val username: String,
        val email : String
    )
}