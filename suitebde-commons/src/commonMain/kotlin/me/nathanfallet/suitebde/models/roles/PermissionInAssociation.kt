package me.nathanfallet.suitebde.models.roles

import me.nathanfallet.usecases.permissions.IPermission

data class PermissionInAssociation(
    val permission: me.nathanfallet.suitebde.models.roles.Permission,
    val association: me.nathanfallet.suitebde.models.associations.Association
) : IPermission
