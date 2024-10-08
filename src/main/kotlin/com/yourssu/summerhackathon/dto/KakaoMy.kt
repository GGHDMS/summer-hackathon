package com.yourssu.summerhackathon.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class KakaoMy
    @JsonCreator
    constructor(
        val id: Long,
        @JsonProperty("kakao_account")
        val kakaoAccount: KakaoAccount,
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        data class KakaoAccount(
            val email: String,
            val profile: Profile,
        )

        @JsonIgnoreProperties(ignoreUnknown = true)
        data class Profile(
            val nickname: String,
        )
    }
