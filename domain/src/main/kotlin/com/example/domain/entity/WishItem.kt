package com.example.domain.entity

import com.example.domain.base.common.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne

@Entity
class WishItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val wishListId: Long,
    val userId: Long,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_code")
    val item: Item
) : BaseEntity()
