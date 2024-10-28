package com.example.domain.repository

import com.example.domain.entity.Item
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemRepository : JpaRepository<Item, Long> {
    fun findByCode(code: String): Item?
    fun existsByCode(code: String): Boolean
    fun deleteByCode(code: String)
}
