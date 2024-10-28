package com.example.webapi.base.exception

data class ErrorResponse(
    val status: Int,
    val code: String,
    val message: String
) {
    companion object {
        operator fun invoke(errorCodes: ErrorCodes): ErrorResponse {
            return ErrorResponse(errorCodes.status, errorCodes.code, errorCodes.message)
        }
    }
}
