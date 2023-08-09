package com.example.andro2groupprojecttest.Adapter

import android.app.Activity
import android.content.Intent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.andro2groupprojecttest.databinding.OrdersItemBinding
import com.example.andro2groupprojecttest.ui.Modle.OrderData


class OrdersAdapter(var activity: Activity, private var data: ArrayList<com.example.andro2groupprojecttest.ui.Modle.OrderData>) :
    RecyclerView.Adapter<OrdersAdapter.MyViewHolder>() {

    class MyViewHolder( var binding: OrdersItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = OrdersItemBinding.inflate(activity.layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.imageView5.setImageResource(data[position].img)
        holder.binding.name.text= data[position].name
        holder.binding.price.text= data[position].price.toString()
        holder.binding.orderStatus.text= data[position].status




    }

}