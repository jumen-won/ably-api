package com.example.domain.base.common

import org.springframework.data.domain.Page

data class PageQueryResult<T>(
    val content: List<T>,
    val hasNext: Boolean,
    val totalElements: Int,
    val totalPages: Int
)

inline fun <reified T> Page<T>.toPageQueryResult(): PageQueryResult<T> {
    return PageQueryResult(
        content = this.content,
        hasNext = this.hasNext(),
        totalElements = this.totalElements.toInt(),
        totalPages = this.totalPages
    )
}
