package com.yourssu.summerhackathon.repository

import com.yourssu.summerhackathon.entity.DailyActivity
import org.springframework.data.jpa.repository.JpaRepository

interface DailyActivityRepository : JpaRepository<DailyActivity, Long> {
    fun countDailyActivityByActivityId(activityId: Long): Int
}
