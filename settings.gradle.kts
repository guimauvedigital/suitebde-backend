pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            // Plugins
            version("kotlin", "2.0.0")
            plugin("multiplatform", "org.jetbrains.kotlin.multiplatform").versionRef("kotlin")
            plugin("serialization", "org.jetbrains.kotlin.plugin.serialization").versionRef("kotlin")
            plugin("kover", "org.jetbrains.kotlinx.kover").version("0.8.0")
            plugin("ksp", "com.google.devtools.ksp").version("2.0.0-1.0.21")
            plugin("maven", "com.vanniktech.maven.publish").version("0.28.0")

            // Kaccelero
            version("kaccelero", "0.1.0")
            library("kaccelero-ktor-client", "dev.kaccelero", "ktor-routers-client").versionRef("kaccelero")
            library("kaccelero-ktor-auth", "dev.kaccelero", "ktor-auth").versionRef("kaccelero")
            library("kaccelero-ktor-auth-apple", "dev.kaccelero", "ktor-auth-apple").versionRef("kaccelero")
            library("kaccelero-ktor-database", "dev.kaccelero", "ktor-database-sessions").versionRef("kaccelero")
            library("kaccelero-ktor-health", "dev.kaccelero", "ktor-health").versionRef("kaccelero")
            library("kaccelero-ktor-i18n-freemarker", "dev.kaccelero", "ktor-i18n-freemarker").versionRef("kaccelero")
            library("kaccelero-ktor-routers", "dev.kaccelero", "ktor-routers").versionRef("kaccelero")
            library("kaccelero-ktor-routers-locale", "dev.kaccelero", "ktor-routers-locale").versionRef("kaccelero")
            library("kaccelero-ktor-sentry", "dev.kaccelero", "ktor-sentry").versionRef("kaccelero")
            bundle(
                "kaccelero-ktor",
                listOf(
                    "kaccelero-ktor-client",
                    "kaccelero-ktor-auth",
                    "kaccelero-ktor-auth-apple",
                    "kaccelero-ktor-database",
                    "kaccelero-ktor-health",
                    "kaccelero-ktor-i18n-freemarker",
                    "kaccelero-ktor-routers",
                    "kaccelero-ktor-routers-locale",
                    "kaccelero-ktor-sentry"
                )
            )

            // Tests
            library("tests-mockk", "io.mockk:mockk:1.13.11")
            library("tests-h2", "com.h2database:h2:2.2.224")
            library("tests-jsoup", "org.jsoup:jsoup:1.16.2")

            // Ktor
            version("ktor", "2.3.11")
            plugin("ktor", "io.ktor.plugin").versionRef("ktor")
            library("ktor-serialization-kotlinx-json", "io.ktor", "ktor-serialization-kotlinx-json").versionRef("ktor")
            library("ktor-server-core", "io.ktor", "ktor-server-core").versionRef("ktor")
            library("ktor-server-netty", "io.ktor", "ktor-server-netty").versionRef("ktor")
            library("ktor-server-call-logging", "io.ktor", "ktor-server-call-logging").versionRef("ktor")
            library("ktor-server-status-pages", "io.ktor", "ktor-server-status-pages").versionRef("ktor")
            library("ktor-server-content-negotiation", "io.ktor", "ktor-server-content-negotiation").versionRef("ktor")
            library("ktor-server-sessions", "io.ktor", "ktor-server-sessions").versionRef("ktor")
            library("ktor-server-auth-jwt", "io.ktor", "ktor-server-auth-jwt").versionRef("ktor")
            library("ktor-server-freemarker", "io.ktor", "ktor-server-freemarker").versionRef("ktor")
            library("ktor-server-websockets", "io.ktor", "ktor-server-websockets").versionRef("ktor")
            library("ktor-server-test-host", "io.ktor", "ktor-server-test-host").versionRef("ktor")
            library("ktor-client-core", "io.ktor", "ktor-client-core").versionRef("ktor")
            library("ktor-client-apache", "io.ktor", "ktor-client-apache").versionRef("ktor")
            library("ktor-client-jetty", "io.ktor", "ktor-client-jetty").versionRef("ktor")
            library("ktor-client-content-negotiation", "io.ktor", "ktor-client-content-negotiation").versionRef("ktor")
            library("ktor-client-mock", "io.ktor", "ktor-client-mock").versionRef("ktor")
            bundle(
                "ktor-server-api",
                listOf(
                    "ktor-server-core",
                    "ktor-server-netty",
                    "ktor-server-call-logging",
                    "ktor-server-status-pages",
                    "ktor-server-content-negotiation",
                    "ktor-server-sessions",
                    "ktor-server-auth-jwt",
                    "ktor-server-freemarker",
                    "ktor-server-websockets",
                    "ktor-serialization-kotlinx-json"
                )
            )
            bundle(
                "ktor-server-tests",
                listOf(
                    "ktor-server-test-host",
                    "ktor-client-core",
                    "ktor-client-content-negotiation",
                )
            )
            bundle(
                "ktor-client-api",
                listOf(
                    "ktor-client-core",
                    "ktor-client-apache",
                    "ktor-client-jetty",
                    "ktor-client-content-negotiation",
                    "ktor-serialization-kotlinx-json"
                )
            )
            bundle(
                "ktor-client-tests",
                listOf(
                    "ktor-client-mock",
                )
            )

            // Koin
            version("koin", "3.5.0")
            library("koin-core", "io.insert-koin", "koin-core").versionRef("koin")
            library("koin-ktor", "io.insert-koin", "koin-ktor").versionRef("koin")

            // Others
            library("logback-core", "ch.qos.logback:logback-core:0.9.30")
            library("logback-classic", "ch.qos.logback:logback-classic:0.9.30")
            library("gson", "com.google.code.gson:gson:2.10.1")
            library("firebase-admin", "com.google.firebase:firebase-admin:9.2.0")
            library("mysql", "com.mysql:mysql-connector-j:8.0.33")
            library("sentry", "io.sentry:sentry:7.9.0")
        }
    }
}

rootProject.name = "suitebde-backend"
include(":commons")
include(":backend")
