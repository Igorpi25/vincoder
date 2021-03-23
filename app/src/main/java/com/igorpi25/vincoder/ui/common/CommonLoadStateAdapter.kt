package com.igorpi25.vincoder.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.igorpi25.vincoder.databinding.ItemLoadstateBinding

class CommonLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<CommonLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = ItemLoadstateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.buttonRetry.setOnClickListener {
            retry.invoke()
        }
        return LoadStateViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class LoadStateViewHolder(binding: ItemLoadstateBinding): RecyclerView.ViewHolder(binding.root){
        private val errorMessage: TextView = binding.textError
        private val progressBar: ProgressBar = binding.progressBar
        private val buttonRetry: Button = binding.buttonRetry

        fun bind(loadState: LoadState) {

            buttonRetry.isVisible = loadState is LoadState.Error
            errorMessage.isVisible = loadState is LoadState.Error
            progressBar.isVisible = loadState is LoadState.Loading

            if (loadState is LoadState.Error){
                errorMessage.text = loadState.error.localizedMessage
            }
        }
    }
}