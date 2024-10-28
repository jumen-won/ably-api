import org.springframework.boot.gradle.tasks.bundling.BootJar

tasks.getByName("bootJar") { enabled = false }
tasks.getByName("jar") { enabled = true }

plugins {
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    kotlin("jvm")
}

dependencies {
    with(rootProject.libs) {
        implementation(project(":common"))

        // Spring
        implementation(spring.boot.starter.data.jpa)

        // Flyway
        implementation(flyway.core)
        implementation(flyway.mysql)
    }
}

tasks.getByName<BootJar>("bootJar") {
    launchScript()
}
repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(17)
}
