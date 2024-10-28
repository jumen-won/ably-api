package com.example.webapi.base.security

import com.example.domain.entity.User
import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope

@Component
@RequestScope
class AuthenticationContext {
    lateinit var principal: User
}
