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
import me.nathanfallet.usecases.pagination.Pagination

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

    override suspend fun list(
        pagination: Pagination,
        userId: String,
        associationId: String,
    ): List<SubscriptionInUser> =
        list(
            pagination,
            RecursiveId<User, String, String>(userId, RecursiveId<Association, String, Unit>(associationId)),
            null
        )

}
