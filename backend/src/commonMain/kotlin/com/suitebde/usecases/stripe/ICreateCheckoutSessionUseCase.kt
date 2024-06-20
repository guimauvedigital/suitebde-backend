package com.suitebde.usecases.stripe

import com.suitebde.models.associations.Association
import com.suitebde.models.stripe.CheckoutItem
import com.suitebde.models.stripe.CheckoutSession
import com.suitebde.models.stripe.ICustomer
import dev.kaccelero.usecases.IQuadSuspendUseCase

interface ICreateCheckoutSessionUseCase :
    IQuadSuspendUseCase<Association, ICustomer?, List<CheckoutItem>, String, CheckoutSession?>
