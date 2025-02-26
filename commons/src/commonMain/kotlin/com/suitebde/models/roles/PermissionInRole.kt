package com.suitebde.models.roles

import dev.kaccelero.annotations.Schema
import dev.kaccelero.models.IChildModel
import dev.kaccelero.models.UUID
import kotlinx.serialization.Serializable

@Serializable
data class PermissionInRole(
    @Schema("Id of the role", "123abc")
    val roleId: UUID,
    @Schema("Permission name", "USERS_UPDATE")
    val permission: Permission,
) : IChildModel<Permission, CreatePermissionInRolePayload, Unit, UUID> {

    override val id: Permission
        get() = permission

    override val parentId: UUID
        get() = roleId

}
