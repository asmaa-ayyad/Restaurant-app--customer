package com.example.andro2groupprojecttest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.andro2groupprojecttest.databinding.ActivityLoginBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class loginActivity : AppCompatActivity() {

    var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val email = binding.username.text.toString()
            val pass = binding.password.text.toString()


            searchUserAndNavigateToMain(email,pass)

        }

        binding.signupText.setOnClickListener {

            val i = Intent(this, signupActivity::class.java)
            startActivity(i)


        }


    }


   // val usersCollection = db.collection("users")
    //  usersCollection.whereEqualTo("username", username)

    fun searchUserAndNavigateToMain(username: String, password: String) {
        db.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    for (document in querySnapshot.documents) {
                        val documentUsername = document.getString("username")
                        val documentPassword = document.getString("password")

                        if (documentUsername == username && documentPassword == password) {
                            val userId=document.id
                            //Toast.makeText(this,"true pass", Toast.LENGTH_LONG).show()
                            val sharedPref = getSharedPreferences("login", MODE_PRIVATE)
                            val editor = sharedPref.edit()
                            editor.putString("username", username)
                            editor.putString("password", password)
                            editor.putString("userid",userId)
                            editor.apply()

                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                            return@addOnSuccessListener
                        }
                    }
                }
                Toast.makeText(this,"incorrect username or password ", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
            }
    }
}


