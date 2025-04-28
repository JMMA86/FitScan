package icesi.edu.co.fitscan.features.auth.domain

object TempDataHolder {
    var customer_id: String? = null

    fun clear() {
        customer_id = null
    }
}