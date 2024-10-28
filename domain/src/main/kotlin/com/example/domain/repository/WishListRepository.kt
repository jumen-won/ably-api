package com.example.domain.repository

import com.example.domain.entity.WishList
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WishListRepository : JpaRepository<WishList, Long> {
    fun findByUserId(userId: Long, pageable: Pageable): Page<WishList>
    fun existsByUserIdAndTitle(userId: Long, title: String): Boolean
}
