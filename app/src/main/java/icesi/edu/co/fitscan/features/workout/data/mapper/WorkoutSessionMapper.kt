package icesi.edu.co.fitscan.features.workout.data.mapper

import icesi.edu.co.fitscan.domain.model.WorkoutSession
import icesi.edu.co.fitscan.features.workout.data.dto.WorkoutSessionDto

class WorkoutSessionMapper {
    fun fromDto(dto: WorkoutSessionDto): WorkoutSession = WorkoutSession(
        id = dto.id,
        customerId = dto.customer_id,
        workoutId = dto.workout_id,
        startTime = dto.start_time,
        endTime = dto.end_time,
        caloriesBurned = dto.calories_burned,
        distanceKm = dto.distance_km,
        averageHeartRate = dto.average_heart_rate
    )

    fun toDto(domain: WorkoutSession): WorkoutSessionDto = WorkoutSessionDto(
        id = domain.id,
        customer_id = domain.customerId,
        workout_id = domain.workoutId,
        start_time = domain.startTime,
        end_time = domain.endTime,
        calories_burned = domain.caloriesBurned,
        distance_km = domain.distanceKm,
        average_heart_rate = domain.averageHeartRate
    )
}