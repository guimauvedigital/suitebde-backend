package com.suitebde.models.roles

import dev.kaccelero.commons.permissions.IPermission
import dev.kaccelero.models.UUID

data class PermissionInAssociation(
    val permission: Permission,
    val associationId: UUID,
) : IPermission
