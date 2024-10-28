import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

tasks.getByName("bootJar") { enabled = false }
tasks.getByName("jar") { enabled = true }

plugins {
    val kotlinVersion = "1.9.20"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion

    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)

    alias(libs.plugins.jib)
    alias(libs.plugins.ktlint)

    jacoco
}

val projectGroup: String by project
val applicationVersion: String by project

allprojects {
    group = projectGroup
    version = applicationVersion

    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://repo.spring.io/milestone") }
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    dependencies {
        with(rootProject.libs) {
            // Kotlin
            implementation(kotlin("reflect"))
            implementation(kotlinx.coroutines.reactor)
            implementation(kotlin.reactor.extensions)
            implementation(kotlinx.coroutines.slf4j)

            // Others
            runtimeOnly(mysql.connector)
            implementation(jackson.kotlin)
            implementation(kotlin.logging)

            // Logging
            implementation(logback.access)
            implementation(logback.classic)
            implementation(logback.core)
            implementation(logstash.logback.encoder)

            // Test
            testImplementation(spring.boot.starter.test)

            testImplementation(spring.boot.testcontainers)
            testImplementation(testcontainers.junit.jupiter)
            testImplementation(testcontainers.mysql)
            testImplementation(testcontainers.redis)

            testImplementation(kotlinx.coroutines.test)
            testImplementation(kotest.assertions.core.jvm)
            testImplementation(kotest.runner.junit5.jvm)
            testImplementation(kotest.extensions.spring)
            testImplementation(kotest.property.jvm)
            testImplementation(kotest.framework.datatest)
            testImplementation(mockk)
            testImplementation(springmockk)
            testImplementation(archunit.junit5)
        }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "17"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
