package me.nathanfallet.suitebde.models.models

import kotlinx.serialization.Serializable

@Serializable
data class ModelKey(
    val name: String,
    val type: ModelKeyType,
    val col: Int = 12,
    val editable: Boolean = true
)
