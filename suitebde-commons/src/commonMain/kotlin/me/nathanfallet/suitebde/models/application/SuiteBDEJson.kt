package me.nathanfallet.suitebde.models.application

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

object SuiteBDEJson {

    @OptIn(ExperimentalSerializationApi::class)
    val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

}
