package com.yourssu.summerhackathon.repository

import com.yourssu.summerhackathon.entity.DailyActivity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface DailyActivityRepository : JpaRepository<DailyActivity, Long> {
    @Query("select count(a) from Activity a where a.id= :activityId and a.startDate < :now")
    fun countDailyActivityByActivityIdAndStartTimeBefore(
        @Param("activityId") activityId: Long,
        @Param("now") now: LocalDateTime = LocalDateTime.now(),
    ): Int
}
