package com.suitebde.models.clubs

import com.suitebde.models.users.User
import dev.kaccelero.models.IChildModel
import dev.kaccelero.models.UUID
import kotlinx.serialization.Serializable

@Serializable
data class UserInClub(
    val userId: UUID,
    val clubId: UUID,
    val roleId: UUID,
    val user: User? = null,
    val club: Club? = null,
    val role: RoleInClub,
) : IChildModel<UUID, CreateUserInClubPayload, Unit, UUID> {

    override val id: UUID
        get() = userId

    override val parentId: UUID
        get() = clubId

}
