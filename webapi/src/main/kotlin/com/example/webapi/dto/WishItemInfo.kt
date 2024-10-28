package com.example.webapi.dto

import com.example.domain.entity.Item
import com.example.domain.entity.WishItem
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class WishItemInfo(
    @Schema(description = "찜 상품 ID")
    val id: Long,
    @Schema(description = "상품 ID")
    val itemInfo: ItemInfo,
    @Schema(description = "쩜 서랍 ID")
    val wishListId: Long,
    @Schema(description = "유저 ID")
    val userId: Long,
    @Schema(description = "생성 시각")
    val createdAt: LocalDateTime,
    @Schema(description = "수정 시각")
    val updatedAt: LocalDateTime
)
fun WishItem.toWishItemInfo(): WishItemInfo {
    return WishItemInfo(this.id, this.item.toItemInfo(), this.wishListId, this.userId, this.createdAt, this.updatedAt)
}

data class ItemInfo(
    @Schema(description = "상품 코드")
    val code: String,
    @Schema(description = "상품 URL")
    val url: String,
    @Schema(description = "상품 가격")
    val price: Int,
    @Schema(description = "생성 시각")
    val createdAt: LocalDateTime,
    @Schema(description = "수정 시각")
    val updatedAt: LocalDateTime
) {
    companion object {
        operator fun invoke(entity: Item): ItemInfo {
            return ItemInfo(
                code = entity.code,
                url = entity.url,
                price = entity.price,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt
            )
        }
    }
}

fun Item.toItemInfo(): ItemInfo {
    return ItemInfo(this.code, this.url, this.price, this.createdAt, this.updatedAt)
}
