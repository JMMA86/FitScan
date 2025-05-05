package icesi.edu.co.fitscan.features.common.ui.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow

object AppState {
    val showHeader = MutableStateFlow(false)
    var customerId: String? = null

    fun setHeaderVisible(newValue: Boolean) {
        showHeader.value = newValue
    }

    fun clear() {
        showHeader.value = false
        customerId = null
    }
}
