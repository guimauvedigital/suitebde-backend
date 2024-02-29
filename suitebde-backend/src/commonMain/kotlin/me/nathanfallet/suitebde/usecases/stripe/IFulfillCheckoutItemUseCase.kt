package me.nathanfallet.suitebde.usecases.stripe

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.stripe.CheckoutItem
import me.nathanfallet.usecases.base.ITripleSuspendUseCase

interface IFulfillCheckoutItemUseCase : ITripleSuspendUseCase<Association, String, CheckoutItem, Unit>
