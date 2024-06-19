package com.suitebde.usecases.stripe

import com.suitebde.models.associations.Association
import com.suitebde.models.stripe.CheckoutItem
import dev.kaccelero.usecases.ITripleSuspendUseCase

interface IFulfillCheckoutItemUseCase : ITripleSuspendUseCase<Association, String, CheckoutItem, Unit>
