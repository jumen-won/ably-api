package com.example.webapi.base.security

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val issuer: String,
    val accessTokenExpireMinutes: Long,
    val secret: String
)
