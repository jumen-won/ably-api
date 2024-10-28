package com.example.webapi.base.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.models.OpenAPI
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@OpenAPIDefinition(
    info = Info(
        title = "Ably Server API 명세서",
        description = "API 명세서",
        version = "v1"
    )
)
@Configuration
class OpenApi30Config {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
    }
}
