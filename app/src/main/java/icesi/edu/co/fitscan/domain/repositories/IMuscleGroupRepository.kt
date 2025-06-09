package icesi.edu.co.fitscan.domain.repositories

import icesi.edu.co.fitscan.domain.model.MuscleGroup
import icesi.edu.co.fitscan.domain.model.MuscleGroupProgress
import java.util.UUID

interface IMuscleGroupRepository {
    suspend fun getAllMuscleGroups(): Result<List<MuscleGroup>>
    suspend fun getMuscleGroupProgress(customerId: String): Result<List<MuscleGroupProgress>>
    suspend fun getMuscleGroupProgressById(customerId: String, muscleGroupId: UUID): Result<MuscleGroupProgress>
}
