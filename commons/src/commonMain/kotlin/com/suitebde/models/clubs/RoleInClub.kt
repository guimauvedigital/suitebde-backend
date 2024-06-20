package com.suitebde.models.clubs

import dev.kaccelero.annotations.ModelProperty
import dev.kaccelero.annotations.Schema
import dev.kaccelero.models.IChildModel
import dev.kaccelero.models.UUID
import kotlinx.serialization.Serializable

@Serializable
data class RoleInClub(
    @ModelProperty("id")
    @Schema("Id of the role", "123abc")
    override val id: UUID,
    @Schema("Id of the club the role is in", "123abc")
    val clubId: UUID,
    @ModelProperty("string")
    @Schema("Name of the role", "President")
    val name: String,
    @ModelProperty("boolean")
    @Schema("Is the role an admin role?", "true")
    val admin: Boolean,
    @ModelProperty("boolean")
    @Schema("Is the role a default role?", "true")
    val default: Boolean = false,
) : IChildModel<UUID, CreateRoleInClubPayload, UpdateRoleInClubPayload, UUID> {

    override val parentId: UUID
        get() = clubId

}
