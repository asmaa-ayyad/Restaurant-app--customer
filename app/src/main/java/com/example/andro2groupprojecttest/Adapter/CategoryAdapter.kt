package com.example.andro2groupprojecttest.Adapter

import android.app.Activity
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.andro2groupprojecttest.databinding.CategoryItemBinding

import com.example.andro2groupprojecttest.ui.Modle.categoryData

class CategoryAdapter(var activity: Activity, private var data: ArrayList<com.example.andro2groupprojecttest.ui.Modle.categoryData>) :
    RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

    class MyViewHolder( var binding: CategoryItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CategoryItemBinding.inflate(activity.layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
    holder.binding.categoryName.text=data[position].Name



    }

}