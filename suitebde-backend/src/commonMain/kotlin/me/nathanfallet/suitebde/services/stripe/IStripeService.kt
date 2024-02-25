package me.nathanfallet.suitebde.services.stripe

import com.stripe.model.Account
import com.stripe.model.AccountLink
import com.stripe.model.checkout.Session
import com.stripe.param.AccountLinkCreateParams
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.stripe.CheckoutItem
import me.nathanfallet.suitebde.models.stripe.ICustomer
import me.nathanfallet.suitebde.models.stripe.StripeAccount

interface IStripeService {

    suspend fun getAccount(accountId: String): Account
    suspend fun createAccount(association: Association): Account

    suspend fun createAccountLink(accountId: String, type: AccountLinkCreateParams.Type, returnUrl: String): AccountLink

    suspend fun getCheckoutSession(accountId: String, sessionId: String): Session
    suspend fun createCheckoutSession(
        account: StripeAccount,
        customer: ICustomer?,
        items: List<CheckoutItem>,
        returnUrl: String,
    ): Session

}
