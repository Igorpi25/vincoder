package com.igorpi25.vincoder.ui.main

import androidx.recyclerview.widget.DiffUtil
import com.igorpi25.vincoder.retrofit.model.Manufacturer

object ManufacturersComparator : DiffUtil.ItemCallback<Manufacturer>() {
    override fun areItemsTheSame(oldItem: Manufacturer, newItem: Manufacturer): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Manufacturer, newItem: Manufacturer): Boolean {
        return oldItem == newItem
    }
}