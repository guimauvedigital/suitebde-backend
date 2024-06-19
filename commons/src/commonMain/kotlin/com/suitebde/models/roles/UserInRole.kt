package com.suitebde.models.roles

import com.suitebde.models.users.User
import dev.kaccelero.models.IChildModel
import dev.kaccelero.models.UUID
import kotlinx.serialization.Serializable

@Serializable
data class UserInRole(
    val roleId: UUID,
    val userId: UUID,
    val role: Role?,
    val user: User?,
) : IChildModel<UUID, CreateUserInRolePayload, Unit, UUID> {

    override val id: UUID
        get() = userId

    override val parentId: UUID
        get() = roleId

}
