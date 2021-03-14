package com.igorpi25.vincoder.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.igorpi25.vincoder.model.Manufacturer
import com.igorpi25.vincoder.databinding.ItemLayoutBinding

class ManufacturersListAdapter(private val context: Context, private val manufacturerList: MutableList<Manufacturer>):RecyclerView.Adapter<ManufacturersListAdapter.MyViewHolder>() {

    class MyViewHolder(binding: ItemLayoutBinding): RecyclerView.ViewHolder(binding.root){
        val manufacturerName: TextView = binding.manufacturerName
        val manufacturerCountry: TextView = binding.manufacturerCountry
        val manufacturerId: TextView = binding.manufacturerId

        fun bind(listItem: Manufacturer) {
            itemView.setOnClickListener {
                Toast.makeText(it.context, "нажал на ${manufacturerName.text}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount() = manufacturerList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val listItem = manufacturerList[position]
        holder.bind(listItem)

        holder.manufacturerName.text = manufacturerList[position].name
        holder.manufacturerCountry.text = manufacturerList[position].country
        holder.manufacturerId.text = manufacturerList[position].id.toString()
    }

}