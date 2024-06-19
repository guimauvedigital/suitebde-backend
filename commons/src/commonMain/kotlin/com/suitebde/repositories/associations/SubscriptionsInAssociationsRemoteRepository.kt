package com.suitebde.repositories.associations

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.associations.Association
import com.suitebde.models.associations.CreateSubscriptionInAssociationPayload
import com.suitebde.models.associations.SubscriptionInAssociation
import com.suitebde.models.associations.UpdateSubscriptionInAssociationPayload
import com.suitebde.models.stripe.CheckoutSession
import dev.kaccelero.models.RecursiveId
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.APIChildModelRemoteRepository
import dev.kaccelero.repositories.IAPIModelRemoteRepository
import dev.kaccelero.repositories.Pagination
import io.ktor.client.call.*
import io.ktor.http.*
import io.ktor.util.reflect.*

class SubscriptionsInAssociationsRemoteRepository(
    client: ISuiteBDEClient,
    parentRepository: IAPIModelRemoteRepository<Association, UUID, *, *>,
) : APIChildModelRemoteRepository<SubscriptionInAssociation, UUID, CreateSubscriptionInAssociationPayload, UpdateSubscriptionInAssociationPayload, UUID>(
    typeInfo<SubscriptionInAssociation>(),
    typeInfo<CreateSubscriptionInAssociationPayload>(),
    typeInfo<UpdateSubscriptionInAssociationPayload>(),
    typeInfo<List<SubscriptionInAssociation>>(),
    client,
    parentRepository,
    route = "subscriptions",
    prefix = "/api/v1"
), ISubscriptionsInAssociationsRemoteRepository {

    override suspend fun list(pagination: Pagination, associationId: UUID): List<SubscriptionInAssociation> =
        list(pagination, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun get(id: UUID, associationId: UUID): SubscriptionInAssociation? =
        get(id, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun create(
        payload: CreateSubscriptionInAssociationPayload,
        associationId: UUID,
    ): SubscriptionInAssociation? =
        create(payload, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun update(
        id: UUID,
        payload: UpdateSubscriptionInAssociationPayload,
        associationId: UUID,
    ): SubscriptionInAssociation? =
        update(id, payload, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun delete(id: UUID, associationId: UUID): Boolean =
        delete(id, RecursiveId<Association, UUID, Unit>(associationId), null)

    override suspend fun checkout(id: UUID, associationId: UUID): CheckoutSession? =
        client
            .request(
                HttpMethod.Post,
                "${constructFullRoute(RecursiveId<Association, UUID, Unit>(associationId))}/$id/checkout"
            )
            .body()

}
