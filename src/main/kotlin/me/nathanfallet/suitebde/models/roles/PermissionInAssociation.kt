package me.nathanfallet.suitebde.models.roles

import me.nathanfallet.suitebde.models.associations.Association

data class PermissionInAssociation(
    val permission: Permission,
    val association: Association
) : IPermission
