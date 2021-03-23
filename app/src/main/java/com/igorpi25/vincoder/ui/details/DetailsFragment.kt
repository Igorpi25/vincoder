package com.igorpi25.vincoder.ui.details

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.igorpi25.vincoder.R
import com.igorpi25.vincoder.databinding.ListFragmentBinding
import com.igorpi25.vincoder.ui.common.CommonLoadStateAdapter
import com.igorpi25.vincoder.ui.common.CommonPagingAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import toothpick.Toothpick


class DetailsFragment : Fragment(R.layout.list_fragment) {

    val args: DetailsFragmentArgs by navArgs()

    private var detailsFragmentBinding: ListFragmentBinding? = null

    lateinit var viewModel: DetailsViewModel

    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: CommonPagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scope = Toothpick.openScopes("AppScope")
        Toothpick.inject(this, scope)

        val binding = ListFragmentBinding.bind(view)
        detailsFragmentBinding = binding

        binding.recyclerView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager

        adapter = CommonPagingAdapter(
            ModelsComparator
        ) {
            Toast.makeText(context, "Id=" + it?.toString(), Toast.LENGTH_LONG).show()
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

        viewModel = ViewModelProvider(
            this,
            DetailsViewModelFactory(targetManufacturerId = args.manufacturerId)
        ).get(DetailsViewModel::class.java)

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            viewModel.flow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }

    }

    override fun onDestroyView() {
        // Consider not storing the binding instance in a field, if not needed.
        detailsFragmentBinding = null
        super.onDestroyView()
    }


}