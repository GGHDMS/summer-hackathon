package com.yourssu.summerhackathon.dto.request

import java.time.LocalDateTime

class DailyActivityRequest(
    val name: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val location: String,
    val memo: String,
)
