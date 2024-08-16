package com.yourssu.summerhackathon.controller

import com.yourssu.summerhackathon.annotation.Auth
import com.yourssu.summerhackathon.dto.AuthUser
import com.yourssu.summerhackathon.dto.request.ActivityRequest
import com.yourssu.summerhackathon.dto.request.DailyActivityRequest
import com.yourssu.summerhackathon.dto.response.ActivitiesResponse
import com.yourssu.summerhackathon.service.ActivityService
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ActivityController(
    private val activityService: ActivityService,
) {
    @PostMapping("/activities")
    fun postActivity(
        @Parameter(hidden = true) @Auth auth: AuthUser,
        @RequestBody request: ActivityRequest,
    ) {
        activityService.createActivity(auth.userId, request)
    }

    @PostMapping("/activities/{activity-id}/daily")
    fun createDailyActivity(
        @Parameter(hidden = true) @Auth auth: AuthUser,
        @PathVariable("activity-id") activityId: Long,
        @RequestBody request: DailyActivityRequest,
    ) {
        activityService.addDailyActivity(auth.userId, activityId, request)
    }

    @GetMapping("/activities/exercise")
    fun searchExercise(
        @RequestParam name: String,
    ): ActivitiesResponse = ActivitiesResponse(activityService.searchExercise(name))

    @GetMapping("/activities/month/my")
    fun searchMonthActivities(
        @Parameter(hidden = true) @Auth auth: AuthUser,
        @RequestParam("year") year: Int,
        @RequestParam("month") month: Int,
    ): ActivitiesResponse = ActivitiesResponse(activityService.findMonthActivity(auth.userId, year, month))

    @GetMapping("/activities/month/friends/{friend-id}")
    fun searchMonthActivitiesFriend(
        @PathVariable("friend-id") friendId: Long,
        @RequestParam("year") year: Int,
        @RequestParam("month") month: Int,
    ): ActivitiesResponse = ActivitiesResponse(activityService.findMonthActivity(friendId, year, month))
}
