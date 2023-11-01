package me.nathanfallet.suitebde.models.associations

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import me.nathanfallet.ktor.routers.models.base.ModelProperty

@Serializable
data class Association(
    @ModelProperty("id")
    val id: String,
    @ModelProperty("string")
    val name: String,
    @ModelProperty("string")
    val school: String,
    @ModelProperty("string")
    val city: String,
    @ModelProperty("boolean")
    val validated: Boolean,
    val createdAt: Instant,
    val expiresAt: Instant
)
