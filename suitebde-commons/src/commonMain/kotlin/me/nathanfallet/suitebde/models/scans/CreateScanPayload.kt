package me.nathanfallet.suitebde.models.scans

import kotlinx.serialization.Serializable

@Serializable
data class CreateScanPayload(
    val userId: String,
)
