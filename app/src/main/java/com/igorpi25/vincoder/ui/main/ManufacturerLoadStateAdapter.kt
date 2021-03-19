package com.igorpi25.vincoder.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.igorpi25.vincoder.databinding.ItemLoadstateLayoutBinding

class ManufacturerLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<ManufacturerLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = ItemLoadstateLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding, retry)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class LoadStateViewHolder(binding: ItemLoadstateLayoutBinding, val retry: () -> Unit): RecyclerView.ViewHolder(binding.root){
        val errorMessage: TextView = binding.errorMessage
        val progressBar: ProgressBar = binding.progressBar
        val buttonRetry: Button = binding.buttonRetry

        fun bind(loadState: LoadState) {

            buttonRetry.isVisible = loadState is LoadState.Error
            errorMessage.isVisible = loadState is LoadState.Error
            progressBar.isVisible = loadState is LoadState.Loading

            if (loadState is LoadState.Error){
                errorMessage.text = loadState.error.localizedMessage
            }

            buttonRetry.setOnClickListener {
                retry.invoke()
            }
        }
    }
}