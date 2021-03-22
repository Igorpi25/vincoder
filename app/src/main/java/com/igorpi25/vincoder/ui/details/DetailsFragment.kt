package com.igorpi25.vincoder.ui.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.igorpi25.vincoder.R
import com.igorpi25.vincoder.databinding.DetailsFragmentBinding
import com.igorpi25.vincoder.db.AppDatabase
import com.igorpi25.vincoder.retrofit.RetrofitService
import com.igorpi25.vincoder.model.Manufacturer
import com.igorpi25.vincoder.model.ServerResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import toothpick.Toothpick
import javax.inject.Inject


class DetailsFragment : Fragment(R.layout.details_fragment) {

    val args: DetailsFragmentArgs by navArgs()

    private var detailsFragmentBinding: DetailsFragmentBinding? = null

    @Inject
    lateinit var mService: RetrofitService

    @Inject
    lateinit var db: AppDatabase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scope = Toothpick.openScopes("AppScope")
        Toothpick.inject(this, scope)

        val binding = DetailsFragmentBinding.bind(view)
        detailsFragmentBinding = binding

        binding.manufacturerId.text = args.manufacturerId.toString()

        getManufacturerDetails()

        binding.buttonSave.setOnClickListener {
            lifecycleScope.launch {
                val manufacturerDao = db.manufacturerDao()
                manufacturerDao.insertAll(
                    Manufacturer(
                        args.manufacturerId,
                        binding.manufacturerName.text.toString(),
                        binding.manufacturerCountry.text.toString(),
                        0
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
                detailsFragmentBinding!!.manufacturerName.text  = (response.body() as ServerResponse<Manufacturer>).results[0].name
                detailsFragmentBinding!!.manufacturerCountry.text  = (response.body() as ServerResponse<Manufacturer>).results[0].country
            }
        })
    }
}