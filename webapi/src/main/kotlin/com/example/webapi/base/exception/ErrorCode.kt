package com.example.webapi.base.exception

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.UNAUTHORIZED

sealed interface ErrorCodes {
    val status: Int
    val code: String
    val message: String
}

object CommonErrorCodes {
    data object BadRequest : ErrorCodes {
        override val status: Int = BAD_REQUEST.value()
        override val code: String = "COMMON.BAD_REQUEST"
        override val message: String = BAD_REQUEST.reasonPhrase
    }

    data object InternalServerError : ErrorCodes {
        override val status: Int = INTERNAL_SERVER_ERROR.value()
        override val code: String = "COMMON.INTERNAL_SERVER_ERROR"
        override val message: String = INTERNAL_SERVER_ERROR.reasonPhrase
    }

    data object Unauthorized : ErrorCodes {
        override val status: Int = UNAUTHORIZED.value()
        override val code: String = "COMMON.UNAUTHORIZED"
        override val message: String = UNAUTHORIZED.reasonPhrase
    }

    data object Forbidden : ErrorCodes {
        override val status: Int = FORBIDDEN.value()
        override val code: String = "COMMON.FORBIDDEN"
        override val message: String = FORBIDDEN.reasonPhrase
    }

    data object NotFound : ErrorCodes {
        override val status: Int = NOT_FOUND.value()
        override val code: String = "COMMON.NOT_FOUND"
        override val message: String = NOT_FOUND.reasonPhrase
    }

    data object MethodNotAllowed : ErrorCodes {
        override val status: Int = METHOD_NOT_ALLOWED.value()
        override val code: String = "COMMON.METHOD_NOT_ALLOWED"
        override val message: String = METHOD_NOT_ALLOWED.reasonPhrase
    }
}

object UserErrorCodes {
    data object NotFound : ErrorCodes {
        override val status: Int = NOT_FOUND.value()
        override val code: String = "USER.NOT_FOUND"
        override val message: String = "User Not Found"
    }

    data object PasswordNotMatched : ErrorCodes {
        override val status: Int = BAD_REQUEST.value()
        override val code: String = "USER.PASSWORD_NOT_MATCHED"
        override val message: String = "Password Not Matched"
    }

    data object AlreadyExists : ErrorCodes {
        override val status: Int = BAD_REQUEST.value()
        override val code: String = "USER.ALREADY_EXISTS"
        override val message: String = "User Already Exists"
    }
}

object WishListErrorCodes {
    data object NotFound : ErrorCodes {
        override val status: Int = NOT_FOUND.value()
        override val code: String = "WISHLIST.NOT_FOUND"
        override val message: String = "WishList Not Found"
    }
    data object AlreadyExists : ErrorCodes {
        override val status: Int = BAD_REQUEST.value()
        override val code: String = "WISHLIST.ALREADY_EXISTS"
        override val message: String = "WishList Already Exists"
    }
}

object WishItemErrorCodes {
    data object AlreadyExists : ErrorCodes {
        override val status: Int = BAD_REQUEST.value()
        override val code: String = "WISHITEM.ALREADY_EXISTS"
        override val message: String = "WishItem Already Exists"
    }
}
