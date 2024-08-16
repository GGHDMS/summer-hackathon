package com.yourssu.summerhackathon.repository

import com.yourssu.summerhackathon.entity.Badge
import org.springframework.data.jpa.repository.JpaRepository

interface BadgeRepository : JpaRepository<Badge, Long> {
    fun findByUserId(userId: Long): List<Badge>
}
