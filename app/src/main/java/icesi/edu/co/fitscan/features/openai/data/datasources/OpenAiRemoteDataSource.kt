package icesi.edu.co.fitscan.features.openai.data.datasources

import android.content.Context
import icesi.edu.co.fitscan.R
import icesi.edu.co.fitscan.domain.model.ChatCompletionRequest
import icesi.edu.co.fitscan.domain.model.ChatCompletionResponse
import icesi.edu.co.fitscan.domain.model.VisionChatCompletionRequest
import icesi.edu.co.fitscan.features.openai.data.remote.OpenAiApiProvider
import icesi.edu.co.fitscan.features.openai.data.dto.MessageDto
import icesi.edu.co.fitscan.features.openai.data.dto.VisionMessageDto
import icesi.edu.co.fitscan.features.openai.data.dto.ContentDto
import icesi.edu.co.fitscan.features.openai.data.dto.ImageUrlDto
import java.util.Properties

class OpenAiRemoteDataSource(
    private val context: Context,
    private val openAiApiDatasource: OpenAiApiDatasource = OpenAiApiProvider.provideOpenAiApiService()
) {
    companion object {
        private const val AUTHORIZATION_PREFIX = "Bearer "
        private const val DEFAULT_MODEL = "gpt-4o-mini"
    }

    private fun readOpenAiProperties(context: Context): Properties {
        val props = Properties()
        context.resources.openRawResource(R.raw.openai).use { inputStream ->
            props.load(inputStream)
        }
        return props
    }

    // Inicialización perezosa de propiedades para usar después de que
    // el constructor se haya completado
    private val props by lazy { readOpenAiProperties(context) }
    private val apiKey by lazy { props.getProperty("OPENAI_API_KEY") }
      suspend fun getChatCompletions(
        prompt: String,
        model: String = DEFAULT_MODEL
    ): Result<ChatCompletionResponse> {
        return try {
            val authorization = AUTHORIZATION_PREFIX + apiKey
            val messages = listOf(MessageDto(role = "user", content = prompt))
            val request = ChatCompletionRequest(model = model, messages = messages)
            
            val response = openAiApiDatasource.getChatCompletions(authorization, request = request)
            
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Response body is empty"))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(Exception("API Error: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getVisionChatCompletions(
        prompt: String,
        imageBase64: String,
        model: String = "gpt-4o-mini"
    ): Result<ChatCompletionResponse> {
        return try {
            val authorization = AUTHORIZATION_PREFIX + apiKey
            val messages = listOf(
                VisionMessageDto(
                    role = "user",
                    content = listOf(
                        ContentDto(type = "text", text = prompt),
                        ContentDto(
                            type = "image_url",
                            image_url = ImageUrlDto(url = "data:image/jpeg;base64,$imageBase64")
                        )
                    )
                )
            )
            val request = VisionChatCompletionRequest(model = model, messages = messages)
            
            val response = openAiApiDatasource.getVisionChatCompletions(authorization, request = request)
            
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Response body is empty"))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(Exception("API Error: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
