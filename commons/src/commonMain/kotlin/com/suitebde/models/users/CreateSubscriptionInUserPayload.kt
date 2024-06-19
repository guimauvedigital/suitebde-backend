package com.suitebde.models.users

import dev.kaccelero.annotations.Schema
import dev.kaccelero.models.UUID
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class CreateSubscriptionInUserPayload(
    @Schema("Id of the subscription", "123abc")
    val subscriptionId: UUID,
    @Schema("Date the subscription starts at", "2023-12-13T09:41:00Z")
    val startsAt: Instant,
    @Schema("Date the subscription ends at", "2023-12-13T09:41:00Z")
    val endsAt: Instant,
)
