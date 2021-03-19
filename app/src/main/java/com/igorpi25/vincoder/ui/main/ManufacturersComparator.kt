package com.igorpi25.vincoder.ui.main

import androidx.recyclerview.widget.DiffUtil

object ManufacturersComparator : DiffUtil.ItemCallback<UiModel>() {
    override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
        return (oldItem is UiModel.ManufacturerItem && newItem is UiModel.ManufacturerItem &&
                oldItem.manufacturer.id == newItem.manufacturer.id) ||
                (oldItem is UiModel.SeparatorItem && newItem is UiModel.SeparatorItem &&
                        oldItem.page == newItem.page)
    }

    override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
        return oldItem == newItem
    }
}