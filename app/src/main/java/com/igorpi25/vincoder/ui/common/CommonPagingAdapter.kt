package com.igorpi25.vincoder.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.igorpi25.vincoder.databinding.ItemMakeBinding
import com.igorpi25.vincoder.databinding.ItemManufacturerBinding
import com.igorpi25.vincoder.databinding.ItemModelBinding
import com.igorpi25.vincoder.databinding.ItemPageBinding

class CommonPagingAdapter(
    diffCallback: DiffUtil.ItemCallback<UiModel>,
    private val listener: (id: Int?)->Unit
) : PagingDataAdapter<UiModel, RecyclerView.ViewHolder>(diffCallback) {

    private val TYPE_MANUFACTURER = 1
    private val TYPE_MODEL = 2
    private val TYPE_PAGE = 3
    private val TYPE_MAKE = 4

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_MANUFACTURER -> {
                ManufacturerViewHolder(
                    ItemManufacturerBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                    listener
                )
            }
            TYPE_MODEL -> {
                ModelViewHolder(
                    ItemModelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
            TYPE_PAGE -> {
                PageViewHolder(
                    ItemPageBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            TYPE_MAKE -> {
                MakeViewHolder(
                    ItemMakeBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> throw Exception("Unknown ViewHolder type number")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val listItem = getItem(position)

        when (listItem) {
            is UiModel.ManufacturerItem -> {
                (holder as ManufacturerViewHolder).bind(listItem)
            }
            is UiModel.ModelItem -> {
                (holder as ModelViewHolder).bind(listItem)
            }
            is UiModel.PageItem -> {
                (holder as PageViewHolder).bind(listItem)
            }
            is UiModel.MakeItem -> {
                (holder as MakeViewHolder).bind(listItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is UiModel.ManufacturerItem -> TYPE_MANUFACTURER
            is UiModel.ModelItem -> TYPE_MODEL
            is UiModel.PageItem -> TYPE_PAGE
            is UiModel.MakeItem -> TYPE_MAKE
            null -> throw UnsupportedOperationException("Unknown view position="+position)
        }
    }

    class ManufacturerViewHolder(binding: ItemManufacturerBinding, val listener: (id: Int?)->Unit): RecyclerView.ViewHolder(binding.root){
        private val textName: TextView = binding.textName
        private val textCountry: TextView = binding.textCountry
        private val textId: TextView = binding.textId

        fun bind(listItem: UiModel.ManufacturerItem) {
            textName.text = listItem.manufacturer.name
            textCountry.text = listItem.manufacturer.country
            textId.text = listItem.manufacturer.id.toString()

            itemView.setOnClickListener {
                listener(listItem.manufacturer.id)
            }
        }
    }

    class ModelViewHolder(binding: ItemModelBinding): RecyclerView.ViewHolder(binding.root){
        private val textName: TextView = binding.textName
        private val makeName: TextView = binding.textMake
        private val textId: TextView = binding.textId

        fun bind(listItem: UiModel.ModelItem) {
            textName.text = listItem.model.name
            makeName.text = listItem.model.makeName
            textId.text = listItem.model.makeId.toString()
        }
    }

    class PageViewHolder(binding: ItemPageBinding): RecyclerView.ViewHolder(binding.root) {
        private val text: TextView = binding.text

        fun bind(listItem: UiModel.PageItem) {
            text.text = "["+listItem.page.toString()+"]"
        }
    }

    class MakeViewHolder(binding: ItemMakeBinding): RecyclerView.ViewHolder(binding.root) {
        private val text: TextView = binding.text

        fun bind(listItem: UiModel.MakeItem) {
            text.text = listItem.name
        }
    }

}