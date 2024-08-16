package com.yourssu.summerhackathon.dto.response

class BadgesResponse(
    val userId: Long,
    val userName: String,
    val badges: List<BadgeResponse>,
)
