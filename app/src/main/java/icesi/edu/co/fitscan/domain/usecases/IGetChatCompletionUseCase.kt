package icesi.edu.co.fitscan.domain.usecases

interface IGetChatCompletionUseCase {
    suspend operator fun invoke(prompt: String): Result<String>
}