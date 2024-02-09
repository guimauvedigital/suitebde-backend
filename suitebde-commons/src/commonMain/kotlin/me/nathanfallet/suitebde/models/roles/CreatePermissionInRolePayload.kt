package me.nathanfallet.suitebde.models.roles

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.annotations.Schema

@Serializable
data class CreatePermissionInRolePayload(
    @Schema("Permission name", "USERS_UPDATE")
    val permission: Permission,
)
