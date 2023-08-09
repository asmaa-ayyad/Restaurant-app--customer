package com.example.andro2groupprojecttest.Adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.andro2groupprojecttest.databinding.MealItemBinding
import com.example.andro2groupprojecttest.databinding.OrdersItemBinding
import com.example.andro2groupprojecttest.meal_details
import com.example.andro2groupprojecttest.ui.Modle.MealsData
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class cardAdapter(var activity: Activity,  var data:MutableList<com.example.andro2groupprojecttest.ui.Modle.MealsData>) :
    RecyclerView.Adapter<cardAdapter.MyViewHolder>() {

    class MyViewHolder( var binding: OrdersItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = OrdersItemBinding.inflate(activity.layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //holder.binding.imageView5.setImageResource(data[position].img)
        holder.binding.name.text= data[position].name
        holder.binding.price.text= data[position].price
       // holder.binding.ratingBar.rating= data[position].rate.toFloat()
        Picasso.get().load(data[position].imageUrl).into(holder.binding.imageView5)

        var q=1

        holder. binding.btnPlus3.setOnClickListener{
            q= holder.binding.tvItemQuantity3.text.toString().toInt()
            q=q+1
            holder. binding.tvItemQuantity3.text=q.toString()
        }
        holder.binding.btnMinus3.setOnClickListener{
            var q=holder.binding.tvItemQuantity3.text.toString().toInt()
            if(q>1) {
                q = q - 1
                holder. binding.tvItemQuantity3.text = q.toString()
            }
        }

       /* holder.binding.detailsBtn.setOnClickListener {
            val i = Intent(activity, meal_details::class.java)
            i.putExtra("meal_id",data[position].m_id)
            activity.startActivity(i)

        }*/



    }










}