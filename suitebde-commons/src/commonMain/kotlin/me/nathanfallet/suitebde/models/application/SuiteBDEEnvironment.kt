package me.nathanfallet.suitebde.models.application

enum class SuiteBDEEnvironment {

    PRODUCTION, DEVELOPMENT;

    val baseUrl: String
        get() = when (this) {
            PRODUCTION -> "https://suitebde.com"
            DEVELOPMENT -> "https://suitebde.dev"
        }

}
