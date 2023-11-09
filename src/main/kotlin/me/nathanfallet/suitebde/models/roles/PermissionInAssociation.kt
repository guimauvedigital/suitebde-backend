package me.nathanfallet.suitebde.models.roles

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.usecases.permissions.IPermission

data class PermissionInAssociation(
    val permission: Permission,
    val association: Association
) : IPermission
