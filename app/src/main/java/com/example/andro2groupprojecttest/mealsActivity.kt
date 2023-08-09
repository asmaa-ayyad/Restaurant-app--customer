package com.example.andro2groupprojecttest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.andro2groupprojecttest.Adapter.MealsAdapter
import com.example.andro2groupprojecttest.databinding.ActivityMealsBinding
import com.example.andro2groupprojecttest.ui.Modle.MealsData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class mealsActivity : AppCompatActivity() {
    lateinit var binding: ActivityMealsBinding
    lateinit var id:String
    lateinit var db: FirebaseFirestore
    lateinit var adapter: MealsAdapter
    internal lateinit var data: ArrayList<com.example.andro2groupprojecttest.ui.Modle.MealsData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = Firebase.firestore
        data = arrayListOf()
        id = intent.getStringExtra("resid").toString()
        Log.e("saja", "res id :$id")



    }

    private lateinit var mealsListener: ListenerRegistration
    private fun getMealsByResId(restaurantId: String) {
        data = ArrayList<MealsData>()

        val mealsRef = db.collection("meals")
            .whereEqualTo("restid", restaurantId)

        mealsListener = mealsRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("TAG", exception.message!!)
                return@addSnapshotListener
            }

            data.clear()

            for (document in snapshot!!) {
                val id = document.id
                val name = document.getString("name") ?: ""
                val price = document.getString("price") ?: ""
                val rate = document.get("rate")
                val description = document.getString("description") ?: ""
                val restid = document.getString("restid") ?: ""
                val issold = document.getString("issold") ?: ""
                val numofreq = document.get("numofreq").toString().toInt()
                val imageUrlString = document.getString("imageUrl") ?: ""
                data.add(
                    com.example.andro2groupprojecttest.ui.Modle.MealsData(
                        id,
                        name,
                        price,
                        rate.toString().toDouble(),
                        description,
                        imageUrlString,
                        restid,
                        issold
                    )
                )
            }

            binding.Meals.setHasFixedSize(true)
            binding.Meals.isNestedScrollingEnabled = false
            binding.Meals.layoutManager = LinearLayoutManager(this)
            adapter = MealsAdapter(this, data)
            binding.Meals.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        getMealsByResId(id)
    }

    override fun onPause() {
        super.onPause()
        mealsListener.remove()
    }


}