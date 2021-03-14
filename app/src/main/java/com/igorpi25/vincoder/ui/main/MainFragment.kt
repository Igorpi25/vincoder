package com.igorpi25.vincoder.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.igorpi25.vincoder.R
import com.igorpi25.vincoder.interfaces.RetrofitServices
import com.igorpi25.vincoder.adapters.MyMovieAdapter
import com.igorpi25.vincoder.common.Common
import com.igorpi25.vincoder.databinding.MainFragmentBinding
import com.igorpi25.vincoder.model.Movie
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
    lateinit var adapter: MyMovieAdapter
    //lateinit var dialog: AlertDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = MainFragmentBinding.bind(view)
        mainFragmentBinding = binding


        binding.recyclerMovieList.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        binding.recyclerMovieList.layoutManager = layoutManager


        mService = Common.retrofitService

        getAllMovieList()
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

    private fun getAllMovieList() {
        //dialog.show()
        mService.getMovieList().enqueue(object : Callback<MutableList<Movie>> {
            override fun onFailure(call: Call<MutableList<Movie>>, t: Throwable) {

            }

            override fun onResponse(call: Call<MutableList<Movie>>, response: Response<MutableList<Movie>>) {
                adapter = MyMovieAdapter(context!!, response.body() as MutableList<Movie>)
                adapter.notifyDataSetChanged()
                mainFragmentBinding!!.recyclerMovieList.adapter = adapter

                //dialog.dismiss()
            }
        })
    }

}