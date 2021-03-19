package com.igorpi25.vincoder.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.igorpi25.vincoder.R
import com.igorpi25.vincoder.databinding.ItemLayoutBinding
import com.igorpi25.vincoder.databinding.ItemSeparatorLayoutBinding

class ManufacturersPagingAdapter(
    diffCallback: DiffUtil.ItemCallback<UiModel>,
    private val listener: (id: Int?)->Unit
) : PagingDataAdapter<UiModel, RecyclerView.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_layout -> {
                MyViewHolder(ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false), listener)
            }
            else -> {
                SeparatorViewHolder(ItemSeparatorLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val listItem = getItem(position)

        when (listItem) {
            is UiModel.ManufacturerItem -> {
                (holder as MyViewHolder).bind(listItem as UiModel.ManufacturerItem)
            }
            is UiModel.SeparatorItem -> {
                (holder as SeparatorViewHolder).bind(listItem as UiModel.SeparatorItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is UiModel.ManufacturerItem -> R.layout.item_layout
            is UiModel.SeparatorItem -> R.layout.item_separator_layout
            null -> throw UnsupportedOperationException("Unknown view")
        }
    }

    class MyViewHolder(binding: ItemLayoutBinding, val listener: (id: Int?)->Unit): RecyclerView.ViewHolder(binding.root){
        val manufacturerName: TextView = binding.manufacturerName
        val manufacturerCountry: TextView = binding.manufacturerCountry
        val manufacturerId: TextView = binding.manufacturerId

        fun bind(listItem: UiModel.ManufacturerItem) {
            manufacturerName.text = listItem.manufacturer.name
            manufacturerCountry.text = listItem.manufacturer.country
            manufacturerId.text = listItem.manufacturer.id.toString()

            itemView.setOnClickListener {
                listener(listItem.manufacturer.id)
            }
        }
    }

    class SeparatorViewHolder(binding: ItemSeparatorLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        val text: TextView = binding.text

        fun bind(listItem: UiModel.SeparatorItem) {
            text.text = listItem.page.toString()
        }
    }

}