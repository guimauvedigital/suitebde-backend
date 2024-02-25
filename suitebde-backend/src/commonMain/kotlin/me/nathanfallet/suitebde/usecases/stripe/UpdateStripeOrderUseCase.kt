package me.nathanfallet.suitebde.usecases.stripe

import me.nathanfallet.suitebde.models.stripe.StripeOrder
import me.nathanfallet.suitebde.models.stripe.UpdateStripeOrderPayload
import me.nathanfallet.suitebde.repositories.stripe.IStripeOrdersRepository
import me.nathanfallet.usecases.models.update.IUpdateChildModelSuspendUseCase

class UpdateStripeOrderUseCase(
    private val repository: IStripeOrdersRepository,
) : IUpdateChildModelSuspendUseCase<StripeOrder, String, UpdateStripeOrderPayload, String> {

    override suspend fun invoke(input1: String, input2: UpdateStripeOrderPayload, input3: String): StripeOrder? {
        // Only update if the order is not already paid
        repository.get(input1, input3)?.takeIf { it.paidAt != null }?.let { return@invoke it }

        // Here we actually update the order
        if (!repository.update(input1, input2, input3)) return null
        val updatedOrder = repository.get(input1, input3) ?: return null
        if (updatedOrder.paidAt == null) return updatedOrder // We stop here if the order is not paid

        // Create items to fulfill the order
        updatedOrder.items.forEach {

        }

        return updatedOrder
    }

}
