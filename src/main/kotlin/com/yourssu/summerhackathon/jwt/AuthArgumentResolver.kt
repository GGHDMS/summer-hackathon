package com.yourssu.summerhackathon.jwt

import com.yourssu.summerhackathon.annotation.Auth
import com.yourssu.summerhackathon.dto.AuthUser
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class AuthArgumentResolver(
    private val jwtUtils: JwtUtils,
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return (parameter.getParameterAnnotation(Auth::class.java) != null) &&
                (parameter.parameterType == AuthUser::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any? {
        val request = (webRequest as ServletWebRequest).request
        val token = resolveToken(request)

        return if (token != null && jwtUtils.validateToken(token)) {
            val userId = jwtUtils.getUserIdFromToken(token)
            AuthUser(userId)  // AuthUser 객체를 반환
        } else {
            null
        }
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else null
    }
}
