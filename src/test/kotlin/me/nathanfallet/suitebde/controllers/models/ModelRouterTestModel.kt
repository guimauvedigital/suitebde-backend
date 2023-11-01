package me.nathanfallet.suitebde.controllers.models

import kotlinx.serialization.Serializable
import me.nathanfallet.ktor.routers.models.base.ModelProperty
import me.nathanfallet.ktor.routers.models.base.PayloadProperty

@Serializable
data class ModelRouterTestModel(
    @ModelProperty("id", "12", visibleOnUpdate = true)
    val id: String,
    @ModelProperty("string") @PayloadProperty("string", "12")
    val string: String,
)
