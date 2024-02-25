package me.nathanfallet.suitebde.usecases.stripe

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.StripeAccountInAssociation
import me.nathanfallet.suitebde.models.associations.UpdateStripeAccountInAssociationPayload
import me.nathanfallet.suitebde.services.stripe.IStripeService
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase
import me.nathanfallet.usecases.models.update.IUpdateChildModelSuspendUseCase

class RefreshStripeAccountUseCase(
    private val stripeService: IStripeService,
    private val listStripeAccountsInAssociationsUseCase: IListChildModelSuspendUseCase<StripeAccountInAssociation, String>,
    private val updateStripeAccountUseCase: IUpdateChildModelSuspendUseCase<StripeAccountInAssociation, String, UpdateStripeAccountInAssociationPayload, String>,
) : IRefreshStripeAccountUseCase {

    override suspend fun invoke(input: Association) {
        listStripeAccountsInAssociationsUseCase(input.id).forEach {
            if (!it.chargesEnabled && stripeService.getAccount(it.accountId)?.chargesEnabled == true)
                updateStripeAccountUseCase(
                    it.accountId,
                    UpdateStripeAccountInAssociationPayload(
                        chargesEnabled = true
                    ),
                    input.id
                )
        }
    }

}
