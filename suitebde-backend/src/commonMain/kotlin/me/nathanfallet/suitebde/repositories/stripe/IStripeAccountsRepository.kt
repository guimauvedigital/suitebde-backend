package me.nathanfallet.suitebde.repositories.stripe

import me.nathanfallet.suitebde.models.stripe.CreateStripeAccountPayload
import me.nathanfallet.suitebde.models.stripe.StripeAccount
import me.nathanfallet.suitebde.models.stripe.UpdateStripeAccountPayload
import me.nathanfallet.usecases.models.repositories.IChildModelSuspendRepository

interface IStripeAccountsRepository :
    IChildModelSuspendRepository<StripeAccount, String, CreateStripeAccountPayload, UpdateStripeAccountPayload, String> {

    suspend fun count(parentId: String): Long

}
