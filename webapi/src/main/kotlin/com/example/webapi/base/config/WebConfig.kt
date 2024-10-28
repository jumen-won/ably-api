package com.example.webapi.base.config

import com.example.webapi.base.security.AuthenticationInterceptor
import com.example.webapi.base.security.AuthenticationUserArgumentResolver
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val authenticationUserArgumentResolver: AuthenticationUserArgumentResolver,
    private val authenticationInterceptor: AuthenticationInterceptor
) : WebMvcConfigurer {

    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argumentResolvers.add(authenticationUserArgumentResolver)
        super.addArgumentResolvers(argumentResolvers)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry
            .addMapping("/**")
            .allowedOriginPatterns("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowCredentials(true)
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authenticationInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/users/login", "/users/register", "/swagger-ui/**", "/v3/api-docs/**")
    }

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        val builder = Jackson2ObjectMapperBuilder()
        val converter = MappingJackson2HttpMessageConverter()
        converter.objectMapper = objectMapper()

        builder.serializationInclusion(JsonInclude.Include.NON_NULL)
        builder.serializationInclusion(JsonInclude.Include.NON_EMPTY)
        converters.apply {
            add(converter)
            add(MappingJackson2HttpMessageConverter(builder.build()))
            add(StringHttpMessageConverter())
        }
    }

    @Bean
    fun objectMapper(): ObjectMapper {
        return JsonMapper.builder()
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)
            .configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true)
            .addModule(JavaTimeModule())
            .findAndAddModules()
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
