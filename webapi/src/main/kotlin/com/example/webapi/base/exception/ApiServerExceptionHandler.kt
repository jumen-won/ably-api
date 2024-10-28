package com.example.webapi.base.exception

import com.example.common.base.exception.ApiDefaultException
import com.example.domain.base.exception.ApiDomainException
import com.example.webapi.base.common.ErrorResponse
import com.example.webapi.base.common.toResponseEntity
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class ApiServerExceptionHandler {

    private val logger = KotlinLogging.logger { }

    @ExceptionHandler(ApiDefaultException::class)
    fun handleApiDefaultException(e: ApiDefaultException): ResponseEntity<ErrorResponse> {
        loggingApiDefaultException(e)
        return toErrorResponse(e)
    }

    private fun loggingApiDefaultException(e: ApiDefaultException) {
        when (e) {
            is ApiDomainException -> logger.error(e) { "logMessage: ${e.message}" }
            is ApiServerException -> logger.error(e) { "errorCode: ${e.errorCode}, logMessage: ${e.message}" }
            else -> logger.warn { "logMessage: ${e.message}" }
        }
    }

    private fun toErrorResponse(e: ApiDefaultException): ResponseEntity<ErrorResponse> {
        return when (e) {
            is ApiDomainException -> handleApiDomainException(e)
            is ApiServerException -> handleApiServerException(e)
            else -> handleRuntimeException(e)
        }
    }

    private fun handleApiDomainException(e: ApiDomainException): ResponseEntity<ErrorResponse> {
        return ErrorResponse.of(CommonErrorCodes.InternalServerError).toResponseEntity()
    }

    private fun handleApiServerException(e: ApiServerException): ResponseEntity<ErrorResponse> {
        return ErrorResponse.of(e.errorCode).toResponseEntity()
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(e: RuntimeException): ResponseEntity<ErrorResponse> {
        logger.error(e) {}
        return ErrorResponse.of(CommonErrorCodes.InternalServerError).toResponseEntity()
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        logger.error(e) {}
        return ErrorResponse.of(CommonErrorCodes.InternalServerError).toResponseEntity()
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        logger.warn(e) {}
        return ErrorResponse.of(CommonErrorCodes.BadRequest, e.bindingResult).toResponseEntity()
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(e: MethodArgumentTypeMismatchException): ResponseEntity<ErrorResponse> {
        logger.warn(e) {}
        return ErrorResponse.of(e).toResponseEntity()
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException): ResponseEntity<ErrorResponse> {
        logger.warn(e) {}
        return ErrorResponse.of(CommonErrorCodes.MethodNotAllowed).toResponseEntity()
    }
}
