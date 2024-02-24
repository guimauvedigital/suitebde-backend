package me.nathanfallet.suitebde.usecases.associations

import com.stripe.model.checkout.Session
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.StripeAccountInAssociation
import me.nathanfallet.suitebde.services.stripe.IStripeService
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase

class CreateCheckoutSessionUseCase(
    private val stripeService: IStripeService,
    private val listStripeAccountsInAssociationsUseCase: IListChildModelSuspendUseCase<StripeAccountInAssociation, String>,
) : ICreateCheckoutSessionUseCase {

    override suspend fun invoke(input1: Association, input2: String, input3: Long): Session? {
        val account = listStripeAccountsInAssociationsUseCase(input1.id).firstOrNull { it.chargesEnabled }
            ?: return null
        return stripeService.createCheckoutSession(
            account.accountId,
            input2,
            input3,
            ""
        )
    }

}
