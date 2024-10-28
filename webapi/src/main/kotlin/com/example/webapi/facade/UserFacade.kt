package com.example.webapi.facade

import com.example.domain.entity.User
import com.example.domain.query.GetUserQuery
import com.example.domain.usecase.UserUseCase
import com.example.webapi.base.exception.UserEmailNotFoundException
import com.example.webapi.base.exception.UserPasswordNotMatchedException
import com.example.webapi.base.security.JwtTokenProvider
import com.example.webapi.dto.UserInfo
import com.example.webapi.dto.toUserInfo
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserFacade(
    private val query: GetUserQuery,
    private val userUseCase: UserUseCase,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {

    fun login(email: String, password: String): String {
        val user = query.getByEmail(email) ?: throw UserEmailNotFoundException(email)
        if (passwordEncoder.matches(password, user.password).not()) throw UserPasswordNotMatchedException()

        return jwtTokenProvider.encode(username = user.email, userId = user.id)
    }

    fun get(id: Long): UserInfo {
        return query.getOrThrow(id).toUserInfo()
    }

    fun register(email: String, password: String): UserInfo {
        return userUseCase.create(UserUseCase.Command.Create(email, passwordEncoder.encode(password))).toUserInfo()
    }
}
