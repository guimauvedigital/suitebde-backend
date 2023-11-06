package me.nathanfallet.suitebde.controllers.models

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.IModel
import me.nathanfallet.usecases.models.annotations.ModelProperty
import me.nathanfallet.usecases.models.annotations.PayloadProperty

@Serializable
data class ModelRouterTestModel(
    @ModelProperty("id", "12", visibleOnUpdate = true)
    override val id: String,
    @ModelProperty("string") @PayloadProperty("string", "12")
    val string: String,
) : IModel<String, ModelRouterTestModel, ModelRouterTestModel>
