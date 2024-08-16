package com.yourssu.summerhackathon.repository

import com.yourssu.summerhackathon.entity.Activity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ActivityRepository : JpaRepository<Activity, Long> {
    fun findByUserId(userId: Long): List<Activity>

    fun findAllByExerciseName(exerciseName: String): List<Activity>

    fun findAllByUserIdIn(userIds: List<Long>): List<Activity>

    @Query("select a from Activity a where a.user.id = :userId and year(a.startDate) = :year and month(a.startDate) = :month")
    fun findAllByUserIdAndYearAndMonth(
        @Param("userId") userId: Long?,
        @Param("year") year: Int,
        @Param("month") month: Int,
    ): List<Activity>
}
