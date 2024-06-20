package com.suitebde.usecases.stripe

import com.suitebde.models.associations.Association
import com.suitebde.models.stripe.StripeOrder
import com.suitebde.models.stripe.UpdateStripeOrderPayload
import com.suitebde.repositories.stripe.IStripeOrdersRepository
import dev.kaccelero.commons.repositories.IGetModelSuspendUseCase
import dev.kaccelero.commons.repositories.IUpdateChildModelSuspendUseCase
import dev.kaccelero.models.UUID

class UpdateStripeOrderUseCase(
    private val repository: IStripeOrdersRepository,
    private val getAssociationUseCase: IGetModelSuspendUseCase<Association, UUID>,
    private val fulfillCheckoutItemUseCase: IFulfillCheckoutItemUseCase,
) : IUpdateChildModelSuspendUseCase<StripeOrder, String, UpdateStripeOrderPayload, UUID> {

    override suspend fun invoke(input1: String, input2: UpdateStripeOrderPayload, input3: UUID): StripeOrder? {
        // Only update if the order is not already paid
        repository.get(input1, input3)?.takeIf { it.paidAt != null }?.let { return@invoke it }

        // Here we actually update the order
        if (!repository.update(input1, input2, input3)) return null
        val updatedOrder = repository.get(input1, input3) ?: return null
        if (updatedOrder.paidAt == null) return updatedOrder // We stop here if the order is not paid

        // Create items to fulfill the order
        getAssociationUseCase(updatedOrder.associationId)?.let { association ->
            updatedOrder.items.forEach {
                fulfillCheckoutItemUseCase(association, updatedOrder.email, it)
            }
        }

        return updatedOrder
    }

}
