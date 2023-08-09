package com.example.andro2groupprojecttest.ui.profile

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.andro2groupprojecttest.MapsActivity
import com.example.andro2groupprojecttest.R
import com.example.andro2groupprojecttest.databinding.FragmentMealDetailsBinding
import com.example.andro2groupprojecttest.databinding.FragmentProfileBinding
import com.example.andro2groupprojecttest.loginActivity
import com.example.andro2groupprojecttest.ui.Modle.MealsData
import com.example.andro2groupprojecttest.ui.Modle.user
import com.example.andro2groupprojecttest.ui.search.NotificationsViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    val COLLECTION_NAME = "users"
    lateinit var db: FirebaseFirestore
    lateinit var id:String
    lateinit var username:String
    lateinit var user: user

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        db = Firebase.firestore
        val sharedPref = requireContext().getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
        id = sharedPref.getString("userid", "noo user").toString()

        getuserById(id)

       binding.buttonlog.setOnClickListener {
           val editor = sharedPref.edit()
           editor.clear()
           editor.commit()
           val i = Intent(requireActivity(), loginActivity::class.java)
           startActivity(i)

      }
 binding.LocationIcon.setOnClickListener {
     findNavController().navigate(R.id.userLocation)

 }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onResume() {
        super.onResume()
    }




    private fun  getuserById(id:String) {

        val query = db.collection(COLLECTION_NAME).document(id)
        query.addSnapshotListener { documentSnapshot, exception ->
                if (exception != null) {
                    Log.e("hzm", "Error listening to Firestore: ${exception.message}")

                    // Handle the error
                    return@addSnapshotListener
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {

                    user = user(
                        documentSnapshot.id,
                        documentSnapshot.getString("username")!!,
                        documentSnapshot.getString("email")!!,
                        documentSnapshot.getString("password")!!,
                        documentSnapshot.getString("address")!!,
                    )
                    Log.e("hzm", "user info :$id")

                    binding.textView.text = user.username
                    binding.textView2.text = user.email
                    binding.email.text = user.email
                    binding.PasswordNum.text = user.password
                    binding.Street.text = user.address
                }

            }


    }






}