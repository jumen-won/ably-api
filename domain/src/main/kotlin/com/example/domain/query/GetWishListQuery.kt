package com.example.domain.query

import com.example.domain.base.common.PageQueryResult
import com.example.domain.base.common.toPageQueryResult
import com.example.domain.base.exception.WishListNotFoundDomainException
import com.example.domain.entity.WishList
import com.example.domain.repository.WishListRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetWishListQuery(
    private val repository: WishListRepository
) {

    fun getOrThrow(wishListId: Long): WishList {
        return getOrNull(wishListId) ?: throw WishListNotFoundDomainException()
    }

    fun getOrNull(wishListId: Long): WishList? {
        return repository.findByIdOrNull(wishListId)
    }

    fun getByUserId(
        userId: Long,
        page: Int,
        size: Int
    ): PageQueryResult<WishList> {
        return repository.findByUserId(userId, PageRequest.of(page, size)).toPageQueryResult()
    }

    fun getByUserIdAndWishListId(
        userId: Long,
        wishListId: Long
    ): WishList? {
        return repository.findByIdOrNull(wishListId)?.takeIf { it.userId == userId }
    }

    fun exists(
        userId: Long,
        title: String
    ): Boolean {
        return repository.existsByUserIdAndTitle(userId, title)
    }
}
