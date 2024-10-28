package com.example.testbase

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component

@Component
class DatabaseCleaner(
    private val repositories: List<CrudRepository<*, *>>
) {

    val logger = KotlinLogging.logger { }

    /**
     * 통합 테스트 격리를 위해 저장된 DB 데이터를 제거한다
     */
    fun clean() {
        this.cleanMySQL()
    }

    private fun cleanMySQL() {
        this.repositories.forEach {
            try {
                it.deleteAll()
            } catch (e: Exception) {
                logger.warn(e) { "Failed to delete data. ${e.message}" }
            }
        }
    }
}
