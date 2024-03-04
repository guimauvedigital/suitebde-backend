package me.nathanfallet.suitebde.usecases.stripe

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.repositories.stripe.IStripeAccountsRepository

class HasStripeAccountLinkedUseCase(
    private val repository: IStripeAccountsRepository,
) : IHasStripeAccountLinkedUseCase {

    override suspend fun invoke(input: Association): Boolean =
        repository.count(input.id) > 0

}
