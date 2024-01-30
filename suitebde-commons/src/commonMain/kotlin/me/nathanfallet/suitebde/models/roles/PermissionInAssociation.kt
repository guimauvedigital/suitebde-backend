package me.nathanfallet.suitebde.models.roles

import me.nathanfallet.usecases.permissions.IPermission

data class PermissionInAssociation(
    val permission: Permission,
    val associationId: String,
) : IPermission
