package icesi.edu.co.fitscan.domain.repositories

interface IOpenAiRepository {
    suspend fun getChatCompletion(prompt: String): Result<String>
    suspend fun getVisionChatCompletion(prompt: String, imageBase64: String): Result<String>
}
