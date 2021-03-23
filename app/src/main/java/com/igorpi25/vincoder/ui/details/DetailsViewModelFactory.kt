package com.igorpi25.vincoder.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DetailsViewModelFactory(
    private val targetManufacturerId: Int
): ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create(modelClass:Class<T>): T {
        return DetailsViewModel(targetManufacturerId) as T
    }
}