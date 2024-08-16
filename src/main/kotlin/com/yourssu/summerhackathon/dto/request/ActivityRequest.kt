package com.yourssu.summerhackathon.dto.request

data class ActivityRequest(
    val exerciseName: String,
    val goalFrequency: Int,
    val goalDuration: Int,
)
