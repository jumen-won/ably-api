import org.springframework.boot.gradle.tasks.bundling.BootJar

tasks.getByName("bootJar") { enabled = true }
tasks.getByName("jar") { enabled = true }

plugins {
    kotlin("plugin.spring")
    kotlin("jvm")
}

dependencies {
    with(rootProject.libs) {
        implementation(project(":common"))
        implementation(project(":domain"))

        // Spring
        implementation(spring.boot.starter.web)
        implementation(spring.security.crypto)

        implementation(spring.boot.starter.actuator)

        implementation(springdoc.openapi.starter.webmvc.ui)

        implementation("io.jsonwebtoken:jjwt:0.12.6")
        implementation(java.jwt)

        testImplementation(spring.boot.starter.data.jpa)
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

springBoot {
    buildInfo()
}
