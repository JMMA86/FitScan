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
    }override suspend fun getMuscleGroupProgress(customerId: String): Result<List<MuscleGroupProgress>> {
        return try {
            // Calculate date for last week (7 days ago)
            val lastWeekDate = LocalDateTime.now().minusDays(7)
            val fromDate = lastWeekDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            
            // Get all completed exercises with exercise details and muscle groups (last week only)
            val completedExercisesResponse = remoteDataSource.getCompletedExercisesWithMuscleGroups(customerId, fromDate)
            val completedExercises = completedExercisesResponse.data
            
            // Get all muscle groups
            val muscleGroupsResponse = remoteDataSource.getAllMuscleGroups()
            val allMuscleGroups = muscleGroupsResponse.data.map { MuscleGroupMapper.toDomain(it) }
            
            // Initialize muscle group stats map
            val muscleGroupStats = allMuscleGroups.associate { 
                it.id to MutableMuscleGroupStats(it, 0, 0, 0.0, 0.0, mutableSetOf())
            }.toMutableMap()
            
            // Group completed exercises by exercise_id to avoid repetitive queries
            val exercisesById = completedExercises.groupBy { it.exercise_id?.id }            // Cache for secondary muscle groups per exercise
            val secondaryMuscleGroupsCache = mutableMapOf<String, List<icesi.edu.co.fitscan.features.statistics.data.dto.SecondaryMuscleGroupDto>>()            // Pre-fetch secondary muscle groups for each unique exercise
            for (exerciseId in exercisesById.keys.filterNotNull()) {
                try {
                    val secondaryResponse = remoteDataSource.getSecondaryMuscleGroups(exerciseId)
                    secondaryMuscleGroupsCache[exerciseId] = secondaryResponse.data
                } catch (e: Exception) {
                    // Log the error but continue processing
                    secondaryMuscleGroupsCache[exerciseId] = emptyList()
                }
            }            // Process each completed exercise asynchronously
            coroutineScope {
                completedExercises.map { completedExercise ->
                    async {
                        try {
                            val sets = completedExercise.sets ?: 0
                            val reps = completedExercise.reps ?: 0
                            val weightKg = completedExercise.weight_kg ?: 0
                            val exercise = completedExercise.exercise_id
                            if (exercise?.id != null) {
                                // Add to primary muscle group (full sets)
                                exercise.primaryMuscleGroupId?.id?.let { primaryMuscleGroupId ->
                                    try {
                                        val muscleGroupId = java.util.UUID.fromString(primaryMuscleGroupId)
                                        muscleGroupStats[muscleGroupId]?.let { stats ->
                                            synchronized(stats) {
                                                stats.totalSets += sets
                                                stats.totalReps += reps
                                                stats.totalVolume += (sets * reps * weightKg).toDouble()
                                                stats.totalWeight += (sets * weightKg).toDouble()
                                                stats.uniqueExercises.add(exercise.id!!)
                                            }
                                        }
                                    } catch (e: IllegalArgumentException) {
                                        // Invalid UUID format, skip this muscle group
                                    }
                                }

                                // Add to secondary muscle groups (also full sets, not half)
                                val secondaryList = secondaryMuscleGroupsCache[exercise.id] ?: emptyList()
                                for (secondaryMuscleGroup in secondaryList) {
                                    try {
                                        val muscleGroupId = java.util.UUID.fromString(secondaryMuscleGroup.muscle_group_id.id)
                                        muscleGroupStats[muscleGroupId]?.let { stats ->
                                            synchronized(stats) {
                                                stats.totalSets += sets // Full sets for secondary muscle groups too
                                                stats.totalReps += reps
                                                stats.totalVolume += (sets * reps * weightKg).toDouble()
                                                stats.totalWeight += (sets * weightKg).toDouble()
                                                stats.uniqueExercises.add(exercise.id!!)
                                            }
                                        }
                                    } catch (e: IllegalArgumentException) {
                                        // Invalid UUID format, skip this muscle group
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            // Log error but continue processing other exercises
                        }
                    }
                }.awaitAll()
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
                    totalExercises = stats.uniqueExercises.size, // Count of unique exercises
                    completedThisWeek = stats.totalSets, // Total sets this week
                    avgWeightLifted = if (stats.totalSets > 0) (stats.totalWeight / stats.totalSets).toFloat() else 0f,
                    totalVolume = stats.totalVolume.toFloat()
                )
            }.sortedByDescending { it.completedThisWeek }
            
            Result.success(progressList)
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
