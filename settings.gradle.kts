pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "suitebde-backend"
includeBuild("convention-plugins")
include(":suitebde-commons")
include(":suitebde-backend")
