package com.suitebde.repositories.stripe

import com.suitebde.models.stripe.CreateStripeOrderPayload
import com.suitebde.models.stripe.StripeOrder
import com.suitebde.models.stripe.UpdateStripeOrderPayload
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.IChildModelSuspendRepository

interface IStripeOrdersRepository :
    IChildModelSuspendRepository<StripeOrder, String, CreateStripeOrderPayload, UpdateStripeOrderPayload, UUID>
