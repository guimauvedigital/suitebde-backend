package me.nathanfallet.suitebde.models.users

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
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
    val createdAt: Instant,
    @Schema("Date the subscription ends at", "2023-12-13T09:41:00Z")
    val expiresAt: Instant,
) : IChildModel<String, Unit, Unit, String> {

    override val parentId: String
        get() = userId

}
