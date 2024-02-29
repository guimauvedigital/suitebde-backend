package me.nathanfallet.suitebde.usecases.stripe

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.stripe.StripeOrder
import me.nathanfallet.suitebde.models.stripe.UpdateStripeOrderPayload
import me.nathanfallet.suitebde.repositories.stripe.IStripeOrdersRepository
import me.nathanfallet.usecases.models.get.IGetModelSuspendUseCase
import me.nathanfallet.usecases.models.update.IUpdateChildModelSuspendUseCase

class UpdateStripeOrderUseCase(
    private val repository: IStripeOrdersRepository,
    private val getAssociationUseCase: IGetModelSuspendUseCase<Association, String>,
    private val fulfillCheckoutItemUseCase: IFulfillCheckoutItemUseCase,
) : IUpdateChildModelSuspendUseCase<StripeOrder, String, UpdateStripeOrderPayload, String> {

    override suspend fun invoke(input1: String, input2: UpdateStripeOrderPayload, input3: String): StripeOrder? {
        // Only update if the order is not already paid
        repository.get(input1, input3)?.takeIf { it.paidAt != null }?.let { return@invoke it }

        // Here we actually update the order
        if (!repository.update(input1, input2, input3)) return null
        val updatedOrder = repository.get(input1, input3) ?: return null
        if (updatedOrder.paidAt == null) return updatedOrder // We stop here if the order is not paid

        // Create items to fulfill the order
        val association = getAssociationUseCase(updatedOrder.associationId)?.let { association ->
            updatedOrder.items.forEach {
                fulfillCheckoutItemUseCase(association, updatedOrder.email, it)
            }
        }

        return updatedOrder
    }

}
