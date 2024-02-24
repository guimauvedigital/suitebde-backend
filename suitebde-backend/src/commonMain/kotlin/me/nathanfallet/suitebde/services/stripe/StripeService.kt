package me.nathanfallet.suitebde.services.stripe

import com.stripe.Stripe
import com.stripe.model.Account
import com.stripe.model.AccountLink
import com.stripe.model.checkout.Session
import com.stripe.net.RequestOptions
import com.stripe.param.AccountCreateParams
import com.stripe.param.AccountLinkCreateParams
import com.stripe.param.checkout.SessionCreateParams
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

    override suspend fun createCheckoutSession(
        accountId: String,
        name: String,
        amount: Long,
        returnUrl: String,
    ): Session =
        Session.create(
            SessionCreateParams.builder()
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("eur")
                                .setProductData(
                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName(name)
                                        .build()
                                )
                                .setUnitAmount(amount)
                                .build()
                        )
                        .setQuantity(1)
                        .build()
                )
                .setPaymentIntentData(
                    SessionCreateParams.PaymentIntentData.builder()
                        .setApplicationFeeAmount((amount * 0.05).toLong())
                        .build()
                )
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setReturnUrl(returnUrl)
                .setSuccessUrl(returnUrl)
                .build(),
            RequestOptions.builder()
                .setStripeAccount(accountId)
                .build()
        )

}
