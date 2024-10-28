package com.example.webapi.facade

import com.example.domain.entity.User
import com.example.domain.query.GetUserQuery
import com.example.domain.usecase.UserUseCase
import com.example.webapi.base.exception.UserEmailNotFoundException
import com.example.webapi.base.exception.UserPasswordNotMatchedException
import com.example.webapi.base.security.JwtTokenProvider
import com.example.webapi.dto.toUserInfo
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.crypto.password.PasswordEncoder

class UserFacadeTest {
    private var query: GetUserQuery = mockk(relaxed = true)
    private var userUseCase: UserUseCase = mockk(relaxed = true)
    private var passwordEncoder: PasswordEncoder = mockk(relaxed = true)
    private var jwtTokenProvider: JwtTokenProvider = mockk(relaxed = true)

    private lateinit var sut: UserFacade

    @BeforeEach
    fun beforeEach() {
        clearAllMocks()
        sut = UserFacade(query, userUseCase, passwordEncoder, jwtTokenProvider)
    }

    @Test
    fun `login - 이메일에 해당하는 사용자가 없으면 UserEmailNotFoundException을 던진다`() {
        // given
        val email = "test@test.com"
        val password = "password"
        every { query.getByEmail(email) } returns null

        // when & then
        assertThrows<UserEmailNotFoundException> {
            sut.login(email, password)
        }
    }

    @Test
    fun `login - 비밀번호가 일치하지 않으면 UserPasswordNotMatchedException을 던진다`() {
        // given
        val email = "test@test.com"
        val password = "password"
        val encodedPassword = "encodedPassword"
        val user = User(id = 1L, email = email, password = encodedPassword)

        every { query.getByEmail(email) } returns user
        every { passwordEncoder.matches(password, encodedPassword) } returns false

        // when & then
        assertThrows<UserPasswordNotMatchedException> {
            sut.login(email, password)
        }
    }

    @Test
    fun `login - 로그인에 성공하면 JWT 토큰을 반환한다`() {
        // given
        val email = "test@test.com"
        val password = "password"
        val encodedPassword = "encodedPassword"
        val userId = 1L
        val token = "token"
        val user = User(id = userId, email = email, password = encodedPassword)

        every { query.getByEmail(email) } returns user
        every { passwordEncoder.matches(password, encodedPassword) } returns true
        every { jwtTokenProvider.encode(username = email, userId = userId) } returns token

        // when
        val result = sut.login(email, password)

        // then
        assertEquals(token, result)
    }

    @Test
    fun `get - ID로 사용자를 조회한다`() {
        // given
        val userId = 1L
        val user = User(id = userId, email = "test@test.com", password = "password")
        every { query.getOrThrow(userId) } returns user

        // when
        val result = sut.get(userId)

        // then
        assertEquals(user.toUserInfo(), result)
    }

    @Test
    fun `register - 새로운 사용자를 등록한다`() {
        // given
        val email = "test@test.com"
        val password = "password"
        val encodedPassword = "encodedPassword"
        val user = User(id = 1L, email = email, password = encodedPassword)
        val command = UserUseCase.Command.Create(email, encodedPassword)

        every { passwordEncoder.encode(password) } returns encodedPassword
        every { userUseCase.create(command) } returns user

        // when
        val result = sut.register(email, password)

        // then
        assertEquals(user.toUserInfo(), result)
    }
}
