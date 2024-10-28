package com.example.domain.entity

import com.example.domain.base.common.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class Item(
    @Id
    val code: String,
    val url: String,
    val price: Int
) : BaseEntity()
