package com.suitebde.repositories.users

import com.suitebde.models.users.CreateSubscriptionInUserPayload
import com.suitebde.models.users.SubscriptionInUser
import com.suitebde.models.users.UpdateSubscriptionInUserPayload
import dev.kaccelero.models.UUID

interface ISubscriptionsInUsersRemoteRepository {

    suspend fun list(userId: UUID, associationId: UUID): List<SubscriptionInUser>

    suspend fun create(
        payload: CreateSubscriptionInUserPayload,
        userId: UUID,
        associationId: UUID,
    ): SubscriptionInUser?

    suspend fun update(
        id: UUID,
        payload: UpdateSubscriptionInUserPayload,
        userId: UUID,
        associationId: UUID,
    ): SubscriptionInUser?

}
