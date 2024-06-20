package com.suitebde.usecases.stripe

import com.stripe.param.AccountLinkCreateParams
import com.suitebde.models.associations.Association
import com.suitebde.models.stripe.CreateStripeAccountPayload
import com.suitebde.models.stripe.StripeAccount
import com.suitebde.services.stripe.IStripeService
import dev.kaccelero.commons.repositories.ICreateChildModelSuspendUseCase
import dev.kaccelero.commons.repositories.IListChildModelSuspendUseCase
import dev.kaccelero.models.UUID

class CreateStripeAccountLinkUseCase(
    private val stripeService: IStripeService,
    private val listStripeAccountsUseCase: IListChildModelSuspendUseCase<StripeAccount, UUID>,
    private val createStripeAccountUseCase: ICreateChildModelSuspendUseCase<StripeAccount, CreateStripeAccountPayload, UUID>,
) : ICreateStripeAccountLinkUseCase {

    override suspend fun invoke(input1: Association, input2: String): String? {
        val account = listStripeAccountsUseCase(input1.id).firstOrNull()
            ?: stripeService.createAccount(input1).let {
                createStripeAccountUseCase(
                    CreateStripeAccountPayload(
                        accountId = it.id,
                        chargesEnabled = it.chargesEnabled
                    ),
                    input1.id
                )
            }
            ?: return null
        return stripeService.createAccountLink(
            account.accountId,
            if (account.chargesEnabled) AccountLinkCreateParams.Type.ACCOUNT_UPDATE else AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING,
            input2
        ).url
    }

}
