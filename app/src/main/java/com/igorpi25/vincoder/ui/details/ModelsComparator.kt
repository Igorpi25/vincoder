package com.igorpi25.vincoder.ui.details

import androidx.recyclerview.widget.DiffUtil
import com.igorpi25.vincoder.ui.common.UiModel

object ModelsComparator : DiffUtil.ItemCallback<UiModel>() {
    override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
        return (oldItem is UiModel.ModelItem && newItem is UiModel.ModelItem &&
                oldItem.model.id == newItem.model.id) ||
                (oldItem is UiModel.MakeItem && newItem is UiModel.MakeItem &&
                        oldItem.name == newItem.name)
    }

    override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
        return oldItem == newItem
    }
}