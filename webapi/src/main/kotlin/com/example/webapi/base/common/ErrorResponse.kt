package com.example.webapi.base.common

import com.example.webapi.base.exception.CommonErrorCodes
import com.example.webapi.base.exception.ErrorCodes
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

data class ErrorResponse(
    val message: String,
    val status: Int,
    val code: String,
    val fieldErrors: List<FieldError>
) {
    constructor(code: ErrorCodes) : this(code, emptyList())

    constructor(code: ErrorCodes, errors: List<FieldError> = emptyList()) : this(
        message = code.message,
        status = code.status,
        code = code.code,
        fieldErrors = errors
    )

    companion object {
        fun of(code: ErrorCodes): ErrorResponse =
            ErrorResponse(code)

        fun of(code: ErrorCodes, bindingResult: BindingResult): ErrorResponse =
            ErrorResponse(code, FieldError.of(bindingResult))

        fun of(e: MethodArgumentTypeMismatchException): ErrorResponse =
            ErrorResponse(CommonErrorCodes.BadRequest, FieldError.of(e.name, e.value?.toString(), e.errorCode))
    }

    data class FieldError(
        val field: String,
        val value: String?,
        val reason: String
    ) {
        companion object {
            fun of(field: String, value: String?, reason: String): List<FieldError> {
                return listOf(FieldError(field, value, reason))
            }

            fun of(bindingResult: BindingResult): List<FieldError> {
                return bindingResult.fieldErrors.map { error ->
                    FieldError(
                        error.field,
                        error.rejectedValue?.toString() ?: "",
                        error.defaultMessage ?: ""
                    )
                }
            }
        }
    }
}

fun ErrorResponse.toResponseEntity(): ResponseEntity<ErrorResponse> =
    ResponseEntity(this, HttpStatus.valueOf(this.status))
