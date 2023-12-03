package me.nathanfallet.suitebde.models.users

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.IChildModel
import me.nathanfallet.usecases.models.annotations.ModelProperty
import me.nathanfallet.usecases.users.IUser

@Serializable
data class User(
    @ModelProperty("id")
    override val id: String,
    val associationId: String,
    @ModelProperty("email", "12", visibleOnUpdate = true)
    val email: String,
    val password: String?,
    @ModelProperty("string", "6")
    val firstName: String,
    @ModelProperty("string", "6")
    val lastName: String,
    val superuser: Boolean
) : IChildModel<String, CreateUserPayload, UpdateUserPayload, String>, IUser {

    override val parentId: String
        get() = associationId

}
