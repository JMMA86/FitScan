package icesi.edu.co.fitscan.features.workout.domain.service

import icesi.edu.co.fitscan.features.workout.domain.data.remote.response.Workout

interface WorkoutListService {
    suspend fun getWorkouts(): List<Workout>
} 