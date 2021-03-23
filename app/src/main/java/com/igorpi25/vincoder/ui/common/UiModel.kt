package com.igorpi25.vincoder.ui.common

import com.igorpi25.vincoder.model.Manufacturer
import com.igorpi25.vincoder.model.Model

sealed class UiModel {
    data class ManufacturerItem(val manufacturer: Manufacturer): UiModel()
    data class ModelItem(val model: Model): UiModel()
    data class PageItem(val page: Int): UiModel()
    data class MakeItem(val name: String): UiModel()
}