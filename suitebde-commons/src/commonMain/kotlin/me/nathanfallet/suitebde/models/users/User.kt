package me.nathanfallet.suitebde.models.users

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import me.nathanfallet.suitebde.models.stripe.ICustomer
import me.nathanfallet.usecases.models.IChildModel
import me.nathanfallet.usecases.models.annotations.ModelProperty
import me.nathanfallet.usecases.models.annotations.Schema
import me.nathanfallet.usecases.users.IUser

@Serializable
data class User(
    @ModelProperty("id")
    @Schema("Id of the User", "123abc")
    override val id: String,
    @Schema("Id of the association the user is in", "123abc")
    val associationId: String,
    @ModelProperty("email", "12", visibleOnUpdate = true)
    @Schema("Email of the User", "nathan.fallet@uha.fr")
    override val email: String,
    val password: String?,
    @ModelProperty("string", "6")
    @Schema("First name of the User", "Nathan")
    val firstName: String,
    @ModelProperty("string", "6")
    @Schema("Last name of the User", "Fallet")
    val lastName: String,
    @Schema("Is the user a super user in the association?", "true")
    val superuser: Boolean,
    @Schema("Last login date of the profile", "2021-01-01T00:00:00Z")
    val lastLoginAt: Instant,
) : IChildModel<String, CreateUserPayload, UpdateUserPayload, String>, IUser, ICustomer {

    override val parentId: String
        get() = associationId

}
