package com.example.webapi

import com.example.webapi.SystemController.Companion.PATH_API_SYSTEM
import io.swagger.v3.oas.annotations.Operation
import org.springframework.boot.info.BuildProperties
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(PATH_API_SYSTEM)
class SystemController(
    private val buildProperties: BuildProperties
) {

    @GetMapping("/index")
    fun ping(): String = "pong"

    @Operation(summary = "빌드 정보 조회 API", description = "어플리케이션 빌드 정보를 조회합니다.")
    @GetMapping("/buildInfo")
    fun buildInfo(): String = "${buildProperties.group}.${buildProperties.artifact}:${buildProperties.version}"

    companion object {
        private const val SERVICE_NAME = "system"
        const val PATH_API_SYSTEM = "/$SERVICE_NAME"
    }
}
