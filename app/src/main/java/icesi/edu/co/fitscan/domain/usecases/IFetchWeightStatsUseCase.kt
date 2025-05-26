package icesi.edu.co.fitscan.domain.usecases

interface IFetchWeightStatsUseCase {
    suspend operator fun invoke(customerId: String): Result<List<Float>>
}
