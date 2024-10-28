package com.example.webapi.base.common

data class ApiResponse<T>(
    val status: Int,
    val code: String,
    val data: T? = null
) {

    companion object {
        operator fun <T> invoke(data: T): ApiResponse<T> {
            return ApiResponse(
                status = 200,
                code = "SUCCESS",
                data = data
            )
        }
    }
}

data class PageableResponse<T>(
    val data: List<T> = emptyList(),
    val hasNext: Boolean? = null,
    val totalElements: Int = 0,
    val totalPages: Int? = null
) {

    companion object {
        operator fun <T> invoke(data: List<T>, totalElements: Int, totalPages: Int): PageableResponse<T> {
            return PageableResponse(
                data = data,
                totalElements = totalElements,
                totalPages = totalPages
            )
        }
    }
}
