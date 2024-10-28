package com.example.domain.query

import com.example.domain.base.common.PageQueryResult
import com.example.domain.base.common.toPageQueryResult
import com.example.domain.entity.WishItem
import com.example.domain.repository.WishItemRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetWishItemQuery(
    private val repository: WishItemRepository
) {

    fun getAllByWishListIds(
        wishListIds: List<Long>
    ): List<WishItem> {
        return repository.findAllByWishListIdIn(wishListIds)
    }

    fun getByWishListId(
        wishListId: Long,
        page: Int,
        size: Int
    ): PageQueryResult<WishItem> {
        return repository.findByWishListId(wishListId, PageRequest.of(page, size)).toPageQueryResult()
    }

    fun exists(
        userId: Long,
        itemCode: String
    ): Boolean {
        return repository.existsByUserIdAndItemCode(userId, itemCode)
    }
}
