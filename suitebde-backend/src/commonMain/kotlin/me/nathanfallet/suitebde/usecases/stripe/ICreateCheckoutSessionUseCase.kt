package me.nathanfallet.suitebde.usecases.stripe

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.stripe.CheckoutItem
import me.nathanfallet.suitebde.models.stripe.CheckoutSession
import me.nathanfallet.suitebde.models.stripe.ICustomer
import me.nathanfallet.usecases.base.IQuadSuspendUseCase

interface ICreateCheckoutSessionUseCase :
    IQuadSuspendUseCase<Association, ICustomer?, List<CheckoutItem>, String, CheckoutSession?>
