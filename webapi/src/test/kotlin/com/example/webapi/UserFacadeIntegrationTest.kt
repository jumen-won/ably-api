package com.example.webapi

import com.example.domain.base.exception.UserNotFoundDomainException
import com.example.testbase.IntegrationTest
import com.example.webapi.base.exception.UserEmailNotFoundException
import com.example.webapi.base.exception.UserPasswordNotMatchedException
import com.example.webapi.facade.UserFacade
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class UserFacadeIntegrationTest : IntegrationTest() {
    @Autowired
    private lateinit var sut: UserFacade

    private val testEmail = "test@example.com"
    private val testPassword = "password123"

    @BeforeEach
    fun setup() {
        cleanupTestData()
    }

    @Test
    fun `회원가입 성공 테스트`() {
        // when
        val createdUser = sut.register(testEmail, testPassword)

        // then
        assertNotNull(createdUser)
        assertEquals(testEmail, createdUser.email)
    }

    @Test
    fun `로그인 성공 테스트`() {
        // given
        val user = sut.register(testEmail, testPassword)

        // when
        val token = sut.login(testEmail, testPassword)

        // then
        assertNotNull(token)
        assertTrue(token.isNotEmpty())
    }

    @Test
    fun `잘못된 비밀번호로 로그인 실패 테스트`() {
        // given
        sut.register(testEmail, testPassword)

        // when & then
        assertThrows(UserPasswordNotMatchedException::class.java) {
            sut.login(testEmail, "wrongpassword")
        }
    }

    @Test
    fun `존재하지 않는 이메일로 로그인 실패 테스트`() {
        // when & then
        assertThrows(UserEmailNotFoundException::class.java) {
            sut.login("nonexistent@example.com", testPassword)
        }
    }

    @Test
    fun `ID로 사용자 조회 테스트`() {
        // given
        val createdUser = sut.register(testEmail, testPassword)

        // when
        val foundUser = sut.get(createdUser.id)

        // then
        assertNotNull(foundUser)
        assertEquals(createdUser.id, foundUser.id)
        assertEquals(testEmail, foundUser.email)
    }

    @Test
    fun `존재하지 않는 ID로 사용자 조회 실패 테스트`() {
        // when & then
        assertThrows(UserNotFoundDomainException::class.java) {
            sut.get(999L)
        }
    }

    private fun cleanupTestData() {
        // 테스트 데이터 정리 로직
        // 예: repository.deleteAll()
    }
}
