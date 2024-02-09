package me.nathanfallet.suitebde.repositories.associations

import me.nathanfallet.suitebde.models.associations.CreateSubscriptionInAssociationPayload
import me.nathanfallet.suitebde.models.associations.SubscriptionInAssociation
import me.nathanfallet.suitebde.models.associations.UpdateSubscriptionInAssociationPayload

interface ISubscriptionsInAssociationsRemoteRepository {

    suspend fun list(limit: Long, offset: Long, associationId: String): List<SubscriptionInAssociation>
    suspend fun get(id: String, associationId: String): SubscriptionInAssociation?
    suspend fun create(
        payload: CreateSubscriptionInAssociationPayload,
        associationId: String,
    ): SubscriptionInAssociation?

    suspend fun update(
        id: String,
        payload: UpdateSubscriptionInAssociationPayload,
        associationId: String,
    ): SubscriptionInAssociation?

    suspend fun delete(id: String, associationId: String): Boolean

}
