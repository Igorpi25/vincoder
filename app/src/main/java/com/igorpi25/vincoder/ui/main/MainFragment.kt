package com.igorpi25.vincoder.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.igorpi25.vincoder.R
import com.igorpi25.vincoder.databinding.ListFragmentBinding
import com.igorpi25.vincoder.ui.common.CommonLoadStateAdapter
import com.igorpi25.vincoder.ui.common.CommonPagingAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainFragment : Fragment(R.layout.list_fragment) {

    private var mainFragmentBinding: ListFragmentBinding? = null

    lateinit var viewModel: MainViewModel

    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: CommonPagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = ListFragmentBinding.bind(view)
        mainFragmentBinding = binding

        binding.recyclerView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager

        adapter = CommonPagingAdapter(
            ManufacturersComparator
        ) {
            val action = MainFragmentDirections.actionMainToDetails(it!!)
            findNavController().navigate(action)
        }

        binding.recyclerView.adapter = adapter.withLoadStateFooter(
            footer = CommonLoadStateAdapter { adapter.retry() }
        )

        binding.buttonRetry.setOnClickListener {
            adapter.retry()
        }

        adapter.addLoadStateListener { loadState ->
            if (loadState.mediator?.refresh is LoadState.Loading) {
                binding.recyclerView.isVisible = false
                binding.progressBar.isVisible = true
                binding.errorLayout.isVisible = false

            } else if (loadState.mediator?.refresh is LoadState.Error) {
                binding.recyclerView.isVisible = false
                binding.progressBar.isVisible = false
                binding.errorLayout.isVisible = true

                binding.textError.text = (loadState.mediator?.refresh as LoadState.Error).error.message

            } else {
                binding.recyclerView.isVisible = true
                binding.progressBar.isVisible = false
                binding.errorLayout.isVisible = false
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            viewModel.flow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }

    }

    override fun onDestroyView() {
        // Consider not storing the binding instance in a field, if not needed.
        mainFragmentBinding = null
        super.onDestroyView()
    }

}