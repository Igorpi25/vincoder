package com.igorpi25.vincoder.ui.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.igorpi25.vincoder.R
import com.igorpi25.vincoder.adapters.ManufacturersListAdapter
import com.igorpi25.vincoder.common.Common
import com.igorpi25.vincoder.databinding.DetailsFragmentBinding
import com.igorpi25.vincoder.interfaces.RetrofitServices
import com.igorpi25.vincoder.model.Manufacturer
import com.igorpi25.vincoder.retrofit.ServerResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsFragment(val manufacturerId:Int) : Fragment(R.layout.details_fragment) {
    companion object {
        fun newInstance(manufacturerId: Int) = DetailsFragment(manufacturerId)
    }
    private var detailsFragmentBinding: DetailsFragmentBinding? = null

    lateinit var mService: RetrofitServices
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: ManufacturersListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = DetailsFragmentBinding.bind(view)
        detailsFragmentBinding = binding

        binding.manufacturerId.text = manufacturerId.toString()

        mService = Common.retrofitService

        getManufacturerDetails()
    }

    override fun onDestroyView() {
        // Consider not storing the binding instance in a field, if not needed.
        detailsFragmentBinding = null
        super.onDestroyView()
    }

    private fun getManufacturerDetails() {
        mService.getManufacturerDetails(id = manufacturerId).enqueue(object :
            Callback<ServerResponse<Manufacturer>> {
            override fun onFailure(call: Call<ServerResponse<Manufacturer>>, t: Throwable) {

            }

            override fun onResponse(call: Call<ServerResponse<Manufacturer>>, response: Response<ServerResponse<Manufacturer>>) {
                detailsFragmentBinding!!.manufacturerName.text  = (response.body() as ServerResponse<Manufacturer>).results!![0].name
                detailsFragmentBinding!!.manufacturerCountry.text  = (response.body() as ServerResponse<Manufacturer>).results!![0].country
            }
        })
    }
}