package me.nathanfallet.suitebde.services.stripe

import com.stripe.Stripe
import com.stripe.model.Account
import com.stripe.model.AccountLink
import com.stripe.param.AccountCreateParams
import com.stripe.param.AccountLinkCreateParams
import me.nathanfallet.suitebde.models.associations.Association

class StripeService(
    private val apiKey: String,
) : IStripeService {

    init {
        // Set API key
        Stripe.apiKey = apiKey
    }

    override suspend fun getAccount(accountId: String): Account? =
        Account.retrieve(accountId)

    override suspend fun createAccount(association: Association): Account =
        Account.create(
            AccountCreateParams.builder()
                .setType(AccountCreateParams.Type.EXPRESS)
                .setBusinessType(AccountCreateParams.BusinessType.NON_PROFIT)
                .setBusinessProfile(
                    AccountCreateParams.BusinessProfile.builder()
                        .setName(association.name)
                        .build()
                )
                .build()
        )

    override suspend fun createAccountLink(
        accountId: String,
        type: AccountLinkCreateParams.Type,
        returnUrl: String,
    ): AccountLink =
        AccountLink.create(
            AccountLinkCreateParams.builder()
                .setAccount(accountId)
                .setType(type)
                .setReturnUrl(returnUrl)
                .setRefreshUrl(returnUrl)
                .build()
        )

}
