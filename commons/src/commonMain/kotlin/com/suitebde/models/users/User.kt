package com.suitebde.models.users

import com.suitebde.models.stripe.ICustomer
import dev.kaccelero.annotations.ModelProperty
import dev.kaccelero.annotations.Schema
import dev.kaccelero.models.IChildModel
import dev.kaccelero.models.IUser
import dev.kaccelero.models.UUID
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @ModelProperty("id")
    @Schema("Id of the User", "123abc")
    override val id: UUID,
    @Schema("Id of the association the user is in", "123abc")
    val associationId: UUID,
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
) : IChildModel<UUID, CreateUserPayload, UpdateUserPayload, UUID>, IUser, ICustomer {

    override val parentId: UUID
        get() = associationId

}
