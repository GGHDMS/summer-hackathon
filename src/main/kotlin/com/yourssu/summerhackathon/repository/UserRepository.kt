package com.yourssu.summerhackathon.repository

import com.yourssu.summerhackathon.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>
