package com.example.andro2groupprojecttest

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.andro2groupprojecttest.databinding.ActivitySignupBinding
import com.example.andro2groupprojecttest.ui.Modle.user
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class signupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = Firebase.firestore
        val sharedPref=  this.getSharedPreferences("login", Context.MODE_PRIVATE)

        binding.sigupButton.setOnClickListener {
            val username = binding.username.text.toString()
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()
            val user = com.example.andro2groupprojecttest.ui.Modle.user(
                "",
                username,
                email,
                pass,
                "EnterYour Address"
            )

            db.collection("users")
                .add(user)
                .addOnSuccessListener { documentReference ->
                    val editor = sharedPref!!.edit()
                    editor.putString("username",username)
                    editor.putString("password",pass)
                    editor.apply()
                    val i = Intent(this, loginActivity::class.java)
                    startActivity(i)

                }
                .addOnFailureListener { e ->


                }


        }


    }
}