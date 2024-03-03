package me.nathanfallet.suitebde.models.users

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import me.nathanfallet.suitebde.models.associations.SubscriptionInAssociation
import me.nathanfallet.usecases.models.IChildModel
import me.nathanfallet.usecases.models.annotations.ModelProperty
import me.nathanfallet.usecases.models.annotations.Schema

@Serializable
data class SubscriptionInUser(
    @ModelProperty("id")
    @Schema("Id of the subscription", "123abc")
    override val id: String,
    @Schema("Id of the user", "123abc")
    val userId: String,
    @Schema("Id of the subscription", "123abc")
    val subscriptionId: String,
    @Schema("Date the subscription starts at", "2023-12-13T09:41:00Z")
    val startsAt: Instant,
    @Schema("Date the subscription ends at", "2023-12-13T09:41:00Z")
    val endsAt: Instant,
    @Schema("Details of the subscription", "{}")
    val subscription: SubscriptionInAssociation?,
) : IChildModel<String, CreateSubscriptionInUserPayload, UpdateSubscriptionInUserPayload, String> {

    override val parentId: String
        get() = userId

}
