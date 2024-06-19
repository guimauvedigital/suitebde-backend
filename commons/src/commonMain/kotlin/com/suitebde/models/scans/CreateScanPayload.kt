package com.suitebde.models.scans

import dev.kaccelero.models.UUID
import kotlinx.serialization.Serializable

@Serializable
data class CreateScanPayload(
    val userId: UUID,
)
