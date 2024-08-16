package com.yourssu.summerhackathon.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.yourssu.summerhackathon.dto.KakaoMy
import com.yourssu.summerhackathon.dto.KakaoTokenResponse
import com.yourssu.summerhackathon.dto.response.BadgeResponse
import com.yourssu.summerhackathon.dto.response.BadgesResponse
import com.yourssu.summerhackathon.dto.response.UserResponse
import com.yourssu.summerhackathon.entity.Friend
import com.yourssu.summerhackathon.entity.User
import com.yourssu.summerhackathon.jwt.JwtUtils
import com.yourssu.summerhackathon.repository.BadgeRepository
import com.yourssu.summerhackathon.repository.FriendRepository
import com.yourssu.summerhackathon.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

@Transactional(readOnly = true)
@Service
class UserService(
    private val userRepository: UserRepository,
    private val friendRepository: FriendRepository,
    private val badgeRepository: BadgeRepository,
    private val jwtUtils: JwtUtils,
) {
    fun login(code: String): String {
        val restTemplate = RestTemplate()
        val mapper = jacksonObjectMapper()
        val url = "https://kauth.kakao.com/oauth/token"

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val body: MultiValueMap<String, String> = LinkedMultiValueMap()
        body.add("grant_type", "authorization_code")
        body.add("client_id", "6f93d7eeed22bda00467a44225180c61")
        body.add("redirect_uri", "http://localhost:3000")
        body.add("code", code)
        body.add("client_secret", "j58Lhf5ldXueahWPppu00oZSCqBH4kLd")

        val requestEntity = HttpEntity(body, headers)

        val response: ResponseEntity<String> =
            restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String::class.java,
            )

        println(response.body)

        // JSON 응답을 객체로 변환하고 access_token 추출
        val responseMap =
            mapper.readValue(response.body as String, KakaoTokenResponse::class.java)

        responseMap.accessToken

        val myInfoHeader = HttpHeaders()
        myInfoHeader.contentType = MediaType.APPLICATION_FORM_URLENCODED
        myInfoHeader.add("Authorization", "Bearer ${responseMap.accessToken}")

        val myRequestEntity = HttpEntity<String>(null, myInfoHeader)

        val myInfoUrl: String = "https://kapi.kakao.com/v2/user/me"
        val myResponse: ResponseEntity<String> =
            restTemplate.exchange(
                myInfoUrl,
                HttpMethod.GET,
                myRequestEntity,
                String::class.java,
            )

        val kakaoResponse = mapper.readValue(myResponse.body, KakaoMy::class.java)

        val id = kakaoResponse.id
        val name = kakaoResponse.kakaoAccount.name
        val email = kakaoResponse.kakaoAccount.email

        val user = userRepository.save(User(name = name, kakaoId = id, email = email))

        return jwtUtils.createToken(user.id)
    }

    fun findUsers(): List<UserResponse> =
        userRepository.findAll().map {
            UserResponse(
                id = it.id,
                name = it.name,
                email = it.email,
            )
        }

    fun addFriend(
        userId: Long,
        friendId: Long,
    ) {
        val user = userRepository.findByIdOrNull(userId) ?: throw IllegalArgumentException("존재하지 않는 유저입니다.")
        val friend = userRepository.findByIdOrNull(friendId) ?: throw IllegalArgumentException("존재하지 않는 유저입니다.")

        friendRepository.save(
            Friend(
                follower = user.id,
                following = friend.id,
            ),
        )
    }

    fun findFriends(userId: Long): List<UserResponse> {
        val user = userRepository.findByIdOrNull(userId) ?: throw IllegalArgumentException("존재하지 않는 유저입니다.")
        val friends = friendRepository.findByFollower(user.id)

        return friends.map {
            UserResponse(
                id = it.following,
                name = userRepository.findByIdOrNull(it.following)?.name ?: "존재하지 않는 유저",
                email = userRepository.findByIdOrNull(it.following)?.name ?: "존재하지 않는 유저",
            )
        }
    }

    fun findBadges(userId: Long): BadgesResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw IllegalArgumentException("존재하지 않는 유저입니다.")

        val badges = badgeRepository.findByUserId(user.id)

        return BadgesResponse(
            userId = user.id,
            userName = user.name,
            badges =
                badges.map {
                    BadgeResponse(
                        id = it.id,
                        name = it.exercise.name,
                    )
                },
        )
    }
}
