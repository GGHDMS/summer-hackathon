package com.yourssu.summerhackathon.dto.response

import java.time.LocalDateTime

data class DailyActivityResponse(
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val exercise: String,
)
