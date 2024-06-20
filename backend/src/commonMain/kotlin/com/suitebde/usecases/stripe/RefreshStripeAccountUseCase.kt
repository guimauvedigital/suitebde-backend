package com.suitebde.usecases.stripe

import com.suitebde.models.associations.Association
import com.suitebde.models.stripe.StripeAccount
import com.suitebde.models.stripe.UpdateStripeAccountPayload
import com.suitebde.services.stripe.IStripeService
import dev.kaccelero.commons.repositories.IListChildModelSuspendUseCase
import dev.kaccelero.commons.repositories.IUpdateChildModelSuspendUseCase
import dev.kaccelero.models.UUID

class RefreshStripeAccountUseCase(
    private val stripeService: IStripeService,
    private val listStripeAccountsUseCase: IListChildModelSuspendUseCase<StripeAccount, UUID>,
    private val updateStripeAccountUseCase: IUpdateChildModelSuspendUseCase<StripeAccount, String, UpdateStripeAccountPayload, UUID>,
) : IRefreshStripeAccountUseCase {

    override suspend fun invoke(input: Association): List<StripeAccount> =
        listStripeAccountsUseCase(input.id).map {
            if (!it.chargesEnabled && stripeService.getAccount(it.accountId).chargesEnabled == true)
                updateStripeAccountUseCase(
                    it.accountId,
                    UpdateStripeAccountPayload(
                        chargesEnabled = true
                    ),
                    input.id
                ) ?: it
            else it
        }

}
