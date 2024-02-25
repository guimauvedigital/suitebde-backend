package me.nathanfallet.suitebde.repositories.stripe

import me.nathanfallet.suitebde.models.stripe.CreateStripeOrderPayload
import me.nathanfallet.suitebde.models.stripe.StripeOrder
import me.nathanfallet.suitebde.models.stripe.UpdateStripeOrderPayload
import me.nathanfallet.usecases.models.repositories.IChildModelSuspendRepository

interface IStripeOrdersRepository :
    IChildModelSuspendRepository<StripeOrder, String, CreateStripeOrderPayload, UpdateStripeOrderPayload, String>
