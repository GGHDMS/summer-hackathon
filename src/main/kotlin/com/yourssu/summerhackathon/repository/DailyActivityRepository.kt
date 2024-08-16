package com.yourssu.summerhackathon.repository

import com.yourssu.summerhackathon.entity.DailyActivity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface DailyActivityRepository : JpaRepository<DailyActivity, Long> {
    @Query("select count(a) from DailyActivity a where a.id= :activityId and a.startTime < :now")
    fun countDailyActivityByActivityIdAndStartTimeBefore(
        @Param("activityId") activityId: Long,
        @Param("now") now: LocalDateTime = LocalDateTime.now(),
    ): Int

    @Query("select d from DailyActivity d where d.user.id = :userId and year(d.startTime) = :year and month(d.startTime) = :month")
    fun findAllByUserIdAndYearAndMonth(
        @Param("userId") userId: Long,
        @Param("year") year: Int,
        @Param("month") month: Int,
    ): List<DailyActivity>
}
