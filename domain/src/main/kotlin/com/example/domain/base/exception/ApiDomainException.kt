package com.example.domain.base.exception

import com.example.common.base.exception.ApiDefaultException

open class ApiDomainException(logMessage: String, e: Throwable? = null) : ApiDefaultException(logMessage, e)

class UserNotFoundDomainException(logMessage: String = "User Not found.") : ApiDomainException(logMessage)

class WishListNotFoundDomainException(logMessage: String = "WishList Not found.") : ApiDomainException(logMessage)

class ItemNotFoundDomainException(logMessage: String = "Item Not found.") : ApiDomainException(logMessage)
