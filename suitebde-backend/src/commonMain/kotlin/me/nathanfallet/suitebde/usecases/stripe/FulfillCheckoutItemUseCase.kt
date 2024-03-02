package me.nathanfallet.suitebde.usecases.stripe

import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.SubscriptionInAssociation
import me.nathanfallet.suitebde.models.stripe.CheckoutItem
import me.nathanfallet.suitebde.models.users.CreateSubscriptionInUserPayload
import me.nathanfallet.suitebde.models.users.SubscriptionInUser
import me.nathanfallet.suitebde.usecases.application.IAddDurationUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUserForEmailUseCase
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import me.nathanfallet.usecases.models.get.IGetChildModelSuspendUseCase

class FulfillCheckoutItemUseCase(
    private val getUserForEmailUseCase: IGetUserForEmailUseCase,
    private val addDurationUseCase: IAddDurationUseCase,
    private val getSubscriptionInAssociationUseCase: IGetChildModelSuspendUseCase<SubscriptionInAssociation, String, String>,
    private val createSubscriptionInUserUseCase: ICreateChildModelSuspendUseCase<SubscriptionInUser, CreateSubscriptionInUserPayload, String>,
) : IFulfillCheckoutItemUseCase {

    override suspend fun invoke(input1: Association, input2: String, input3: CheckoutItem) {
        when (input3.itemType) {
            SubscriptionInAssociation::class.simpleName!! -> {
                val subscription = getSubscriptionInAssociationUseCase(input3.itemId, input1.id) ?: return
                val user = getUserForEmailUseCase(input2, false) ?: return
                createSubscriptionInUserUseCase(
                    CreateSubscriptionInUserPayload(
                        subscription.id,
                        Clock.System.now(),
                        addDurationUseCase(Clock.System.now(), subscription.duration)
                    ),
                    user.id
                )
            }

            // TODO: Add more item types here

            else -> {}
        }
    }

}
