package com.yourssu.summerhackathon.service

import com.yourssu.summerhackathon.dto.request.ActivityRequest
import com.yourssu.summerhackathon.dto.request.DailyActivityRequest
import com.yourssu.summerhackathon.dto.response.ActivityResponse
import com.yourssu.summerhackathon.entity.Activity
import com.yourssu.summerhackathon.entity.DailyActivity
import com.yourssu.summerhackathon.repository.ActivityRepository
import com.yourssu.summerhackathon.repository.DailyActivityRepository
import com.yourssu.summerhackathon.repository.ExerciseRepository
import com.yourssu.summerhackathon.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional(readOnly = true)
@Service
class ActivityService(
    private val userRepository: UserRepository,
    private val exerciseRepository: ExerciseRepository,
    private val activityRepository: ActivityRepository,
    private val dailyActivityRepository: DailyActivityRepository,
) {
    @Transactional
    fun createActivity(
        userId: Long,
        request: ActivityRequest,
    ) {
        val user = userRepository.findByIdOrNull(userId) ?: throw RuntimeException("User not found")
        val exercise =
            exerciseRepository.findByName(request.exerciseName) ?: throw RuntimeException("Exercise not found")

        activityRepository.save(
            Activity(
                user = user,
                exercise = exercise,
                startDate = LocalDateTime.now(),
                goalFrequency = request.goalFrequency,
                goalDuration = request.goalDuration,
            ),
        )
    }

    @Transactional
    fun addDailyActivity(
        userId: Long,
        activityId: Long,
        request: DailyActivityRequest,
    ) {
        val activity = activityRepository.findByIdOrNull(activityId) ?: throw RuntimeException("Activity not found")

        dailyActivityRepository.save(
            DailyActivity(
                activity = activity,
                date = LocalDateTime.now(),
            ),
        )
    }

    fun searchExercise(name: String): List<ActivityResponse> {
        val exercises = activityRepository.findAllByExerciseName(name)

        return exercises.map {
            ActivityResponse(
                activityId = it.id,
                exerciseName = it.exercise.name,
                userName = it.user.name,
                exercise = it.exercise,
                startDate = it.startDate,
                goalFrequency = it.goalFrequency,
                goalDuration = it.goalDuration,
                goalPercent = calculateAchievementRate(it),
            )
        }
    }

    fun calculateAchievementRate(activity: Activity): Double {
        val totalTarget = activity.goalFrequency * activity.goalDuration * 4
        val completedSessions = dailyActivityRepository.countDailyActivityByActivityId(activity.id)
        return (completedSessions.toDouble() / totalTarget) * 100
    }
}
