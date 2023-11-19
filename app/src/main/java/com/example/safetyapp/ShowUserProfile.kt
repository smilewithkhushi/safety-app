package com.example.safetyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ShowUserProfile : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_user_profile)

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        val usernameTextView: TextView = findViewById(R.id.username)
        val emailTextView: TextView = findViewById(R.id.email)
        val phoneNumberTextView: TextView = findViewById(R.id.phone)
        val genderTextView: TextView = findViewById(R.id.gender)
        val addressTextView: TextView = findViewById(R.id.address)
        val dobTextView: TextView = findViewById(R.id.dob)

        val userId = auth.currentUser?.uid ?:""

        val dbRef = database.reference.child("users")
        val userRef = dbRef.child(userId ?: "")

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userData = dataSnapshot.getValue(User::class.java)
                userData?.let {
                    usernameTextView.text = it.username
                    emailTextView.text = auth.currentUser?.email
                    phoneNumberTextView.text = it.phone
                    genderTextView.text = it.gender
                    dobTextView.text= it.dateofbirth
                    addressTextView.text= it.address



                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //handle DB error
            }
        })

        val editProfileBtn = findViewById<Button>(R.id.editProfile)

        editProfileBtn.setOnClickListener {
            editUserProfile()
        }

    }


    private fun editUserProfile() {
        val intent = Intent(this, EditUserProfile::class.java)
        startActivity(intent)
        finish()
    }
}
