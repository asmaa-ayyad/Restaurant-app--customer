package com.example.andro2groupprojecttest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.andro2groupprojecttest.databinding.ActivityUserResRateBinding
import com.example.andro2groupprojecttest.databinding.FragmentMealDetailsBinding
import com.example.andro2groupprojecttest.databinding.FragmentResturantBinding
import com.example.andro2groupprojecttest.ui.Modle.MealsData
import com.example.andro2groupprojecttest.ui.dashboard.DashboardFragment
import com.example.andro2groupprojecttest.ui.home.HomeVerData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlin.properties.Delegates

class meal_details : AppCompatActivity() {
    val COLLECTION_NAME = "meals"
    lateinit var binding:FragmentMealDetailsBinding
    lateinit var db: FirebaseFirestore
    lateinit var id:String
    lateinit var meal: com.example.andro2groupprojecttest.ui.Modle.MealsData
    lateinit var  userid:String
    var q by Delegates.notNull<Int>()
    var numOfReq by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = FragmentMealDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = Firebase.firestore
         q=1
        val sharedPref = getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
        userid = sharedPref.getString("userid", "noo user").toString()
        id = intent.getStringExtra("meal_id").toString()
        Log.e("saja", "mealid :$id")
        getNumOfReqForMeal(id)
        getMealById(id)

        binding.btnFav.setOnClickListener {
            AddMealToFav(userid,id)
            Toast.makeText(this, "Your meal Added to favourite successfuly",Toast.LENGTH_SHORT).show()

            /*val fragment = com.example.andro2groupprojecttest.ui.dashboard.DashboardFragment()
            val bundle = Bundle().apply {
                putString("mealid", "$id")
            }
            fragment.arguments = bundle
            */
        }

        binding.icCart.setOnClickListener {
            val i = Intent(this, cartAct::class.java)
            i.putExtra("mealid",id)
            this.startActivity(i)

        }


        binding.GoToReview.setOnClickListener{
            val i = Intent(this, UserMealRate::class.java)
            i.putExtra("mealid",id)
            this.startActivity(i)
        }

        binding.btnAddToCart.setOnClickListener {
            updateIsSoldField()
            val price= binding.tvItemPrice.text.toString().toFloat()
            AddMealToCart(userid,id,q,price)
            Toast.makeText(this, "Your meal Added to cart successfuly",Toast.LENGTH_SHORT).show()
        }


        binding.btnPlus.setOnClickListener{
             q=binding.tvItemQuantity.text.toString().toInt()
            q += 1
            binding.tvItemQuantity.text=q.toString()
        }
        binding.btnMinus.setOnClickListener{
            var q=binding.tvItemQuantity.text.toString().toInt()
            if(q>1) {
                q -= 1
                binding.tvItemQuantity.text = q.toString()
            }
        }




        getUserRate(userid,id )



    }

    private fun  getMealById(id:String){
        db.collection(COLLECTION_NAME).document(id)
            .addSnapshotListener { documentSnapshot, exception ->
                if (exception != null) {
                    return@addSnapshotListener
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                     meal = com.example.andro2groupprojecttest.ui.Modle.MealsData(
                         documentSnapshot.id,
                         documentSnapshot.getString("name")!!,
                         documentSnapshot.getString("price")!!,
                         documentSnapshot.get("rate")!!.toString().toDouble(),
                         documentSnapshot.getString("description")!!,
                         documentSnapshot.getString("imageUrl")!!,
                         documentSnapshot.getString("restid")!!,
                         documentSnapshot.getString("issold")!!,
                         //documentSnapshot.get("numofreq")!!.toString().toInt()
                     )

                    binding.tvItemName.text = meal.name
                    binding.ratingBar.rating = meal.rate.toFloat()
                    binding.txtvItemRating.text = meal.rate.toString()
                    binding.tvItemPrice.text=meal.price
                    binding.tvItemDetail.text= documentSnapshot.getString("description")
                    Picasso.get().load(meal.imageUrl).into(binding.mealImage)
                }
            }

    }


    private fun updateIsSoldField() {
        val updateSold = hashMapOf<String,Any>(
            "issold" to "1",
            "numofreq" to numOfReq+q

        )
        db.collection("meals").document(id)
            .update(updateSold).addOnSuccessListener {
                Log.e("saja", "update meals request info success")

            }.addOnFailureListener{
                Log.e("saja", it.message.toString())


            }
    }

        private fun getNumOfReqForMeal(mealId: String) {
            db.collection("meals")
                .document(mealId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                         numOfReq = documentSnapshot.get("numofreq").toString().toInt()
                        Log.e("saja", numOfReq.toString())

                    } else {
                        Log.e("saja", "Document doesn't exist")


                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("saja", exception.message!!)
                }
        }

    fun getUserRate(userid: String, mealid: String) {
        val query = db.collection("mealRate")
            .whereEqualTo("userid", userid)
            .whereEqualTo("mealid", mealid)

        query.addSnapshotListener { querySnapshot, e ->
            if (e != null) {
                Log.e("saja", "Failed to retrieve user rate: ${e.message}")
                return@addSnapshotListener
            }

            if (!querySnapshot!!.isEmpty ) {
                val document = querySnapshot.documents[0]
                val rate = document.get("rate")
                val review = document.getString("review") as String
                binding.userRate.rating = rate.toString().toFloat()
                Log.e("saja", "Rate: $rate, Review: $review")
            } else {
                Log.e("saja", "User rate not found")
            }
        }

    }

    fun AddMealToCart(userId: String, mealId: String, quantity: Int, price: Float) {
        val cartRef =db.collection("usercart")
        val query = cartRef.whereEqualTo("userid", userId).whereEqualTo("mealid", mealId)
        query.get().addOnSuccessListener { querySnapshot ->
            if (querySnapshot.isEmpty) {
                val item = hashMapOf(
                    "userid" to userId,
                    "mealid" to mealId,
                    "quantity" to quantity,
                    "price" to price
                )
                cartRef.add(item)
                    .addOnSuccessListener { documentReference ->
                        Log.e("saja", "added")
                    }
                    .addOnFailureListener { e ->
                        Log.e("saja", "fail")
                    }
            } else {
                Toast.makeText(this, "You have already added this meal to your cart", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
        }
    }


    fun AddMealToFav(userId: String, mealId: String) {
        val favRef = db.collection("userFavorite")
        val query = favRef.whereEqualTo("userid", userId).whereEqualTo("mealid", mealId)
        query.get().addOnSuccessListener { querySnapshot ->
            if (querySnapshot.isEmpty) {
                val item = hashMapOf(
                    "userid" to userId,
                    "mealid" to mealId
                )
                favRef.add(item)
                    .addOnSuccessListener { documentReference ->
                        Log.e("saja", "added")
                    }
                    .addOnFailureListener { e ->
                        Log.e("saja", "fail")
                    }
            } else {
                Toast.makeText(this, "You have already added this meal to your Favourite", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
        }
    }





}