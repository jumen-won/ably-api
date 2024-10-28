package com.example

import com.example.domain.usecase.ItemUseCase
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource

@ConfigurationPropertiesScan(basePackages = ["com.example"])
@SpringBootApplication
class AblyApplication {

    @Bean
    fun init(itemUseCase: ItemUseCase) = CommandLineRunner {
        val resource = ClassPathResource("dummy_product.csv")
        val commands = resource.inputStream.bufferedReader().use { reader ->
            reader.readLines().drop(1).map { line ->
                val (code, url, price) = line.split(",")
                ItemUseCase.Command.Create(code, url, price.toInt())
            }
        }

        itemUseCase.init(commands)
    }
}

fun main(args: Array<String>) {
    runApplication<AblyApplication>(*args)
}
