package com.suitebde.models.users

import dev.kaccelero.annotations.Schema
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class UpdateSubscriptionInUserPayload(
    @Schema("Date the subscription ends at", "2023-12-13T09:41:00Z")
    val endsAt: Instant,
)
