package com.igorpi25.vincoder.ui.main

import com.igorpi25.vincoder.model.Manufacturer

sealed class UiModel {
    data class ManufacturerItem(val manufacturer: Manufacturer): UiModel()
    data class SeparatorItem(val page: Int): UiModel()
}