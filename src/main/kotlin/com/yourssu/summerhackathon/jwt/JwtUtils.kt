package com.yourssu.summerhackathon.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.Key
import java.util.Date

@Component
class JwtUtils {
    companion object {
        private val secretKey = "secretKey_secretKey_secretKey_secretKey"
        private val expiredMs = 1000 * 60 * 60 * 24 * 7 // 7 days
    }

    fun createToken(userId: Long): String =
        Jwts
            .builder()
            .setSubject(userId.toString())
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + expiredMs))
            .signWith(getKey(secretKey), SignatureAlgorithm.HS256)
            .compact()

    fun getKey(key: String): Key {
        val keyBytes = key.toByteArray(StandardCharsets.UTF_8)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun validateToken(token: String): Boolean =
        try {
            Jwts
                .parserBuilder()
                .setSigningKey(getKey(secretKey))
                .build()
                .parseClaimsJws(token)
            true
        } catch (e: Exception) {
            false
        }

    fun getUserIdFromToken(token: String): Long {
        val claims = getClaimsFromToken(token)
        return claims.subject.toLong()
    }

    private fun getClaimsFromToken(token: String): Claims =
        Jwts
            .parserBuilder()
            .setSigningKey(getKey(secretKey))
            .build()
            .parseClaimsJws(token)
            .body
}
