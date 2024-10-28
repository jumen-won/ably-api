package com.example.common.base.exception

open class ApiDefaultException : AbstractApiException {
    constructor(logMessage: String?) : super(logMessage)
    constructor(logMessage: String?, e: Throwable?) : super(logMessage, e)
}
