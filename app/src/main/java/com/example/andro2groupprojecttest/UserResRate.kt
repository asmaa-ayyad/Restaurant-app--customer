package com.example.andro2groupprojecttest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.andro2groupprojecttest.databinding.ActivityUserResRateBinding
import com.example.andro2groupprojecttest.databinding.FragmentResturantBinding
import com.example.andro2groupprojecttest.ui.Modle.user
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.properties.Delegates

class UserResRate : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    lateinit var resid:String
    lateinit var userid:String
    var averageRate by Delegates.notNull<Double>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       val binding = ActivityUserResRateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var rateValue:Float
      //  var temp:String

        db= Firebase.firestore
        averageRate=0.0
        resid = intent.getStringExtra("resid").toString()
        val sharedPref = getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
        userid = sharedPref.getString("userid", "noo user").toString()


        binding.ratingBar2.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            rateValue=binding.ratingBar2.rating
            if(rateValue<=1&&rateValue>0) {
                binding.rateCount.setText("Bad" + rateValue + "/5")
            }
            else if(rateValue<=2&&rateValue >1){
                binding.rateCount.setText("ok" + rateValue + "/5")

            }  else if(rateValue<=3&&rateValue >2){
                binding.rateCount.setText("Good" + rateValue + "/5")

            }  else if(rateValue<=4&&rateValue >3){
                binding.rateCount.setText("Very Good" + rateValue + "/5")

            }else if(rateValue<=5&&rateValue >4){
                binding.rateCount.setText("Best" + rateValue + "/5")

            }


            binding.submit.setOnClickListener {
                userRate(userid,resid,binding.ratingBar2.rating , binding.review.text.toString())
                calculateAverageRateForRestaurant(resid)
                //temp=binding.rateCount.text.toString()
                binding .thank.setText("Thank you for rateing")
                binding.review.setText("")
                binding.ratingBar2.rating=0f
                binding.rateCount.setText("")
               /* val i = Intent(this,ResturantAct::class.java)
                this.startActivity(i)*/


            }

        }

    }

    fun userRate(userid: String, resid: String, rate: Float, review: String) {
        val userRateQuery = db.collection("ResturantrRate")
            .whereEqualTo("userid", userid)
            .whereEqualTo("resid", resid)
        userRateQuery.get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    val userRate = hashMapOf("userid" to userid, "resid" to resid, "rate" to rate, "review" to review)
                    db.collection("ResturantrRate")
                        .add(userRate)
                        .addOnSuccessListener { documentReference ->
                            Log.e("saja", "userRate added")
                        }
                        .addOnFailureListener { e ->
                            Log.e("saja", "Failed to add userRate: ${e.message}")
                        }
                } else {
                    Toast.makeText(this," you are already rated for this restaurant",Toast.LENGTH_SHORT).show()
                    Log.e("saja", "User has already rated for this restaurant.")
                }
            }
            .addOnFailureListener { e ->
                Log.e("saja", "Failed to check user rate: ${e.message}")
            }
    }




    private fun calculateAverageRateForRestaurant(resid: String) {
        db.collection("ResturantrRate")
            .whereEqualTo("resid", resid)
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
        val updaterate = hashMapOf<String,Any>(
            "rate" to averageRate

        )
        db.collection("restaurants").document(resid)
            .update(updaterate).addOnSuccessListener {
                Log.e("saja", "update Res Rate success")

            }.addOnFailureListener{
                Log.e("saja", it.message.toString())


            }




    }







}
/*

class UserResRate : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    lateinit var resid:String
    lateinit var userid:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityUserResRateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var rateValue:Float
        var temp:String

        db= Firebase.firestore

        resid = intent.getStringExtra("resid").toString()
        Log.e("saja","intent $resid")
        val sharedPref = getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
        userid = sharedPref.getString("userid", "noo user").toString()


        binding.ratingBar2.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->

            rateValue=binding.ratingBar2.rating
            if(rateValue<=1&&rateValue>0) {
                binding.rateCount.setText("Bad" + rateValue + "/5")
            }
            else if(rateValue<=2&&rateValue <1){
                binding.rateCount.setText("ok" + rateValue + "/5")

            }  else if(rateValue<=3&&rateValue <2){
                binding.rateCount.setText("Good" + rateValue + "/5")

            }  else if(rateValue<=4&&rateValue <3){
                binding.rateCount.setText("Very Good" + rateValue + "/5")

            }else if(rateValue<=5&&rateValue <4){
                binding.rateCount.setText("Best" + rateValue + "/5")

            }


            binding.submit.setOnClickListener {
                userRate(userid,resid,binding.ratingBar2.rating , binding.review.text.toString())

                temp=binding.rateCount.text.toString()
                binding .showRating.setText("yourRating:\n "+ temp+"\n"+binding.review.text.toString())
                binding.review.setText("")
                binding.ratingBar2.rating=0f
                binding.rateCount.setText("")
                val i = Intent(this,ResturantAct::class.java)
                this.startActivity(i)


            }

        }






    }
    fun userRate(userid:String,resid:String,rate:Float ,review:String){
        //val user = user("",username,email,pass,"EnterYour Address")
        val userRate= hashMapOf("userid" to userid,"resid" to resid, "rate" to rate ,"review" to review)
        db.collection("ResturantrRate")
            .add(userRate)
            .addOnSuccessListener { documentReference ->
                Log.e("saja", "userRateadded" )


            }
            .addOnFailureListener { e ->
                Log.e("saja", "fail" )

            }

    }

}*/