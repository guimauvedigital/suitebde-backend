package me.nathanfallet.suitebde.repositories.users

import me.nathanfallet.suitebde.models.users.SubscriptionInUser
import me.nathanfallet.usecases.pagination.Pagination

interface ISubscriptionsInUsersRemoteRepository {

    suspend fun list(pagination: Pagination, userId: String, associationId: String): List<SubscriptionInUser>

}
