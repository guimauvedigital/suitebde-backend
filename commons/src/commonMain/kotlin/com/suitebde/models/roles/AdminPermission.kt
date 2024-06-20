package com.suitebde.models.roles

import dev.kaccelero.commons.permissions.IPermission
import dev.kaccelero.models.UUID

object AdminPermission : IPermission {

    val adminAssociationId = UUID("00000000-0000-0000-0000-000000000000")

}
