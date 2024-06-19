plugins {
    alias(libs.plugins.multiplatform) apply false
}

allprojects {
    group = "com.suitebde"
    version = "0.1.0"

    repositories {
        mavenCentral()
    }
}
