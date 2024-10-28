package com.example.testbase

import com.example.AblyApplication
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(
    classes = [AblyApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles(TestProfile.TEST)
@Testcontainers
@Transactional
abstract class IntegrationTest {

    @Autowired
    private lateinit var databaseCleaner: DatabaseCleaner

    @BeforeEach
    fun clean() {
        if (mysqlContainer.isRunning) {
            cleanMysql()
        }
    }

    private fun cleanMysql() {
        databaseCleaner.clean()
    }

    companion object {
        private val mysqlContainer = MySQLContainer<Nothing>("mysql:8.0").apply {
            withDatabaseName("ably")
            withUsername("root")
            withPassword("root")
            withCreateContainerCmdModifier { cmd -> cmd.withPlatform("linux/x86_64") }
            withCommand("mysqld", "--character-set-server=utf8mb4")
            withReuse(true)
            start()
        }

        @DynamicPropertySource
        @JvmStatic
        fun registerProperties(registry: DynamicPropertyRegistry) {
            setJdbcProperties(registry)
        }

        private fun setJdbcProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { mysqlContainer.jdbcUrl + "?useSSL=false" }
            registry.add("spring.datasource.username", mysqlContainer::getUsername)
            registry.add("spring.datasource.password", mysqlContainer::getPassword)
            registry.add("spring.flyway.enabled") { true }
            registry.add("spring.flyway.url") { mysqlContainer.jdbcUrl }
        }
    }
}
