package com.example.domain.usecase

import com.example.domain.entity.User
import com.example.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserUseCase(
    private val repository: UserRepository
) {

    fun create(command: Command.Create): User {
        val (email, password) = command
        return repository.save(User(email = email, password = password))
    }

    sealed interface Command {
        data class Create(val email: String, val password: String) : Command
    }
}
