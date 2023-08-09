package com.example.andro2groupprojecttest.ui.home
import android.app.Activity
import android.content.Intent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.andro2groupprojecttest.ResturantAct
import com.example.andro2groupprojecttest.databinding.ResturantItemBinding
import com.squareup.picasso.Picasso


class HomeVerAdapter(var activity: Activity, private var data: ArrayList<HomeVerData>         /* val fragmentManager: FragmentManager*/
) : RecyclerView.Adapter<HomeVerAdapter.MyViewHolder>() {

    class MyViewHolder(var binding: ResturantItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ResturantItemBinding.inflate(activity.layoutInflater, parent, false)
        return MyViewHolder(binding)    }

    override fun getItemCount(): Int {
        return data.size    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.name.text=data[position].name
        holder.binding.ratingBar.rating= data[position].rate.toFloat()
        holder.binding.rateTxt.text=data[position].rate.toString()
        holder.binding.detailsBtn.setOnClickListener {
            val i = Intent(activity, ResturantAct::class.java)
            i.putExtra("id",data[position].id)
            activity.startActivity(i)
        }
        Picasso.get().load(data[position].imageurl).into(holder.binding.resImg)




    }
}

