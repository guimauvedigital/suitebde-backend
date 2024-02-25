package me.nathanfallet.suitebde.services.stripe

import com.stripe.Stripe
import com.stripe.model.Account
import com.stripe.model.AccountLink
import com.stripe.model.checkout.Session
import com.stripe.net.RequestOptions
import com.stripe.param.AccountCreateParams
import com.stripe.param.AccountLinkCreateParams
import com.stripe.param.checkout.SessionCreateParams
import com.stripe.param.checkout.SessionRetrieveParams
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.stripe.CheckoutItem
import me.nathanfallet.suitebde.models.stripe.ICustomer
import me.nathanfallet.suitebde.models.stripe.StripeAccount

class StripeService(
    private val apiKey: String,
) : IStripeService {

    init {
        // Set API key
        Stripe.apiKey = apiKey
    }

    override suspend fun getAccount(accountId: String): Account =
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
                .setCapabilities(
                    AccountCreateParams.Capabilities.builder()
                        .setCardPayments(
                            AccountCreateParams.Capabilities.CardPayments.builder()
                                .setRequested(true)
                                .build()
                        )
                        .setTransfers(
                            AccountCreateParams.Capabilities.Transfers.builder()
                                .setRequested(true)
                                .build()
                        )
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

    override suspend fun getCheckoutSession(accountId: String, sessionId: String): Session =
        Session.retrieve(
            sessionId,
            SessionRetrieveParams.builder()
                .addAllExpand(listOf("line_items", "line_items.data.price", "line_items.data.price.product"))
                .build(),
            RequestOptions.builder()
                .setStripeAccount(accountId)
                .build()
        )

    override suspend fun createCheckoutSession(
        account: StripeAccount,
        customer: ICustomer?,
        items: List<CheckoutItem>,
        returnUrl: String,
    ): Session =
        Session.create(
            SessionCreateParams.builder()
                .setCustomerEmail(customer?.email)
                .addAllLineItem(
                    items.map {
                        SessionCreateParams.LineItem.builder()
                            .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency(it.currency)
                                    .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                            .setName(it.name)
                                            .setDescription(it.description)
                                            .putMetadata("itemType", it.itemType)
                                            .putMetadata("itemId", it.itemId)
                                            .build()
                                    )
                                    .setUnitAmount(it.unitAmount)
                                    .build()
                            )
                            .setQuantity(it.quantity)
                            .build()
                    }
                )
                .setPaymentIntentData(
                    SessionCreateParams.PaymentIntentData.builder()
                        .setApplicationFeeAmount(
                            (items.fold(0L) { acc, item -> acc + item.unitAmount } * 0.05).toLong()
                        )
                        .build()
                )
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(returnUrl)
                .putMetadata("associationId", account.associationId)
                .putMetadata("accountId", account.accountId)
                .build(),
            RequestOptions.builder()
                .setStripeAccount(account.accountId)
                .build()
        )

}
