package com.suitebde.models.users

import com.suitebde.models.associations.SubscriptionInAssociation
import dev.kaccelero.annotations.ModelProperty
import dev.kaccelero.annotations.Schema
import dev.kaccelero.models.IChildModel
import dev.kaccelero.models.UUID
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionInUser(
    @ModelProperty("id")
    @Schema("Id of the subscription", "123abc")
    override val id: UUID,
    @Schema("Id of the user", "123abc")
    val userId: UUID,
    @Schema("Id of the subscription", "123abc")
    val subscriptionId: UUID,
    @Schema("Date the subscription starts at", "2023-12-13T09:41:00Z")
    val startsAt: Instant,
    @Schema("Date the subscription ends at", "2023-12-13T09:41:00Z")
    val endsAt: Instant,
    @Schema("Details of the subscription", "{}")
    val subscription: SubscriptionInAssociation?,
) : IChildModel<UUID, CreateSubscriptionInUserPayload, UpdateSubscriptionInUserPayload, UUID> {

    override val parentId: UUID
        get() = userId

}
