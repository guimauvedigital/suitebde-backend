package me.nathanfallet.suitebde.usecases.stripe

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.stripe.StripeAccount
import me.nathanfallet.suitebde.models.stripe.UpdateStripeAccountPayload
import me.nathanfallet.suitebde.services.stripe.IStripeService
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase
import me.nathanfallet.usecases.models.update.IUpdateChildModelSuspendUseCase

class RefreshStripeAccountUseCase(
    private val stripeService: IStripeService,
    private val listStripeAccountsUseCase: IListChildModelSuspendUseCase<StripeAccount, String>,
    private val updateStripeAccountUseCase: IUpdateChildModelSuspendUseCase<StripeAccount, String, UpdateStripeAccountPayload, String>,
) : IRefreshStripeAccountUseCase {

    override suspend fun invoke(input: Association) {
        listStripeAccountsUseCase(input.id).forEach {
            if (!it.chargesEnabled && stripeService.getAccount(it.accountId)?.chargesEnabled == true)
                updateStripeAccountUseCase(
                    it.accountId,
                    UpdateStripeAccountPayload(
                        chargesEnabled = true
                    ),
                    input.id
                )
        }
    }

}
