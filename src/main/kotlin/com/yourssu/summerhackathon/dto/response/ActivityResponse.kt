package com.yourssu.summerhackathon.dto.response

import com.yourssu.summerhackathon.entity.Exercise
import java.time.LocalDateTime

data class ActivityResponse(
    val activityId: Long,
    val exerciseName: String,
    val userName: String,
    val exercise: Exercise,
    val startDate: LocalDateTime,
    val goalFrequency: Int,
    val goalDuration: Int,
    val goalPercent: Double,
)
