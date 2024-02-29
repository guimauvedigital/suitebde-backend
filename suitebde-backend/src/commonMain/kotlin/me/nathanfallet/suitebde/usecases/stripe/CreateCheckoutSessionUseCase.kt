package me.nathanfallet.suitebde.usecases.stripe

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.stripe.CheckoutItem
import me.nathanfallet.suitebde.models.stripe.CheckoutSession
import me.nathanfallet.suitebde.models.stripe.ICustomer
import me.nathanfallet.suitebde.models.stripe.StripeAccount
import me.nathanfallet.suitebde.services.stripe.IStripeService
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase

class CreateCheckoutSessionUseCase(
    private val stripeService: IStripeService,
    private val listStripeAccountsUseCase: IListChildModelSuspendUseCase<StripeAccount, String>,
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
