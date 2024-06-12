package me.nathanfallet.suitebde.repositories.users

import me.nathanfallet.suitebde.models.users.CreateSubscriptionInUserPayload
import me.nathanfallet.suitebde.models.users.SubscriptionInUser
import me.nathanfallet.suitebde.models.users.UpdateSubscriptionInUserPayload

interface ISubscriptionsInUsersRemoteRepository {

    suspend fun list(userId: String, associationId: String): List<SubscriptionInUser>

    suspend fun create(
        payload: CreateSubscriptionInUserPayload,
        userId: String,
        associationId: String,
    ): SubscriptionInUser?

    suspend fun update(
        id: String,
        payload: UpdateSubscriptionInUserPayload,
        userId: String,
        associationId: String,
    ): SubscriptionInUser?

}
