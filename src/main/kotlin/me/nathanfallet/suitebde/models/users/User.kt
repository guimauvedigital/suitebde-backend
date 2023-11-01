package me.nathanfallet.suitebde.models.users

import kotlinx.serialization.Serializable
import me.nathanfallet.ktor.routers.models.base.ModelProperty

@Serializable
data class User(
    @ModelProperty("id")
    val id: String,
    val associationId: String,
    @ModelProperty("email", "12", visibleOnUpdate = true)
    val email: String,
    val password: String?,
    @ModelProperty("string", "6")
    val firstName: String,
    @ModelProperty("string", "6")
    val lastName: String,
    val superuser: Boolean
)
