package com.yourssu.summerhackathon.repository

import com.yourssu.summerhackathon.entity.Activity
import org.springframework.data.jpa.repository.JpaRepository

interface ActivityRepository : JpaRepository<Activity, Long> {
    fun findByUserId(userId: Long): List<Activity>

    fun findAllByExerciseName(exerciseName: String): List<Activity>

    fun findAllByExerciseNameAndUserIdIn(
        exerciseName: String,
        userIds: List<Long>,
    ): List<Activity>
}
