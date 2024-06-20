package com.suitebde.models.roles

import dev.kaccelero.annotations.ModelProperty
import dev.kaccelero.annotations.Schema
import dev.kaccelero.models.IChildModel
import dev.kaccelero.models.UUID
import kotlinx.serialization.Serializable

@Serializable
data class Role(
    @ModelProperty("id")
    @Schema("Id of the role", "123abc")
    override val id: UUID,
    @Schema("Id of the association the role is in", "123abc")
    val associationId: UUID,
    @ModelProperty("string")
    @Schema("Name of the role", "Respo Web")
    val name: String,
) : IChildModel<UUID, CreateRolePayload, UpdateRolePayload, UUID> {

    override val parentId: UUID
        get() = associationId

}
