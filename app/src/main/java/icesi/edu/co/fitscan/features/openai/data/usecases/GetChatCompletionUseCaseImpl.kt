package icesi.edu.co.fitscan.features.openai.data.usecases

import android.content.Context
import icesi.edu.co.fitscan.features.openai.data.repositories.IOpenAiRepositoryImpl
import icesi.edu.co.fitscan.domain.repositories.IOpenAiRepository
import icesi.edu.co.fitscan.domain.usecases.IGetChatCompletionUseCase

class GetChatCompletionUseCaseImpl(
    private val context: Context,
    private val repository: IOpenAiRepository = IOpenAiRepositoryImpl(context)
) : IGetChatCompletionUseCase {
    override suspend operator fun invoke(prompt: String): Result<String> {
        return repository.getChatCompletion(prompt)
    }
}
