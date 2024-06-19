package com.suitebde.usecases.stripe

import com.suitebde.models.associations.Association
import com.suitebde.repositories.stripe.IStripeAccountsRepository

class HasStripeAccountLinkedUseCase(
    private val repository: IStripeAccountsRepository,
) : IHasStripeAccountLinkedUseCase {

    override suspend fun invoke(input: Association): Boolean =
        repository.count(input.id) > 0

}
