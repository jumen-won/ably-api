package com.example.webapi.facade

import com.example.domain.base.common.PageQueryResult
import com.example.domain.query.GetWishItemQuery
import com.example.domain.query.GetWishListQuery
import com.example.domain.usecase.WishItemUseCase
import com.example.webapi.base.exception.WishItemAlreadyExistsException
import com.example.webapi.base.exception.WishListNotFoundException
import com.example.webapi.dto.WishItemInfo
import com.example.webapi.dto.toWishItemInfo
import org.springframework.stereotype.Service

@Service
class WishItemFacade(
    private val query: GetWishItemQuery,
    private val wishItemUseCase: WishItemUseCase,
    private val wishListQuery: GetWishListQuery
) {

    fun getByWishListId(
        wishListId: Long,
        page: Int,
        size: Int
    ): PageQueryResult<WishItemInfo> {
        val (content, hasNext, totalElements, totalPages) = query.getByWishListId(wishListId, page, size)
        return PageQueryResult(
            content = content.map { it.toWishItemInfo() },
            hasNext = hasNext,
            totalElements = totalElements,
            totalPages = totalPages
        )
    }

    fun addToWishList(
        userId: Long,
        itemCode: String,
        wishListId: Long
    ) {
        val wishList = wishListQuery.getByUserIdAndWishListId(userId, wishListId) ?: throw WishListNotFoundException(wishListId)
        val exists = query.exists(userId, itemCode)
        if (exists) throw WishItemAlreadyExistsException(userId, itemCode, wishListId)

        wishItemUseCase.create(WishItemUseCase.Command.Create(itemCode, wishListId, userId))
    }

    fun delete(
        userId: Long,
        itemCode: String
    ) {
        wishItemUseCase.delete(WishItemUseCase.Command.Delete(userId, itemCode))
    }
}
