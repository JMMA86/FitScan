package icesi.edu.co.fitscan.features.statistics.data.repositories

import icesi.edu.co.fitscan.domain.model.MuscleGroup
import icesi.edu.co.fitscan.domain.model.MuscleGroupProgress
import icesi.edu.co.fitscan.domain.repositories.IMuscleGroupRepository
import icesi.edu.co.fitscan.features.statistics.data.remote.StatisticsRemoteDataSource
import icesi.edu.co.fitscan.features.statistics.data.mapper.MuscleGroupMapper
import icesi.edu.co.fitscan.features.statistics.data.mapper.MuscleGroupProgressMapper
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class MuscleGroupRepositoryImpl(
    private val remoteDataSource: StatisticsRemoteDataSource
) : IMuscleGroupRepository {    override suspend fun getAllMuscleGroups(): Result<List<MuscleGroup>> {
        return try {
            val response = remoteDataSource.getAllMuscleGroups()
            val muscleGroups = response.data.map { dto ->
                MuscleGroupMapper.toDomain(dto)
            }
            Result.success(muscleGroups)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }    override suspend fun getMuscleGroupProgress(customerId: String): Result<List<MuscleGroupProgress>> {
        return try {
            // Calculate date for last week (7 days ago)
            val lastWeekDate = LocalDateTime.now().minusDays(7)
            val fromDate = lastWeekDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            
            // Fetch all data in parallel to minimize wait time
            coroutineScope {
                val completedExercisesDeferred = async {
                    remoteDataSource.getCompletedExercisesWithMuscleGroups(customerId, fromDate)
                }
                val muscleGroupsDeferred = async {
                    remoteDataSource.getAllMuscleGroups()
                }
                val allSecondaryMuscleGroupsDeferred = async {
                    remoteDataSource.getAllSecondaryMuscleGroups()
                }
                
                // Wait for all API calls to complete
                val completedExercisesResponse = completedExercisesDeferred.await()
                val muscleGroupsResponse = muscleGroupsDeferred.await()
                val allSecondaryMuscleGroupsResponse = allSecondaryMuscleGroupsDeferred.await()
                
                val completedExercises = completedExercisesResponse.data
                val allMuscleGroups = muscleGroupsResponse.data.map { MuscleGroupMapper.toDomain(it) }
                
                // Build a map of exercise_id -> list of secondary muscle groups for fast lookup
                val secondaryMuscleGroupsMap = allSecondaryMuscleGroupsResponse.data
                    .groupBy { it.exercise_id }
                    .mapValues { (_, secondaryList) -> 
                        secondaryList.map { it.muscle_group_id }
                    }
                
                // Initialize muscle group stats map
                val muscleGroupStats = allMuscleGroups.associate { 
                    it.id to MutableMuscleGroupStats(it, 0, 0, 0.0, 0.0, mutableSetOf())
                }.toMutableMap()
                
                // Process completed exercises efficiently
                for (completedExercise in completedExercises) {
                    val sets = completedExercise.sets ?: 0
                    val reps = completedExercise.reps ?: 0
                    val weightKg = completedExercise.weight_kg ?: 0
                    val exercise = completedExercise.exercise_id
                    
                    if (exercise?.id != null && sets > 0) {
                        // Add to primary muscle group (full sets)
                        exercise.primaryMuscleGroupId?.id?.let { primaryMuscleGroupId ->
                            try {
                                val muscleGroupId = java.util.UUID.fromString(primaryMuscleGroupId)
                                muscleGroupStats[muscleGroupId]?.let { stats ->
                                    stats.totalSets += sets
                                    stats.totalReps += reps
                                    stats.totalVolume += (sets * reps * weightKg).toDouble()
                                    stats.totalWeight += (sets * weightKg).toDouble()
                                    stats.uniqueExercises.add(exercise.id)
                                }
                            } catch (e: IllegalArgumentException) {
                                // Invalid UUID format, skip this muscle group
                            }
                        }

                        // Add to secondary muscle groups using the pre-built map
                        secondaryMuscleGroupsMap[exercise.id]?.forEach { secondaryMuscleGroup ->
                            try {
                                val muscleGroupId = java.util.UUID.fromString(secondaryMuscleGroup.id)
                                muscleGroupStats[muscleGroupId]?.let { stats ->
                                    stats.totalSets += sets
                                    stats.totalReps += reps
                                    stats.totalVolume += (sets * reps * weightKg).toDouble()
                                    stats.totalWeight += (sets * weightKg).toDouble()
                                    stats.uniqueExercises.add(exercise.id)
                                }
                            } catch (e: IllegalArgumentException) {
                                // Invalid UUID format, skip this muscle group
                            }
                        }
                    }
                }
                
                // Calculate total sets across all muscle groups for percentage calculation
                val totalSetsAllMuscleGroups = muscleGroupStats.values.maxOfOrNull { it.totalSets } ?: 0
                
                // Convert to MuscleGroupProgress objects
                val progressList = muscleGroupStats.values.map { stats ->
                    val progressPercentage = if (totalSetsAllMuscleGroups > 0) {
                        (stats.totalSets.toFloat() / totalSetsAllMuscleGroups.toFloat())
                    } else {
                        0f
                    }
                    MuscleGroupProgress(
                        muscleGroup = stats.muscleGroup,
                        progressPercentage = progressPercentage,
                        totalExercises = stats.uniqueExercises.size,
                        completedThisWeek = stats.totalSets,
                        avgWeightLifted = if (stats.totalSets > 0) (stats.totalWeight / stats.totalSets).toFloat() else 0f,
                        totalVolume = stats.totalVolume.toFloat()
                    )
                }.sortedByDescending { it.completedThisWeek }
                
                Result.success(progressList)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMuscleGroupProgressById(customerId: String, muscleGroupId: UUID): Result<MuscleGroupProgress> {
        return try {
            // Get all progress and filter by muscle group ID
            val allProgress = getMuscleGroupProgress(customerId)
            allProgress.fold(
                onSuccess = { progressList ->
                    val specificProgress = progressList.find { it.muscleGroup.id == muscleGroupId }
                    if (specificProgress != null) {
                        Result.success(specificProgress)
                    } else {
                        Result.failure(Exception("Muscle group progress not found"))
                    }
                },
                onFailure = { exception ->
                    Result.failure(exception)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

// Helper data class for calculating muscle group statistics
private data class MutableMuscleGroupStats(
    val muscleGroup: MuscleGroup,
    var totalSets: Int,
    var totalReps: Int,
    var totalVolume: Double,
    var totalWeight: Double,
    val uniqueExercises: MutableSet<String>
)
