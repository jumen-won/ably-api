tasks.getByName("bootJar") { enabled = false }
tasks.getByName("jar") { enabled = true }

dependencies {
}

kotlin {
    jvmToolchain(17)
}
