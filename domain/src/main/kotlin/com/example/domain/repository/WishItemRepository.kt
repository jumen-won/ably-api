package com.example.domain.repository

import com.example.domain.entity.WishItem
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface WishItemRepository : JpaRepository<WishItem, Long> {
    fun findAllByWishListIdIn(wishListIds: List<Long>): List<WishItem>
    @Query("""
    SELECT wi
    FROM WishItem wi
    JOIN FETCH wi.item
    WHERE wi.wishListId = :wishListId
    """)
    fun findByWishListId(wishListId: Long, pageable: Pageable): Page<WishItem>
    fun existsByUserIdAndItemCode(userId: Long, itemCode: String): Boolean
    fun deleteByUserIdAndItemCode(userId: Long, itemCode: String)
    fun deleteAllByUserIdAndWishListId(userId: Long, wishListId: Long)
}
