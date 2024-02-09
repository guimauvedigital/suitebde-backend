package me.nathanfallet.suitebde.repositories.users

import me.nathanfallet.suitebde.models.users.CreateSubscriptionInUserPayload
import me.nathanfallet.suitebde.models.users.SubscriptionInUser
import me.nathanfallet.suitebde.models.users.UpdateSubscriptionInUserPayload
import me.nathanfallet.usecases.models.repositories.IChildModelSuspendRepository

interface ISubscriptionsInUsersRepository :
    IChildModelSuspendRepository<SubscriptionInUser, String, CreateSubscriptionInUserPayload, UpdateSubscriptionInUserPayload, String>
