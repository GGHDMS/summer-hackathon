package com.yourssu.summerhackathon.controller

import com.yourssu.summerhackathon.dto.response.ExerciseResponse
import com.yourssu.summerhackathon.dto.response.ExercisesResponse
import com.yourssu.summerhackathon.service.ExerciseService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ExerciseController(
    private val exerciseService: ExerciseService,
) {
    @GetMapping("/exercises")
    fun getExercises(
        @RequestParam("name") name: String,
    ): ExercisesResponse {
        val exercises = exerciseService.searchExercise(name)
        return ExercisesResponse(
            exercises.map {
                ExerciseResponse(it.id, it.name)
            },
        )
    }
}
