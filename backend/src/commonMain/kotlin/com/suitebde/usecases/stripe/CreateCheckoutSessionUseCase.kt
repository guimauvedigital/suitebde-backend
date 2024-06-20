package com.suitebde.usecases.stripe

import com.suitebde.models.associations.Association
import com.suitebde.models.stripe.CheckoutItem
import com.suitebde.models.stripe.CheckoutSession
import com.suitebde.models.stripe.ICustomer
import com.suitebde.models.stripe.StripeAccount
import com.suitebde.services.stripe.IStripeService
import dev.kaccelero.commons.repositories.IListChildModelSuspendUseCase
import dev.kaccelero.models.UUID

class CreateCheckoutSessionUseCase(
    private val stripeService: IStripeService,
    private val listStripeAccountsUseCase: IListChildModelSuspendUseCase<StripeAccount, UUID>,
) : ICreateCheckoutSessionUseCase {

    override suspend fun invoke(
        input1: Association,
        input2: ICustomer?,
        input3: List<CheckoutItem>,
        input4: String,
    ): CheckoutSession? {
        val account = listStripeAccountsUseCase(input1.id).firstOrNull { it.chargesEnabled }
            ?: return null
        return stripeService.createCheckoutSession(
            account,
            input2,
            input3,
            input4
        ).let {
            CheckoutSession(
                it.id,
                it.url
            )
        }
    }

}
