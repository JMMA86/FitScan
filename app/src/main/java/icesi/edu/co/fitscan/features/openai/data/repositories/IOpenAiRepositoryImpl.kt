package icesi.edu.co.fitscan.features.openai.data.repositories

import android.content.Context
import icesi.edu.co.fitscan.features.openai.data.datasources.OpenAiRemoteDataSource
import icesi.edu.co.fitscan.domain.repositories.IOpenAiRepository

class IOpenAiRepositoryImpl(
    private val context: Context,
    private val remoteDataSource: OpenAiRemoteDataSource = OpenAiRemoteDataSource(context)
) : IOpenAiRepository {
    
    override suspend fun getChatCompletion(prompt: String): Result<String> {
        val result = remoteDataSource.getChatCompletions(prompt)
        
        return result.map { response ->
            // Extrae el texto de la respuesta
            response.choices?.firstOrNull()?.message?.content 
                ?: throw Exception("No se pudo obtener una respuesta de la IA")
        }
    }
}
