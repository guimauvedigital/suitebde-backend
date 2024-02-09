package me.nathanfallet.suitebde.repositories.users

import me.nathanfallet.suitebde.models.users.SubscriptionInUser

interface ISubscriptionsInUsersRemoteRepository {

    suspend fun list(limit: Long, offset: Long, userId: String, associationId: String): List<SubscriptionInUser>

}
