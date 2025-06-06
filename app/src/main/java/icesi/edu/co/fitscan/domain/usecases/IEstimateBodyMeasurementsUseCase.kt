package icesi.edu.co.fitscan.domain.usecases

import android.content.Context
import android.net.Uri
import icesi.edu.co.fitscan.domain.model.EstimatedBodyMeasurements

interface IEstimateBodyMeasurementsUseCase {
    suspend operator fun invoke(
        context: Context,
        imageUri: Uri,
        height: Double,
        weight: Double,
        existingMeasurements: Map<String, Double>
    ): Result<EstimatedBodyMeasurements>
}
