package me.nathanfallet.suitebde.usecases.stripe

import com.stripe.param.AccountLinkCreateParams
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.stripe.CreateStripeAccountPayload
import me.nathanfallet.suitebde.models.stripe.StripeAccount
import me.nathanfallet.suitebde.services.stripe.IStripeService
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase

class CreateStripeAccountLinkUseCase(
    private val stripeService: IStripeService,
    private val listStripeAccountsUseCase: IListChildModelSuspendUseCase<StripeAccount, String>,
    private val createStripeAccountUseCase: ICreateChildModelSuspendUseCase<StripeAccount, CreateStripeAccountPayload, String>,
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
