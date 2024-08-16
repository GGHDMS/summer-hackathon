package com.yourssu.summerhackathon.repository

import com.yourssu.summerhackathon.entity.Friend
import org.springframework.data.jpa.repository.JpaRepository

interface FriendRepository : JpaRepository<Friend, Long> {
    fun findByFollower(follower: Long): List<Friend>

    fun findByFollowing(following: Long): List<Friend>

    fun findByFollowerAndFollowing(
        follower: Long,
        following: Long,
    ): Friend?

    fun deleteByFollowerAndFollowing(
        follower: Long,
        following: Long,
    )
}
