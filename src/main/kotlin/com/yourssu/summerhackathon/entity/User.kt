package com.yourssu.summerhackathon.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val name: String,
    val kakaoId: Long,
    @Column(unique = true)
    val email: String,
    var weight: Double = 0.0,
    var muscle: Double = 0.0,
    var fat: Double = 0.0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
)
