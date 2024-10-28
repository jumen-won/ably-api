package com.example.domain.query

import com.example.domain.base.exception.UserNotFoundDomainException
import com.example.domain.entity.User
import com.example.domain.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetUserQuery(
    private val repository: UserRepository
) {

    fun getOrThrow(userId: Long): User {
        return getOrNull(userId) ?: throw UserNotFoundDomainException()
    }

    fun getOrNull(userId: Long): User? {
        return repository.findByIdOrNull(userId)
    }

    fun getByEmail(email: String): User? {
        return repository.findByEmail(email)
    }
}
