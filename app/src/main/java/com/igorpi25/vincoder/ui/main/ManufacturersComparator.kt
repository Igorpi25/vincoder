package com.igorpi25.vincoder.ui.main

import androidx.recyclerview.widget.DiffUtil
import com.igorpi25.vincoder.ui.common.UiModel

object ManufacturersComparator : DiffUtil.ItemCallback<UiModel>() {
    override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
        return (oldItem is UiModel.ManufacturerItem && newItem is UiModel.ManufacturerItem &&
                oldItem.manufacturer.id == newItem.manufacturer.id) ||
                (oldItem is UiModel.PageItem && newItem is UiModel.PageItem &&
                        oldItem.page == newItem.page)
    }

    override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
        return oldItem == newItem
    }
}