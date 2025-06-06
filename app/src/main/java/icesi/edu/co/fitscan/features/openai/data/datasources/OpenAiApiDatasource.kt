package icesi.edu.co.fitscan.features.openai.data.datasources

import icesi.edu.co.fitscan.domain.model.ChatCompletionRequest
import icesi.edu.co.fitscan.domain.model.ChatCompletionResponse
import icesi.edu.co.fitscan.domain.model.VisionChatCompletionRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenAiApiDatasource {
    @POST("v1/chat/completions")
    suspend fun getChatCompletions(
        @Header("Authorization") authorization: String, // Bearer YOUR_API_KEY
        @Header("Content-Type") contentType: String = "application/json",
        @Body request: ChatCompletionRequest
    ): Response<ChatCompletionResponse>

    @POST("v1/chat/completions")
    suspend fun getVisionChatCompletions(
        @Header("Authorization") authorization: String, // Bearer YOUR_API_KEY
        @Header("Content-Type") contentType: String = "application/json",
        @Body request: VisionChatCompletionRequest
    ): Response<ChatCompletionResponse>
}
