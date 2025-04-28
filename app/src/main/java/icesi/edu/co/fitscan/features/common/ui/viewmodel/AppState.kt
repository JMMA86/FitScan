package icesi.edu.co.fitscan.features.common.ui.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow

object AppState {
    val showHeader = MutableStateFlow(false)

    fun setHeaderVisible(newValue: Boolean) {
        showHeader.value = newValue
    }
}
