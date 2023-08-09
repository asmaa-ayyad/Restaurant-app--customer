package com.example.andro2groupprojecttest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.example.andro2groupprojecttest.Adapter.CategoryAdapter
import com.example.andro2groupprojecttest.databinding.ActivityUserResRateBinding
import com.example.andro2groupprojecttest.databinding.FragmentResturantBinding
import com.example.andro2groupprojecttest.ui.Modle.categoryData
import com.example.andro2groupprojecttest.ui.home.HomeVerAdapter
import com.example.andro2groupprojecttest.ui.home.HomeVerData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class ResturantAct : AppCompatActivity() {
    val COLLECTION_NAME = "restaurants"
lateinit var binding: FragmentResturantBinding
    lateinit var db:FirebaseFirestore
    lateinit var id:String
    lateinit var userid:String
    lateinit var res:HomeVerData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = FragmentResturantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db=Firebase.firestore
        id = intent.getStringExtra("id").toString()
         getResturantById(id)

        binding.menuIc.setOnClickListener{
            val i = Intent(this, mealsActivity::class.java)
            i.putExtra("resid",id)
            this.startActivity(i)
        }

        binding.btnLocation.setOnClickListener {
            val i = Intent(this, MapsActivity::class.java)
            i.putExtra("resid",id)
            this.startActivity(i)
        }

        binding.GoToReview.setOnClickListener {
            val i = Intent(this,UserResRate::class.java)
            i.putExtra("resid",id)
            this.startActivity(i)

        }
        binding.icCart2.setOnClickListener {
            val i = Intent(this, cartAct::class.java)
            this.startActivity(i)

        }

        val sharedPref = getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
        userid = sharedPref.getString("userid", "noo user").toString()
        getUserRate(userid,id)



/*
        val data2 = ArrayList<com.example.andro2groupprojecttest.ui.Modle.categoryData>()
        data2.add(com.example.andro2groupprojecttest.ui.Modle.categoryData("FastFood"))
        data2.add(com.example.andro2groupprojecttest.ui.Modle.categoryData("BreekFast"))
        data2.add(com.example.andro2groupprojecttest.ui.Modle.categoryData("Lunch"))

        binding.resMeals.layoutManager=GridLayoutManager(this,2)
        val VerAdapter = CategoryAdapter(this, data2)
        binding.resMeals.adapter=VerAdapter

        binding.resMeals.setHasFixedSize(true)
        binding.resMeals.isNestedScrollingEnabled=false
        binding.resMeals.setHasFixedSize(true)
        binding.resMeals.isNestedScrollingEnabled=false
*/
    }




    private fun  getResturantById(id:String){

        db.collection(COLLECTION_NAME).document(id)
            .addSnapshotListener { documentSnapshot, exception ->
                if (exception != null) {
                    Log.e("saja", "Error listening to Firestore: ${exception.message}")

                    return@addSnapshotListener
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    res = HomeVerData(
                        documentSnapshot.id,
                        documentSnapshot.getString("name") ?: "",
                        documentSnapshot.getString("address") ?: "",
                        documentSnapshot.getDouble("rate")?.toFloat() ?: 0f,
                        documentSnapshot.getString("imageUrl") ?: ""
                    )
                    binding.tvItemName.text = res.name
                    binding.ratingBar.rating = res.rate
                    binding.txtvItemRating.text = res.rate.toString()
                    Picasso.get().load(res.imageurl).into(binding.imageView)
                }
            }

        }


    fun getUserRate(userid: String, resid: String) {
        val query = db.collection("ResturantrRate")
            .whereEqualTo("userid", userid)
            .whereEqualTo("resid", resid)

      query.addSnapshotListener { querySnapshot, e ->
            if (e != null) {
                Log.e("saja", "Failed to retrieve user rate: ${e.message}")
                return@addSnapshotListener
            }

            if (!querySnapshot!!.isEmpty) {
                val document = querySnapshot.documents[0]
                val rate = document.get("rate")
                val review = document.getString("review") as String

                binding.ratingBar3.rating = rate.toString().toFloat()
                //binding.textView22.text = rate.toString()

                Log.e("saja", "Rate: $rate, Review: $review")
            } else {
                Log.e("saja", "User rate not found")
            }
        }


    }



}