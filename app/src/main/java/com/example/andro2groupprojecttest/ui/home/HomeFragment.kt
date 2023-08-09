package com.example.andro2groupprojecttest.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.andro2groupprojecttest.ResturantAct
import com.example.andro2groupprojecttest.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var db: FirebaseFirestore
    val COLLECTION_NAME = "restaurants"
    //private lateinit var adapter: HomeVerAdapter
    private lateinit var resturant: ArrayList<HomeVerData>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        db = Firebase.firestore
        resturant = arrayListOf()

        val sharedPref =requireContext().getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
        val username =sharedPref.getString("username","noo user")
        binding.textView7.text="Hello,$username"
        return root
       }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onResume() {
        getAllResturant()
        super.onResume()
    }






    fun  getAllResturant(){
        val query = db.collection(COLLECTION_NAME)
        query.addSnapshotListener { querySnapshot, exception ->
            if (exception != null) {
                Log.e("saja", "Error listening to Firestore: ${exception.message}")
                return@addSnapshotListener
            }

            if (querySnapshot != null) {
                resturant.clear()
                for (document in querySnapshot) {
                    Log.e("hzm", document.id)
                    Log.e("hzm", document.getString("name")!!)
                    val res = HomeVerData(
                        document.id,
                        document.getString("name")!!,
                        document.getString("address")!!,
                        document.getDouble("rate")!!.toFloat(),
                        document.getString("imageUrl")!!
                    )
                    resturant.add(res)
                    res.id = document.id
                }

                binding.verticalRec.layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL, false
                )
                val verAdapter = HomeVerAdapter(requireActivity(), resturant)
                binding.verticalRec.adapter = verAdapter
                binding.verticalRec.setHasFixedSize(true)
                binding.verticalRec.isNestedScrollingEnabled = false
            }
        }
    }


}

/*
    private fun getAllResturant() {
           val data = ArrayList<HomeVerData>()

           db.collection(COLLECTION_NAME)
               .get()
               .addOnSuccessListener { querySnapshot ->
                   data.clear()
                   for (document in querySnapshot) {
                       val res = HomeVerData(
                           document.id,
                           document.getString("name")!!,
                           document.getString("address")!!,
                           document.getDouble("rate")!!.toFloat(),
                           document.getString("imageUrl")!!
                       )
                       data.add(res)
                       res.id = document.id
                   }

                   binding.verticalRec.layoutManager = LinearLayoutManager(
                       requireContext(),
                       LinearLayoutManager.VERTICAL, false
                   )
                   val VerAdapter = HomeVerAdapter(requireActivity(), data,/*this*/)
                   binding.verticalRec.adapter = VerAdapter

                   binding.verticalRec.setHasFixedSize(true)
                   binding.verticalRec.isNestedScrollingEnabled = false
               }
               .addOnFailureListener { exception ->
                   Log.e("hzm", "Error getting documents: ${exception.message}")
               }
       }
   }
  */



