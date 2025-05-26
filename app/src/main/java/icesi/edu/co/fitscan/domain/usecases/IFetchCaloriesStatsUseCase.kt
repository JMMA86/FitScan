package icesi.edu.co.fitscan.domain.usecases

interface IFetchCaloriesStatsUseCase {
    suspend operator fun invoke(customerId: String): Result<List<Float>>
}
