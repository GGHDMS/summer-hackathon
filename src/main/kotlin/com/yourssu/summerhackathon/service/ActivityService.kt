package com.yourssu.summerhackathon.service

import com.yourssu.summerhackathon.dto.request.ActivityRequest
import com.yourssu.summerhackathon.dto.request.DailyActivityRequest
import com.yourssu.summerhackathon.dto.response.ActivityResponse
import com.yourssu.summerhackathon.dto.response.DailyActivityDetailsResponse
import com.yourssu.summerhackathon.dto.response.DailyActivityResponse
import com.yourssu.summerhackathon.entity.Activity
import com.yourssu.summerhackathon.entity.DailyActivity
import com.yourssu.summerhackathon.repository.ActivityRepository
import com.yourssu.summerhackathon.repository.DailyActivityRepository
import com.yourssu.summerhackathon.repository.ExerciseRepository
import com.yourssu.summerhackathon.repository.FriendRepository
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
    private val friendRepository: FriendRepository,
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
                startTime = request.startTime,
                endTime = request.endTime,
                location = request.location,
                memo = request.memo,
                user = activity.user,
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

    fun searchExercise(userId: Long): List<ActivityResponse> {
        val users = friendRepository.findByFollower(userId)

        val exercises = activityRepository.findAllByUserIdIn(users.map { it.id })

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

    fun findMonthActivity(
        userId: Long,
        year: Int,
        month: Int,
    ): List<DailyActivityResponse> {
        val activities = dailyActivityRepository.findAllByUserIdAndYearAndMonth(userId, year, month)

        return activities.map {
            DailyActivityResponse(
                startTime = it.startTime,
                endTime = it.endTime,
                exercise = it.activity.exercise.name,
            )
        }
    }

    fun getDailyActivity(dailyActivityId: Long): DailyActivityDetailsResponse {
        val dailyActivity =
            dailyActivityRepository.findByIdOrNull(dailyActivityId)
                ?: throw RuntimeException("Daily Activity not found")

        return DailyActivityDetailsResponse(
            activityId = dailyActivity.activity.id,
            exerciseName = dailyActivity.activity.exercise.name,
            userName = dailyActivity.user.name,
            startTime = dailyActivity.startTime,
            endTime = dailyActivity.endTime,
        )
    }

    fun calculateAchievementRate(activity: Activity): Double {
        val totalTarget = activity.goalFrequency * activity.goalDuration * 4
        val completedSessions = dailyActivityRepository.countDailyActivityByActivityIdAndStartTimeBefore(activity.id)
        println(completedSessions)
        return (completedSessions.toDouble() / totalTarget) * 100
    }
}
