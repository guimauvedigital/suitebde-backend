package me.nathanfallet.suitebde.models.users

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.annotations.Schema

@Serializable
data class CreateSubscriptionInUserPayload(
    @Schema("Id of the subscription", "123abc")
    val subscriptionId: String,
    @Schema("Date the subscription starts at", "2023-12-13T09:41:00Z")
    val startsAt: Instant,
    @Schema("Date the subscription ends at", "2023-12-13T09:41:00Z")
    val endsAt: Instant,
)
