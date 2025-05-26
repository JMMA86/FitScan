package icesi.edu.co.fitscan.domain.usecases

import icesi.edu.co.fitscan.domain.model.ProgressPhoto

interface IFetchProgressPhotosUseCase {
    suspend operator fun invoke(customerId: String): Result<List<ProgressPhoto>>
}
