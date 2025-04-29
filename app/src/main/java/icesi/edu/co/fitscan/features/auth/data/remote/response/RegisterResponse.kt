package icesi.edu.co.fitscan.features.auth.data.remote.response

import icesi.edu.co.fitscan.features.auth.data.remote.request.BodyMeasure
import icesi.edu.co.fitscan.features.auth.data.remote.request.Customer

data class CustomerResponseData(
    val data: Customer
)

data class BodyMeasureResponseData(
    val data: BodyMeasure
)