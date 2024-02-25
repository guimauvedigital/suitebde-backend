package me.nathanfallet.suitebde.usecases.stripe

import com.stripe.model.checkout.Session
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.StripeAccountInAssociation
import me.nathanfallet.suitebde.models.stripe.CheckoutItem
import me.nathanfallet.suitebde.models.stripe.ICustomer
import me.nathanfallet.suitebde.services.stripe.IStripeService
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase

class CreateCheckoutSessionUseCase(
    private val stripeService: IStripeService,
    private val listStripeAccountsInAssociationsUseCase: IListChildModelSuspendUseCase<StripeAccountInAssociation, String>,
) : ICreateCheckoutSessionUseCase {

    override suspend fun invoke(
        input1: Association,
        input2: ICustomer?,
        input3: List<CheckoutItem>,
        input4: String,
    ): Session? {
        val account = listStripeAccountsInAssociationsUseCase(input1.id).firstOrNull { it.chargesEnabled }
            ?: return null
        return stripeService.createCheckoutSession(
            account.accountId,
            input2,
            input3,
            input4
        )
    }

}
