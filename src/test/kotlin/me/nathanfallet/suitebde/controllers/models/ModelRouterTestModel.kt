package me.nathanfallet.suitebde.controllers.models

import kotlinx.serialization.Serializable

@Serializable
data class ModelRouterTestModel(
    val id: String,
    val string: String,
)
