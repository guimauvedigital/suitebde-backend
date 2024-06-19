package com.suitebde.repositories.users

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.associations.Association
import com.suitebde.models.users.CreateSubscriptionInUserPayload
import com.suitebde.models.users.SubscriptionInUser
import com.suitebde.models.users.UpdateSubscriptionInUserPayload
import com.suitebde.models.users.User
import dev.kaccelero.models.RecursiveId
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.APIChildModelRemoteRepository
import dev.kaccelero.repositories.IAPIChildModelRemoteRepository
import io.ktor.util.reflect.*

class SubscriptionsInUsersRemoteRepository(
    client: ISuiteBDEClient,
    parentRepository: IAPIChildModelRemoteRepository<User, UUID, *, *, *>,
) : APIChildModelRemoteRepository<SubscriptionInUser, UUID, CreateSubscriptionInUserPayload, UpdateSubscriptionInUserPayload, UUID>(
    typeInfo<SubscriptionInUser>(),
    typeInfo<CreateSubscriptionInUserPayload>(),
    typeInfo<UpdateSubscriptionInUserPayload>(),
    typeInfo<List<SubscriptionInUser>>(),
    client,
    parentRepository,
    route = "subscriptions",
    prefix = "/api/v1"
), ISubscriptionsInUsersRemoteRepository {

    override suspend fun list(userId: UUID, associationId: UUID): List<SubscriptionInUser> =
        list(
            RecursiveId<User, UUID, UUID>(userId, RecursiveId<Association, UUID, Unit>(associationId)),
            null
        )

    override suspend fun create(
        payload: CreateSubscriptionInUserPayload,
        userId: UUID,
        associationId: UUID,
    ): SubscriptionInUser? =
        create(
            payload,
            RecursiveId<User, UUID, UUID>(userId, RecursiveId<Association, UUID, Unit>(associationId)),
            null
        )

    override suspend fun update(
        id: UUID,
        payload: UpdateSubscriptionInUserPayload,
        userId: UUID,
        associationId: UUID,
    ): SubscriptionInUser? =
        update(
            id,
            payload,
            RecursiveId<User, UUID, UUID>(userId, RecursiveId<Association, UUID, Unit>(associationId)),
            null
        )

}
