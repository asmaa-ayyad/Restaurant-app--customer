package com.example.andro2groupprojecttest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.andro2groupprojecttest.databinding.ActivityUserMealRateBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.properties.Delegates

class UserMealRate : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    lateinit var mealid:String
    lateinit var userid:String
    var averageRate by Delegates.notNull<Double>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityUserMealRateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var rateValue: Float
        var temp: String

        db = Firebase.firestore
        averageRate=0.0

        mealid = intent.getStringExtra("mealid").toString()
        val sharedPref = getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
        userid = sharedPref.getString("userid", "noo user").toString()


        binding.ratingBar2.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->

            rateValue = binding.ratingBar2.rating
            if (rateValue <= 1 && rateValue > 0) {
                binding.rateCount.setText("Bad" + rateValue + "/5")
            } else if (rateValue <= 2 && rateValue < 1) {
                binding.rateCount.setText("ok" + rateValue + "/5")

            } else if (rateValue <= 3 && rateValue < 2) {
                binding.rateCount.setText("Good" + rateValue + "/5")

            } else if (rateValue <= 4 && rateValue < 3) {
                binding.rateCount.setText("Very Good" + rateValue + "/5")

            } else if (rateValue <= 5 && rateValue < 4) {
                binding.rateCount.setText("Best" + rateValue + "/5")

            }


            binding.submit.setOnClickListener {
                userRate(userid, mealid, binding.ratingBar2.rating, binding.review.text.toString())
                calculateAverageRateForRestaurant(mealid)

                temp = binding.rateCount.text.toString()
                binding.showRating.setText("yourRating:\n " + temp + "\n" + binding.review.text)
                binding.review.setText("")
                binding.ratingBar2.rating = 0f
                binding.rateCount.setText("")
            }

        }
    }

    fun userRate(userid: String, mealid: String, rate: Float, review: String) {
        val userRateQuery = db.collection("mealRate")
            .whereEqualTo("userid", userid)
            .whereEqualTo("mealid", mealid)

        userRateQuery.get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    val userRate = hashMapOf("userid" to userid, "mealid" to mealid, "rate" to rate, "review" to review)
                    db.collection("mealRate")
                        .add(userRate)
                        .addOnSuccessListener { documentReference ->
                            Log.e("saja", "userRate added")
                        }
                        .addOnFailureListener { e ->
                            Log.e("saja", "Failed to add userRate: ${e.message}")
                        }
                } else {
                    Toast.makeText(this," you are already rated for this meal", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e("saja", "Failed to check user rate: ${e.message}")
            }
    }

    /*fun userRate(userid: String, mealid: String, rate: Float, review: String) {
        val userRate = hashMapOf("userid" to userid, "mealid" to mealid, "rate" to rate, "review" to review)
        db.collection("mealRate")
            .add(userRate)
            .addOnSuccessListener { documentReference ->
                Log.e("saja", "userRateadded")

            }
            .addOnFailureListener { e ->
                Log.e("saja", "fail")

            }

    }*/


    private fun calculateAverageRateForRestaurant(mealid: String) {
       /*
        val firestore = FirebaseFirestore.getInstance()
        val collectionRef = firestore.collection("mealRate")
        collectionRef.whereEqualTo("mealid", mealid)*/
        db.collection("mealRate")
            .whereEqualTo("mealid", mealid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val documents = querySnapshot.documents

                if (documents.isNotEmpty()) {
                    var totalRate = 0.0
                    var userCount = 0

                    for (document in documents) {
                        val data = document.data
                        val rate = data?.get("rate") as? Double ?: 0.0
                        totalRate += rate
                        userCount++
                    }
                    averageRate = totalRate / userCount
                    updateResRate()
                } else {
                }


            }

            .addOnFailureListener { exception ->
                Log.e("saja", exception.message!!)
            }
    }


    private fun updateResRate() {
        val updateRate = hashMapOf<String,Any>(
            "rate" to averageRate
        )
        db.collection("meals").document(mealid)
            .update(updateRate).addOnSuccessListener {
                Log.e("saja", "update rate success")

            }.addOnFailureListener{
                Log.e("saja", it.message.toString())


            }




    }

    }



