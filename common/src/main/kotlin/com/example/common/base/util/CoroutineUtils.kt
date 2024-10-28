package com.example.common.base.util

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.withContext
import java.time.Duration
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

val logger = KotlinLogging.logger { }

fun <T> CoroutineScope.mdcAsync(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
): Deferred<T> {
    return async(context + MDCContext(), start, block)
}

fun CoroutineScope.mdcLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return launch(context + MDCContext(), start, block)
}

suspend fun <T> mdcWithContext(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> T
): T {
    return withContext(context + MDCContext(), block)
}

fun <T> retryWhen(delayMillis: Long, maxAttempts: Int, block: suspend () -> Result<T>): T? =
    runBlocking(MDCContext()) {
        retryWhenAwait(Duration.ofMillis(delayMillis), maxAttempts, block)
    }

fun <T> retryWhen(delay: Duration, maxAttempts: Int, block: suspend () -> Result<T>): T? =
    runBlocking(MDCContext()) {
        retryWhenAwait(delay, maxAttempts, block)
    }

suspend fun <T> retryWhenAwait(delay: Duration, maxAttempts: Int, block: suspend () -> Result<T>): T? {
    repeat(maxAttempts) { repeatCount ->
        block()
            .onSuccess { return it }
            .onFailure {
                logger.warn {
                    "Retrying block Max attempts: $maxAttempts, Repeat counts: ${repeatCount.inc()}, Retry delay: ${delay.toMillis()}ms"
                }
                delay(delay.toMillis())

                if (maxAttempts == repeatCount.inc()) throw it
            }.getOrNull()
    }
    return block().getOrThrow()
}
