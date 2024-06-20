package com.suitebde.models.roles

import dev.kaccelero.annotations.Schema
import kotlinx.serialization.Serializable

@Serializable
data class CreatePermissionInRolePayload(
    @Schema("Permission name", "USERS_UPDATE")
    val permission: Permission,
)
