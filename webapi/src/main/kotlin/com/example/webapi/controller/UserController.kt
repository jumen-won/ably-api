package com.example.webapi.controller

import com.example.domain.entity.User
import com.example.webapi.base.security.AuthenticatedUser
import com.example.webapi.controller.UserController.Companion.PATH_API_USER
import com.example.webapi.dto.UserInfo
import com.example.webapi.facade.UserFacade
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(PATH_API_USER)
class UserController(
    private val userFacade: UserFacade
) {

    @GetMapping
    fun get(@AuthenticatedUser user: User): ResponseEntity<UserInfo> {
        return ResponseEntity.ok(userFacade.get(user.id))
    }

    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginUserRequest,
        response: HttpServletResponse
    ): ResponseEntity<Unit> {
        response.setAccessTokenHeader(userFacade.login(request.email, request.password))
        return ResponseEntity.ok().build()
    }

    @PostMapping("/register")
    fun register(
        @RequestBody request: CreateUserRequest
    ): ResponseEntity<UserInfo> {
        return ResponseEntity.ok(userFacade.register(request.email, request.password))
    }

    companion object {
        private const val SERVICE_NAME = "users"
        const val PATH_API_USER = "/$SERVICE_NAME"
    }

    data class CreateUserRequest(
        val email: String,
        val password: String
    )

    data class LoginUserRequest(
        val email: String,
        val password: String
    )
}

fun HttpServletResponse.setAccessTokenHeader(accessToken: String) {
    setHeader("X-Access-Token", accessToken)
}
