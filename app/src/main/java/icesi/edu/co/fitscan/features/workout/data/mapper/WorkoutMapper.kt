package icesi.edu.co.fitscan.features.workout.data.mapper

import icesi.edu.co.fitscan.domain.model.Workout
import icesi.edu.co.fitscan.domain.model.WorkoutType
import icesi.edu.co.fitscan.features.workout.data.dto.WorkoutDto
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class WorkoutMapper {

    fun toDomain(dto: WorkoutDto): Workout {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val parsedDate = try {
            formatter.parse(dto.dateCreated) ?: Date()
        } catch (e: Exception) {
            Date()
        }

        return Workout(
            id = try {
                UUID.fromString(dto.id)
            } catch (e: Exception) {
                UUID.randomUUID()
            },
            customerId = try {
                UUID.fromString(dto.customer_id)
            } catch (e: Exception) {
                UUID.randomUUID()
            },
            name = dto.name ?: "Sin nombre",
            type = try {
                WorkoutType.valueOf(dto.type?.uppercase() ?: WorkoutType.Gym.name)
            } catch (e: Exception) {
                WorkoutType.Gym
            },
            durationMinutes = dto.duration_minutes,
            difficulty = dto.difficulty ?: "Sin nivel",
            dateCreated = parsedDate
        )
    }

    fun toDto(workout: Workout): WorkoutDto {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        return WorkoutDto(
            id = workout.id.toString(),
            customer_id = workout.customerId.toString(),
            name = workout.name,
            type = workout.type.name,
            duration_minutes = workout.durationMinutes,
            difficulty = workout.difficulty,
            dateCreated = formatter.format(workout.dateCreated)
        )
    }
}
