package com.yourssu.summerhackathon.service

import com.yourssu.summerhackathon.repository.ExerciseRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class ExerciseService(
    private val exerciseRepository: ExerciseRepository,
) {
    fun searchExercise(name: String) = exerciseRepository.findByNameContaining(name)
}
