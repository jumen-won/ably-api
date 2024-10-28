plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "ably-api"

include(
    "common",
    "domain",
    "webapi"
)

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("./buildSrc/libs.versions.toml"))
        }
    }
}
