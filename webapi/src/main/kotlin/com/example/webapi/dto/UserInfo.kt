package com.example.webapi.dto

import com.example.domain.entity.User
import com.example.domain.entity.WishList
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class UserInfo(
    @Schema(description = "유저 ID")
    val id: Long,
    @Schema(description = "유저 이메일")
    val email: String,
    @Schema(description = "생성 시각")
    val createdAt: LocalDateTime,
    @Schema(description = "수정 시각")
    val updatedAt: LocalDateTime,
) {
    companion object {
        operator fun invoke(entity: User): UserInfo {
            return UserInfo(
                id = entity.id,
                email = entity.email,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
            )
        }
    }
}

fun User.toUserInfo(): UserInfo {
    return UserInfo(this.id, this.email, this.createdAt, this.updatedAt)
}
