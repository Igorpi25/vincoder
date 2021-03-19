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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val listItem = getItem(position)
        holder.bind(listItem!!)
    }

    class MyViewHolder(binding: ItemLayoutBinding, val listener: (id: Int?)->Unit): RecyclerView.ViewHolder(binding.root){
        val manufacturerName: TextView = binding.manufacturerName
        val manufacturerCountry: TextView = binding.manufacturerCountry
        val manufacturerId: TextView = binding.manufacturerId

        fun bind(listItem: Manufacturer) {

            manufacturerName.text = listItem.name
            manufacturerCountry.text = listItem.country
            manufacturerId.text = listItem.id.toString()

            itemView.setOnClickListener {
                listener(listItem.id)
            }
        }
    }

}