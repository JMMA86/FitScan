package icesi.edu.co.fitscan.features.common.ui.viewmodel

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow

object AppState {
    private lateinit var prefs: SharedPreferences

    val showHeader = MutableStateFlow(true)
    var token: String? = null
        private set
        
    var customerId: String? = null

    fun init(prefs: SharedPreferences) {
        this.prefs = prefs
        token = prefs.getString("token", null)
        customerId = prefs.getString("customerId", null)
    }

    fun saveToken(token: String) {
        this.token = token
        prefs.edit { putString("token", token) }
    }

    fun getToken(): String? {
        return prefs.getString("token", null)
    }

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
