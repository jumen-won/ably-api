package com.example.webapi.base.exception

import com.example.common.base.exception.ApiDefaultException

open class ApiServerException(logMessage: String?, val errorCode: ErrorCodes, e: Throwable? = null) :
    ApiDefaultException(logMessage, e)

open class ApiBadRequestException(logMessage: String, errorCode: ErrorCodes = CommonErrorCodes.BadRequest) :
    ApiServerException(logMessage, errorCode)

class UserEmailNotFoundException(email: String) :
    ApiBadRequestException("Not found User. email: $email", UserErrorCodes.NotFound)

class UserPasswordNotMatchedException :
    ApiBadRequestException("Password not matched", UserErrorCodes.PasswordNotMatched)

class WishListNotFoundException(wishListId: Long) :
    ApiBadRequestException("Not found WishList. wishListId: $wishListId", WishListErrorCodes.NotFound)

class WishListAlreadyExistsException(userId: Long, title: String) :
    ApiBadRequestException("WishList already exists. userId: $userId, title: $title", WishListErrorCodes.AlreadyExists)

class WishItemAlreadyExistsException(userId: Long, itemCode: String, wishListId: Long) :
    ApiBadRequestException("WishItem already exists. userId: $userId, itemCode: $itemCode, wishListId: $wishListId", WishItemErrorCodes.AlreadyExists)

open class ApiUnauthorizedException(logMessage: String, errorCode: ErrorCodes = CommonErrorCodes.Unauthorized) :
    ApiBadRequestException(logMessage, errorCode)

class AccessTokenExpiredException : ApiUnauthorizedException("Access token expired.")

open class ApiForbiddenException(logMessage: String, errorCode: ErrorCodes = CommonErrorCodes.Forbidden) :
    ApiBadRequestException(logMessage, errorCode)

open class ApiNotFoundException(logMessage: String, errorCode: ErrorCodes = CommonErrorCodes.NotFound) :
    ApiBadRequestException(logMessage, errorCode)
