package com.suitebde.models.users

import dev.kaccelero.models.IContext
import dev.kaccelero.models.UUID

data class OptionalUserContext(
    val userId: UUID? = null,
) : IContext
