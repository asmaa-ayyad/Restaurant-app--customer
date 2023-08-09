package com.example.andro2groupprojecttest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.andro2groupprojecttest.Adapter.MealsAdapter
import com.example.andro2groupprojecttest.Adapter.cardAdapter
import com.example.andro2groupprojecttest.databinding.FragmentCartBinding
import com.example.andro2groupprojecttest.ui.Modle.MealsData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class cartAct : AppCompatActivity() {
    lateinit var binding: FragmentCartBinding
    lateinit var db: FirebaseFirestore
    lateinit var id:String
     lateinit var data: ArrayList<MealsData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCartBinding.inflate(layoutInflater)

        setContentView(binding.root)
        db = Firebase.firestore
        data = arrayListOf()

        val sharedPref =getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
        val id =sharedPref.getString("userid","noo user")
        getUserCart(id.toString())



    }
    //private var cartListener: ListenerRegistration? = null
  //  private var mealListener: ListenerRegistration? = null

    fun getUserCart(userId: String) {
        val cartRef = db.collection("usercart").whereEqualTo("userid", userId)
            cartRef.addSnapshotListener { querySnapshot, exception ->
            if (exception != null) {
                return@addSnapshotListener
            }

            val mealIds = mutableListOf<String>()
            val mealQuantities = mutableMapOf<String, Int>()
            var totalPrice = 0.0

            for (document in querySnapshot!!.documents) {
                val mealId = document.getString("mealid")
                val quantity = document.getLong("quantity")?.toInt() ?: 0

                if (mealId != null) {
                    mealIds.add(mealId)
                    mealQuantities[mealId] = quantity
                }
            }


            val mealDetails = mutableListOf<MealsData>()
            val mealRef = db.collection("meals")
                mealRef.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    return@addSnapshotListener
                }
                mealDetails.clear()
                for (mealId in mealIds) {
                    mealRef.document(mealId).get().addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val meal = documentSnapshot.toObject(MealsData::class.java)
                            meal?.let {
                                mealDetails.add(it)
                                val quantity = mealQuantities[mealId] ?: 0
                                val subtotal = it.price.toString().toFloat() * quantity.toString().toInt()
                                totalPrice += subtotal
                            }
                            updateRecyclerView(mealDetails, totalPrice)
                        }
                    }
                }
            }
        }
    }

    private fun updateRecyclerView(mealDetails: List<MealsData>, totalPrice: Double) {
        binding.cardres.setHasFixedSize(true)
        binding.cardres.isNestedScrollingEnabled = false
        binding.cardres.layoutManager = LinearLayoutManager(this)
        val adapter = cardAdapter(this, mealDetails as MutableList<MealsData>)
        binding.cardres.adapter = adapter
        binding.totalFreeTxt.text = totalPrice.toString()
        binding.Total.text = totalPrice.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
    }




    }