package com.suitebde.usecases.stripe

import com.stripe.model.checkout.Session
import com.suitebde.models.stripe.CheckoutItem
import com.suitebde.models.stripe.CreateStripeOrderPayload
import com.suitebde.models.stripe.StripeOrder
import com.suitebde.services.stripe.IStripeService
import dev.kaccelero.commons.repositories.ICreateChildModelSuspendUseCase
import dev.kaccelero.models.UUID

class CreateStripeOrderForSessionUseCase(
    private val stripeService: IStripeService,
    private val createStripeOrderUseCase: ICreateChildModelSuspendUseCase<StripeOrder, CreateStripeOrderPayload, UUID>,
) : ICreateStripeOrderForSessionUseCase {

    override suspend fun invoke(input: Session): StripeOrder? {
        val session = stripeService.getCheckoutSession(
            input.metadata["accountId"]!!,
            input.id
        )
        return createStripeOrderUseCase(
            CreateStripeOrderPayload(
                session.id,
                session.customerDetails.email,
                session.lineItems.autoPagingIterable().map {
                    val product = it.price.productObject
                    CheckoutItem(
                        product.metadata["itemType"]!!,
                        UUID(product.metadata["itemId"]!!),
                        product.name,
                        product.description,
                        it.quantity,
                        it.price.unitAmount
                    )
                }
            ),
            UUID(session.metadata["associationId"]!!)
        )
    }

}
