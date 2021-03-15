package com.igorpi25.vincoder.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.igorpi25.vincoder.R
import com.igorpi25.vincoder.interfaces.RetrofitServices
import com.igorpi25.vincoder.adapters.ManufacturersListAdapter
import com.igorpi25.vincoder.common.Common
import com.igorpi25.vincoder.databinding.MainFragmentBinding
import com.igorpi25.vincoder.model.Manufacturer
import com.igorpi25.vincoder.retrofit.ServerResponse
import com.igorpi25.vincoder.ui.details.DetailsFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainFragment : Fragment(R.layout.main_fragment) {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var mainFragmentBinding: MainFragmentBinding? = null

    private lateinit var viewModel: MainViewModel
    lateinit var mService: RetrofitServices
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: ManufacturersListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = MainFragmentBinding.bind(view)
        mainFragmentBinding = binding

        binding.recyclerMovieList.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        binding.recyclerMovieList.layoutManager = layoutManager

        mService = Common.retrofitService

        getAllManufacturers()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroyView() {
        // Consider not storing the binding instance in a field, if not needed.
        mainFragmentBinding = null
        super.onDestroyView()
    }

    private fun getAllManufacturers() {
        mService.getAllManufacturers(page = 2).enqueue(object : Callback<ServerResponse<Manufacturer>> {
            override fun onFailure(call: Call<ServerResponse<Manufacturer>>, t: Throwable) {

            }

            override fun onResponse(call: Call<ServerResponse<Manufacturer>>, response: Response<ServerResponse<Manufacturer>>) {
                adapter = ManufacturersListAdapter(
                    context = context!!,
                    manufacturerList = ((response.body() as ServerResponse<Manufacturer>).results as MutableList<Manufacturer>?)!!
                ) {

                    val action = MainFragmentDirections.actionMainToDetails(it!!)
                    findNavController().navigate(action)

                }
                adapter.notifyDataSetChanged()
                mainFragmentBinding!!.recyclerMovieList.adapter = adapter
            }
        })
    }

}