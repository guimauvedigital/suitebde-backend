package com.suitebde.repositories.stripe

import com.suitebde.models.stripe.CreateStripeAccountPayload
import com.suitebde.models.stripe.StripeAccount
import com.suitebde.models.stripe.UpdateStripeAccountPayload
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.IChildModelSuspendRepository

interface IStripeAccountsRepository :
    IChildModelSuspendRepository<StripeAccount, String, CreateStripeAccountPayload, UpdateStripeAccountPayload, UUID>
