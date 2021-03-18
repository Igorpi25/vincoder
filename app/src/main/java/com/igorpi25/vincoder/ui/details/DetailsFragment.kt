package com.igorpi25.vincoder.ui.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.igorpi25.vincoder.App
import com.igorpi25.vincoder.R
import com.igorpi25.vincoder.adapters.ManufacturersListAdapter
import com.igorpi25.vincoder.common.Common
import com.igorpi25.vincoder.databinding.DetailsFragmentBinding
import com.igorpi25.vincoder.db.AppDatabase
import com.igorpi25.vincoder.interfaces.RetrofitServices
import com.igorpi25.vincoder.model.Manufacturer
import com.igorpi25.vincoder.retrofit.ServerResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import toothpick.Toothpick
import javax.inject.Inject


class DetailsFragment : Fragment(R.layout.details_fragment) {
    companion object {
        fun newInstance() = DetailsFragment()
    }

    val args: DetailsFragmentArgs by navArgs()

    private var detailsFragmentBinding: DetailsFragmentBinding? = null

    lateinit var mService: RetrofitServices
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: ManufacturersListAdapter

    @Inject
    lateinit var db: AppDatabase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scope = Toothpick.openScopes("AppScope","ViewModelScope")
        Toothpick.inject(this, scope);

        val binding = DetailsFragmentBinding.bind(view)
        detailsFragmentBinding = binding

        binding.manufacturerId.text = args.manufacturerId.toString()

        mService = Common.retrofitService

        getManufacturerDetails()

        binding.buttonSave.setOnClickListener {
            lifecycleScope.launch {
                val manufacturerDao = db.manufacturerDao()
                manufacturerDao.insertAll(
                    com.igorpi25.vincoder.db.entity.Manufacturer(
                        args.manufacturerId,
                        binding.manufacturerName.text.toString(),
                        binding.manufacturerCountry.text.toString()
                    )
                )
            }
        }
    }

    override fun onDestroyView() {
        // Consider not storing the binding instance in a field, if not needed.
        detailsFragmentBinding = null
        super.onDestroyView()
    }

    private fun getManufacturerDetails() {
        mService.getManufacturerDetails(id = args.manufacturerId).enqueue(object :
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