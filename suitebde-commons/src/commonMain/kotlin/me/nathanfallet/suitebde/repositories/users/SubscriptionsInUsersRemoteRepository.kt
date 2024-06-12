package me.nathanfallet.suitebde.repositories.users

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.repositories.api.APIChildModelRemoteRepository
import me.nathanfallet.ktorx.repositories.api.IAPIChildModelRemoteRepository
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.users.CreateSubscriptionInUserPayload
import me.nathanfallet.suitebde.models.users.SubscriptionInUser
import me.nathanfallet.suitebde.models.users.UpdateSubscriptionInUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.models.id.RecursiveId

class SubscriptionsInUsersRemoteRepository(
    client: ISuiteBDEClient,
    parentRepository: IAPIChildModelRemoteRepository<User, String, *, *, *>,
) : APIChildModelRemoteRepository<SubscriptionInUser, String, CreateSubscriptionInUserPayload, UpdateSubscriptionInUserPayload, String>(
    typeInfo<SubscriptionInUser>(),
    typeInfo<CreateSubscriptionInUserPayload>(),
    typeInfo<UpdateSubscriptionInUserPayload>(),
    typeInfo<List<SubscriptionInUser>>(),
    client,
    parentRepository,
    route = "subscriptions",
    prefix = "/api/v1"
), ISubscriptionsInUsersRemoteRepository {

    override suspend fun list(userId: String, associationId: String): List<SubscriptionInUser> =
        list(
            RecursiveId<User, String, String>(userId, RecursiveId<Association, String, Unit>(associationId)),
            null
        )

    override suspend fun create(
        payload: CreateSubscriptionInUserPayload,
        userId: String,
        associationId: String,
    ): SubscriptionInUser? =
        create(
            payload,
            RecursiveId<User, String, String>(userId, RecursiveId<Association, String, Unit>(associationId)),
            null
        )

    override suspend fun update(
        id: String,
        payload: UpdateSubscriptionInUserPayload,
        userId: String,
        associationId: String,
    ): SubscriptionInUser? =
        update(
            id,
            payload,
            RecursiveId<User, String, String>(userId, RecursiveId<Association, String, Unit>(associationId)),
            null
        )

}
