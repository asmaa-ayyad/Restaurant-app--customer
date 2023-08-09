package com.example.andro2groupprojecttest.Adapter

import android.app.Activity
import android.content.Intent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.andro2groupprojecttest.databinding.MealItemBinding
import com.example.andro2groupprojecttest.meal_details
import com.example.andro2groupprojecttest.ui.Modle.MealsData
import com.squareup.picasso.Picasso

class MealsAdapter(var activity: Activity, private var data: MutableList<com.example.andro2groupprojecttest.ui.Modle.MealsData>) :
    RecyclerView.Adapter<MealsAdapter.MyViewHolder>() {

    class MyViewHolder( var binding: MealItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = MealItemBinding.inflate(activity.layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //holder.binding.imageView5.setImageResource(data[position].img)
        holder.binding.name.text= data[position].name
        holder.binding.price.text= data[position].price
        holder.binding.ratingBar.rating= data[position].rate.toFloat()
        Picasso.get().load(data[position].imageUrl).into(holder.binding.imageView5)

        holder.binding.detailsBtn.setOnClickListener {
            val i = Intent(activity, meal_details::class.java)
            i.putExtra("meal_id",data[position].m_id)
            activity.startActivity(i)

        }



    }

}