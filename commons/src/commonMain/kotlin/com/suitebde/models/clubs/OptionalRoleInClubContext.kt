package com.suitebde.models.clubs

import dev.kaccelero.models.IContext
import dev.kaccelero.models.UUID

data class OptionalRoleInClubContext(
    val roleId: UUID? = null,
) : IContext
