package com.yourssu.summerhackathon.controller

import com.yourssu.summerhackathon.annotation.Auth
import com.yourssu.summerhackathon.dto.AuthUser
import com.yourssu.summerhackathon.dto.JwtResponse
import com.yourssu.summerhackathon.dto.request.LoginRequest
import com.yourssu.summerhackathon.dto.response.BadgesResponse
import com.yourssu.summerhackathon.dto.response.UserResponse
import com.yourssu.summerhackathon.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.view.RedirectView

@RestController
class UserController(
    private val userService: UserService,
) {
    @GetMapping("/login")
    fun getOauthLogin(): RedirectView {
        val apiKey = "6f93d7eeed22bda00467a44225180c61"
        val redirectUrl =
            "https://kauth.kakao.com/oauth/authorize?client_id=$apiKey&redirect_uri=http://localhost:3000&response_type=code"

        return RedirectView(redirectUrl)
    }

    @PostMapping("/login")
    fun login(
        @RequestBody loginRequest: LoginRequest,
    ): JwtResponse = JwtResponse(userService.login(loginRequest.code))

    @GetMapping("/users")
    fun findUsers(): List<UserResponse> = userService.findUsers()

    @PostMapping("/friends")
    fun addFriend(
        @Auth authUser: AuthUser,
        @RequestBody friendId: Long,
    ) {
        userService.addFriend(authUser.userId, friendId)
    }

    @GetMapping("/friends")
    fun findFriends(
        @Auth authUser: AuthUser,
    ): List<UserResponse> = userService.findFriends(authUser.userId)

    @GetMapping("/badges")
    fun findBadges(
        @Auth authUser: AuthUser,
    ): BadgesResponse = userService.findBadges(authUser.userId)
}
