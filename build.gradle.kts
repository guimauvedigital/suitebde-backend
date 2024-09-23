plugins {
    alias(libs.plugins.multiplatform) apply false
}

allprojects {
    group = "com.suitebde"
    version = "0.1.3"

    repositories {
        mavenCentral()
    }
}
