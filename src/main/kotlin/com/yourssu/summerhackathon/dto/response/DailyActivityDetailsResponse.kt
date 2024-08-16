package com.yourssu.summerhackathon.dto.response

import java.time.LocalDateTime

data class DailyActivityDetailsResponse(
    val activityId: Long,
    val exerciseName: String,
    val userName: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
)
