package com.example.webapi.base.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant

@Component
class JwtTokenProvider(properties: JwtProperties) {
    private val issuer = properties.issuer
    private val accessTokenExpireMinutes = Duration.ofMinutes(properties.accessTokenExpireMinutes).toMillis()
    private val algorithm = Algorithm.HMAC256(properties.secret)
    private val verifier = JWT.require(algorithm).build()

    fun encode(username: String, userId: Long): String {
        val issuedAt = Instant.now()
        val expiresAt = issuedAt.plusMillis(accessTokenExpireMinutes)

        return JWT.create()
            .withSubject(username)
            .withClaim(USER_ID_CLAIM_KEY, userId)
            .withIssuer(issuer)
            .withIssuedAt(issuedAt)
            .withExpiresAt(expiresAt)
            .sign(algorithm)
    }

    fun decode(token: String): DecodedJWT {
        return verifier.verify(token)
    }

    companion object {
        const val USER_ID_CLAIM_KEY = "userId"
    }
}

fun DecodedJWT.getUserId(): Long = this.getClaim(JwtTokenProvider.USER_ID_CLAIM_KEY).asLong()
