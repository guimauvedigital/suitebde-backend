package com.suitebde.repositories.associations

import com.suitebde.models.associations.CreateSubscriptionInAssociationPayload
import com.suitebde.models.associations.SubscriptionInAssociation
import com.suitebde.models.associations.UpdateSubscriptionInAssociationPayload
import com.suitebde.models.stripe.CheckoutSession
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination

interface ISubscriptionsInAssociationsRemoteRepository {

    suspend fun list(pagination: Pagination, associationId: UUID): List<SubscriptionInAssociation>
    suspend fun get(id: UUID, associationId: UUID): SubscriptionInAssociation?
    suspend fun create(
        payload: CreateSubscriptionInAssociationPayload,
        associationId: UUID,
    ): SubscriptionInAssociation?

    suspend fun update(
        id: UUID,
        payload: UpdateSubscriptionInAssociationPayload,
        associationId: UUID,
    ): SubscriptionInAssociation?

    suspend fun delete(id: UUID, associationId: UUID): Boolean

    suspend fun checkout(id: UUID, associationId: UUID): CheckoutSession?

}
