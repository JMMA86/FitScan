package icesi.edu.co.fitscan.features.workout.domain.mapper

import icesi.edu.co.fitscan.features.workout.data.dto.WorkoutDto
import icesi.edu.co.fitscan.features.workout.domain.model.Workout
import icesi.edu.co.fitscan.features.workout.domain.model.WorkoutType
import java.text.SimpleDateFormat
import java.util.*

class WorkoutMapper {

    fun toDomain(dto: WorkoutDto): Workout {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val parsedDate = formatter.parse(dto.dateCreated) ?: Date()

        return Workout(
            id = UUID.fromString(dto.id),
            customerId = UUID.fromString(dto.customerId),
            name = dto.name,
            type = WorkoutType.valueOf(dto.type.uppercase()),
            durationMinutes = dto.durationMinutes,
            difficulty = dto.difficulty,
            dateCreated = parsedDate
        )
    }

    fun toDto(workout: Workout): WorkoutDto {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        return WorkoutDto(
            id = workout.id.toString(),
            customerId = workout.customerId.toString(),
            name = workout.name,
            type = workout.type.name,
            durationMinutes = workout.durationMinutes,
            difficulty = workout.difficulty,
            dateCreated = formatter.format(workout.dateCreated)
        )
    }
    
}
