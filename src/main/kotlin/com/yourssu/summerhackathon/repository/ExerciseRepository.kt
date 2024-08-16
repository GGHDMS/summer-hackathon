package com.yourssu.summerhackathon.repository

import com.yourssu.summerhackathon.entity.Exercise
import org.springframework.data.jpa.repository.JpaRepository

interface ExerciseRepository : JpaRepository<Exercise, Long> {
    fun findByName(name: String): Exercise?

    fun findAllByName(name: String): List<Exercise>

    fun findByNameContaining(name: String): List<Exercise>
}
