package com.igorpi25.vincoder.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.igorpi25.vincoder.retrofit.model.Manufacturer
import com.igorpi25.vincoder.databinding.ItemLayoutBinding

class ManufacturersPagingAdapter(
    diffCallback: DiffUtil.ItemCallback<Manufacturer>,
    private val listener: (id: Int?)->Unit
) : PagingDataAdapter<Manufacturer, ManufacturersPagingAdapter.MyViewHolder>(diffCallback) {

    class MyViewHolder(binding: ItemLayoutBinding, val listener: (id: Int?)->Unit): RecyclerView.ViewHolder(binding.root){
        val manufacturerName: TextView = binding.manufacturerName
        val manufacturerCountry: TextView = binding.manufacturerCountry
        val manufacturerId: TextView = binding.manufacturerId

        fun bind(listItem: Manufacturer) {

            itemView.setOnClickListener {
                Toast.makeText(it.context, "нажал на ${manufacturerName.text}", Toast.LENGTH_SHORT).show()
                listener(listItem.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(
            binding,
            listener
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val listItem = getItem(position)
        holder.bind(listItem!!)

        holder.manufacturerName.text = listItem.name
        holder.manufacturerCountry.text = listItem.country
        holder.manufacturerId.text = listItem.id.toString()
    }



}