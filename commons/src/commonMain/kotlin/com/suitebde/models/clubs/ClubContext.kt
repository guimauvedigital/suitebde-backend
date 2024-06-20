package com.suitebde.models.clubs

import dev.kaccelero.models.IContext
import dev.kaccelero.models.UUID

data class ClubContext(
    val userId: UUID? = null,
    val onlyShowValidated: Boolean = true,
) : IContext
