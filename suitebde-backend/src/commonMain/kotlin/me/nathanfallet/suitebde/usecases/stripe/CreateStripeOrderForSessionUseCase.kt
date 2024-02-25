package me.nathanfallet.suitebde.usecases.stripe

import com.stripe.model.checkout.Session
import me.nathanfallet.suitebde.models.stripe.CheckoutItem
import me.nathanfallet.suitebde.models.stripe.CreateStripeOrderPayload
import me.nathanfallet.suitebde.models.stripe.StripeOrder
import me.nathanfallet.suitebde.services.stripe.IStripeService
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase

class CreateStripeOrderForSessionUseCase(
    private val stripeService: IStripeService,
    private val createStripeOrderUseCase: ICreateChildModelSuspendUseCase<StripeOrder, CreateStripeOrderPayload, String>,
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
                        product.metadata["itemId"]!!,
                        product.name,
                        product.description,
                        it.quantity,
                        it.price.unitAmount
                    )
                }
            ),
            session.metadata["associationId"]!!
        )
    }

}
