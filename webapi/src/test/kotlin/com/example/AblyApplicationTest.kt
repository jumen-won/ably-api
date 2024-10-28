package com.example

import com.example.testbase.IntegrationTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext

class AblyApplicationTest : IntegrationTest() {

    @Autowired
    lateinit var ctx: ApplicationContext

    @Test
    fun `context loads`() {
        Assertions.assertNotNull(ctx)
    }
}
