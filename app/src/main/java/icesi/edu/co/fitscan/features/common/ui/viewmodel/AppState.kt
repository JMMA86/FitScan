package icesi.edu.co.fitscan.features.common.ui.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow

object AppState {
    val showHeader = MutableStateFlow(true)
    var token: String? = null
    var customerId: String? = null

    fun setHeaderVisible(newValue: Boolean) {
        showHeader.value = newValue
    }

    fun setAuthToken(newToken: String) {
        token = newToken
    }

    // Limpia los datos de sesión al cerrar sesión
    fun clear() {
        showHeader.value = false
        customerId = null
    }
}
