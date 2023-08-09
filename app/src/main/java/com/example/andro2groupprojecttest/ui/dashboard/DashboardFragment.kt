package com.example.andro2groupprojecttest.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.andro2groupprojecttest.Adapter.MealsAdapter
import com.example.andro2groupprojecttest.Adapter.OrdersAdapter
import com.example.andro2groupprojecttest.R
import com.example.andro2groupprojecttest.databinding.FragmentDashboardBinding
import com.example.andro2groupprojecttest.databinding.FragmentOrdersBinding
import com.example.andro2groupprojecttest.ui.Modle.MealsData
import com.example.andro2groupprojecttest.ui.Modle.OrderData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    lateinit var db: FirebaseFirestore
    lateinit var userid:String
    lateinit var adapter: MealsAdapter
    lateinit var data: ArrayList<MealsData>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        db = Firebase.firestore
       // val mealid = arguments?.getString("mealid")

        val sharedPref =requireContext().getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
         userid =sharedPref.getString("userid","noo user").toString()

        return root
    }
    override fun onResume() {
        super.onResume()
        getUserFav(userid)

    }

    private var mealListener: ListenerRegistration? = null

    fun getUserFav(userId: String) {
        val favRef = db.collection("userFavorite").whereEqualTo("userid", userId)

        favRef.get().addOnSuccessListener { querySnapshot ->
            val mealIds = mutableListOf<String>()
            for (document in querySnapshot.documents) {
                val mealId = document.getString("mealid")
                Log.d("saja", "data $mealId")
                if (mealId != null) {
                    mealIds.add(mealId)
                }
            }

            val mealDetails = mutableListOf<MealsData>()
            val mealRef = db.collection("meals")

            mealListener = mealRef.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    return@addSnapshotListener
                }

                mealDetails.clear()

                for (mealId in mealIds) {
                    val mealDocRef = mealRef.document(mealId)
                    mealDocRef.get().addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val meal = documentSnapshot.toObject(MealsData::class.java)
                            meal?.let {
                                mealDetails.add(it)
                            }

                            binding.favres.setHasFixedSize(true)
                            binding.favres.isNestedScrollingEnabled = false
                            binding.favres.layoutManager = LinearLayoutManager(requireContext())
                            adapter = MealsAdapter(requireActivity(), mealDetails)
                            binding.favres.adapter = adapter
                        }
                    }
                }
            }
        }.addOnFailureListener { exception ->
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mealListener?.remove()
    }



    }

