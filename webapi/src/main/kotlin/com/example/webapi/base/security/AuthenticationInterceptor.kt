package com.example.webapi.base.security

import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.example.domain.query.GetUserQuery
import com.example.webapi.base.exception.AccessTokenExpiredException
import com.example.webapi.base.exception.ApiServerException
import com.example.webapi.base.exception.CommonErrorCodes
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthenticationInterceptor(
    private val jwtTokenProvider: JwtTokenProvider,
    private val query: GetUserQuery,
    private val authenticationContext: AuthenticationContext
) : HandlerInterceptor {

    private val logger = KotlinLogging.logger { }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val authorization = getAuthorization(request)
        if (authorization.isNullOrEmpty() || authorization.startsWith(BEARER).not()) {
            return false
        }
        val decodedJWT = try {
            jwtTokenProvider.decode(authorization.replace(BEARER, ""))
        } catch (e: JWTVerificationException) {
            when (e) {
                is TokenExpiredException -> throw AccessTokenExpiredException()
                else -> {
                    throw ApiServerException("Failed to verify jwt.", CommonErrorCodes.InternalServerError)
                }
            }
        }

        authenticationContext.principal = query.getOrThrow(decodedJWT.getUserId())

        return true
    }

    private fun getAuthorization(request: HttpServletRequest): String? {
        return request.getHeader(HttpHeaders.AUTHORIZATION)
    }

    companion object {
        private const val BEARER = "Bearer "
    }
}
