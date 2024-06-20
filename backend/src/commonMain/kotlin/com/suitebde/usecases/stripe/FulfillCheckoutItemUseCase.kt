package com.suitebde.usecases.stripe

import com.suitebde.models.associations.Association
import com.suitebde.models.associations.SubscriptionInAssociation
import com.suitebde.models.stripe.CheckoutItem
import com.suitebde.models.users.CreateSubscriptionInUserPayload
import com.suitebde.models.users.SubscriptionInUser
import com.suitebde.usecases.application.IAddDurationUseCase
import com.suitebde.usecases.users.IGetUserForEmailUseCase
import dev.kaccelero.commons.repositories.ICreateChildModelSuspendUseCase
import dev.kaccelero.commons.repositories.IGetChildModelSuspendUseCase
import dev.kaccelero.models.UUID
import kotlinx.datetime.Clock

class FulfillCheckoutItemUseCase(
    private val getUserForEmailUseCase: IGetUserForEmailUseCase,
    private val addDurationUseCase: IAddDurationUseCase,
    private val getSubscriptionInAssociationUseCase: IGetChildModelSuspendUseCase<SubscriptionInAssociation, UUID, UUID>,
    private val createSubscriptionInUserUseCase: ICreateChildModelSuspendUseCase<SubscriptionInUser, CreateSubscriptionInUserPayload, UUID>,
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
