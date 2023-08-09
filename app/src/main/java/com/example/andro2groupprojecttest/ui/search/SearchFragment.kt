package com.example.andro2groupprojecttest.ui.search
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.andro2groupprojecttest.Adapter.MealsAdapter
import com.example.andro2groupprojecttest.databinding.FragmentSearchBinding
import com.example.andro2groupprojecttest.ui.Modle.MealsData
import com.example.andro2groupprojecttest.ui.home.HomeVerAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: MealsAdapter
    private val meals: MutableList<MealsData> = mutableListOf()
    lateinit var db: FirebaseFirestore


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = Firebase.firestore

            adapter = MealsAdapter(requireActivity(),meals)

         binding.RateFilter.setOnClickListener {
         searchText("rate")
         }
        binding.NameFilter.setOnClickListener {
            searchText("name")
        }
        binding.priceFilter.setOnClickListener {
            searchText("price")

        }
    }

    private fun searchText(filterType: String){
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    search(newText, filterType)
                } else {
                    meals.clear()
                    adapter.notifyDataSetChanged()
                }
                return true
            }
        })
    }
    private fun search(query: String, filterType: String) {
        val collectionRef = db.collection("meals")
        val queryRef = if (filterType == "rate") {
            collectionRef.whereEqualTo(filterType, query.toFloatOrNull())
        } else {
            collectionRef.whereEqualTo(filterType, query)
        }

        queryRef.get()
            .addOnSuccessListener { querySnapshot ->
                meals.clear()
                for (document in querySnapshot.documents) {
                    val meal = document.toObject(MealsData::class.java)
                    meal?.let {
                        meals.add(meal)
                    }
                }

                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerView.adapter = adapter
                binding.recyclerView.setHasFixedSize(true)
                binding.recyclerView.isNestedScrollingEnabled = false

                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d("saja", "Error getting documents: ", exception)
            }
    }





}
/*    private fun search(query: String ,filterType: String) {
        if(filterType.equals("rate")){
            val queryFloat = query.toFloatOrNull()
            if (queryFloat != null) {
                firestore.collection("meals")
                    .orderBy("rate")
                    .startAt(queryFloat)
                    .endAt(queryFloat + 0.1f)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        updateList(querySnapshot)
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "Error getting documents: ", exception)
                    }
            }
        } else{
        firestore.collection("meals")
            .orderBy(filterType)
            .startAt(query)
            .endAt(query + "\uf8ff")
            .get()
            .addOnSuccessListener { querySnapshot ->
                updateList(querySnapshot)

            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }}*/