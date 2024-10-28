package com.example.webapi.dto

import com.example.domain.entity.WishList
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class WishListInfo(
    @Schema(description = "쩜 시럽 ID")
    val id: Long,
    @Schema(description = "찜 서랍 제목")
    val title: String,
    @Schema(description = "유저 ID")
    val userId: Long,
    @Schema(description = "생성 시각")
    val createdAt: LocalDateTime,
    @Schema(description = "수정 시각")
    val updatedAt: LocalDateTime,
    @Schema(description = "찜 상품 목록")
    val wishItemInfos: List<WishItemInfo>
) {
    companion object {
        operator fun invoke(entity: WishList, wishItemInfos: List<WishItemInfo>): WishListInfo {
            return WishListInfo(
                id = entity.id,
                title = entity.title,
                userId = entity.userId,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
                wishItemInfos = wishItemInfos
            )
        }
    }
}

fun WishList.toWishListInfo(wishItemInfos: List<WishItemInfo>): WishListInfo {
    return WishListInfo(this.id, this.title, this.userId, this.createdAt, this.updatedAt, wishItemInfos)
}
