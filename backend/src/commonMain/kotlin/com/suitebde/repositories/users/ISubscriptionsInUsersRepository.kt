package com.suitebde.repositories.users

import com.suitebde.models.users.CreateSubscriptionInUserPayload
import com.suitebde.models.users.SubscriptionInUser
import com.suitebde.models.users.UpdateSubscriptionInUserPayload
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.IChildModelSuspendRepository

interface ISubscriptionsInUsersRepository :
    IChildModelSuspendRepository<SubscriptionInUser, UUID, CreateSubscriptionInUserPayload, UpdateSubscriptionInUserPayload, UUID>
