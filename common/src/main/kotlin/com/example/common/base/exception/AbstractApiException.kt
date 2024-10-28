package com.example.common.base.exception

abstract class AbstractApiException : RuntimeException {
    constructor(logMessage: String?) : super(logMessage)
    constructor(logMessage: String?, e: Throwable?) : super(logMessage, e)
}
