package com.example.webapi.facade

import com.example.domain.base.common.PageQueryResult
import com.example.domain.entity.WishList
import com.example.domain.query.GetWishItemQuery
import com.example.domain.query.GetWishListQuery
import com.example.domain.usecase.WishListUseCase
import com.example.webapi.base.exception.WishListAlreadyExistsException
import com.example.webapi.dto.WishListInfo
import com.example.webapi.dto.toWishItemInfo
import com.example.webapi.dto.toWishListInfo
import org.springframework.stereotype.Service

@Service
class WishListFacade(
    private val query: GetWishListQuery,
    private val wishListUseCase: WishListUseCase,
    private val wishItemQuery: GetWishItemQuery
) {
    fun getMyWishList(
        userId: Long,
        page: Int,
        size: Int
    ): PageQueryResult<WishListInfo> {
        val (content, hasNext, totalElements, totalPages) = query.getByUserId(userId, page, size)
        val wishListIds = content.map { it.id }
        val wishItemInfos = wishItemQuery.getAllByWishListIds(wishListIds).map { it.toWishItemInfo() }
        val wishListIdWishItemInfosMap = wishItemInfos.groupBy { it.wishListId }

        return PageQueryResult(
            content = content.map { it.toWishListInfo(wishListIdWishItemInfosMap[it.id] ?: emptyList()) },
            hasNext = hasNext,
            totalElements = totalElements,
            totalPages = totalPages
        )
    }

    fun create(
        userId: Long,
        title: String
    ): WishList {
        if (query.exists(userId, title)) throw WishListAlreadyExistsException(userId, title)
        return wishListUseCase.create(WishListUseCase.Command.Create(title, userId))
    }

    fun delete(
        userId: Long,
        wishListId: Long
    ) {
        wishListUseCase.delete(WishListUseCase.Command.Delete(userId, wishListId))
    }
}
