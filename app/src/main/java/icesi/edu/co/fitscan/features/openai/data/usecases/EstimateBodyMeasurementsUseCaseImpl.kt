package icesi.edu.co.fitscan.features.openai.data.usecases

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import icesi.edu.co.fitscan.domain.model.EstimatedBodyMeasurements
import icesi.edu.co.fitscan.domain.repositories.IOpenAiRepository
import icesi.edu.co.fitscan.domain.usecases.IEstimateBodyMeasurementsUseCase
import icesi.edu.co.fitscan.features.common.utils.ImageUtils

class EstimateBodyMeasurementsUseCaseImpl(
    private val openAiRepository: IOpenAiRepository
) : IEstimateBodyMeasurementsUseCase {

    private val gson = Gson()
    
    override suspend fun invoke(
        context: Context,
        imageUri: Uri,
        height: Double,
        weight: Double,
        existingMeasurements: Map<String, Double>
    ): Result<EstimatedBodyMeasurements> {
        return try {
            // Validate inputs
            if (height <= 0 || weight <= 0) {
                return Result.failure(Exception("Invalid height or weight values"))
            }

            // Process image for AI analysis
            val imageBase64 = ImageUtils.processImageForAI(context, imageUri)
                ?: return Result.failure(Exception("Failed to process image. Please try taking another photo."))

            // Build prompt based on missing measurements
            val prompt = buildPrompt(height, weight, existingMeasurements)

            // Call OpenAI vision API with enhanced error handling
            val result = openAiRepository.getVisionChatCompletion(prompt, imageBase64)

            result.fold(
                onSuccess = { response ->
                    parseResponse(response)
                },
                onFailure = { error ->
                    // Enhanced error messages for different failure types
                    val enhancedError = when {
                        error.message?.contains("network", ignoreCase = true) == true -> 
                            Exception("Network error. Please check your internet connection and try again.")
                        error.message?.contains("timeout", ignoreCase = true) == true -> 
                            Exception("Request timed out. Please try again.")
                        error.message?.contains("unauthorized", ignoreCase = true) == true -> 
                            Exception("API authentication error. Please check your OpenAI API key.")
                        else -> Exception("AI estimation failed: ${error.message}")
                    }
                    Result.failure(enhancedError)
                }
            )
        } catch (e: Exception) {
            Result.failure(Exception("Unexpected error during measurement estimation: ${e.message}"))
        }
    }

    private fun buildPrompt(
        height: Double,
        weight: Double,
        existingMeasurements: Map<String, Double>
    ): String {
        val missingMeasurements = mutableListOf<String>()
        
        if (!existingMeasurements.containsKey("arms") || existingMeasurements["arms"] == 0.0) {
            missingMeasurements.add("arms_cm INTEGER")
        }
        if (!existingMeasurements.containsKey("chest") || existingMeasurements["chest"] == 0.0) {
            missingMeasurements.add("chest_cm INTEGER")
        }
        if (!existingMeasurements.containsKey("waist") || existingMeasurements["waist"] == 0.0) {
            missingMeasurements.add("waist_cm INTEGER")
        }
        if (!existingMeasurements.containsKey("hips") || existingMeasurements["hips"] == 0.0) {
            missingMeasurements.add("hips_cm INTEGER")
        }
        if (!existingMeasurements.containsKey("thighs") || existingMeasurements["thighs"] == 0.0) {
            missingMeasurements.add("thighs_cm INTEGER")
        }
        if (!existingMeasurements.containsKey("calves") || existingMeasurements["calves"] == 0.0) {
            missingMeasurements.add("calves_cm INTEGER")
        }

        val existingInfo = buildString {
            existingMeasurements.forEach { (key, value) ->
                if (value > 0.0) {
                    append("$key: ${value.toInt()}cm, ")
                }
            }
        }.trimEnd(',', ' ')

        val missingFields = missingMeasurements.joinToString(", ")

        return buildString {
            append("The person in the image is ${height.toInt()}cm tall and weighs ${weight.toInt()}kg. ")
            if (existingInfo.isNotEmpty()) {
                append("Known measurements: $existingInfo. ")
            }
            append("Analyze the body proportions in the image and provide estimated measurements for the following body parts in JSON format: { $missingFields }. ")
            append("Only provide the JSON response without any additional text or explanation. ")
            append("Ensure the measurements are realistic and proportional to the given height and weight.")
        }
    }

    private fun parseResponse(response: String): Result<EstimatedBodyMeasurements> {
        return try {
            // Clean the response to extract JSON
            val jsonString = response.trim()
                .removePrefix("```json")
                .removeSuffix("```")
                .trim()

            val estimatedMeasurements = gson.fromJson(jsonString, EstimatedBodyMeasurements::class.java)
            Result.success(estimatedMeasurements)
        } catch (e: JsonSyntaxException) {
            Result.failure(Exception("Failed to parse AI response: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Unexpected error parsing response: ${e.message}"))
        }
    }
}
