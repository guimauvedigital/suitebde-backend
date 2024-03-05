package me.nathanfallet.suitebde.repositories.associations

import io.ktor.client.call.*
import io.ktor.http.*
import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.repositories.api.APIChildModelRemoteRepository
import me.nathanfallet.ktorx.repositories.api.IAPIModelRemoteRepository
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateSubscriptionInAssociationPayload
import me.nathanfallet.suitebde.models.associations.SubscriptionInAssociation
import me.nathanfallet.suitebde.models.associations.UpdateSubscriptionInAssociationPayload
import me.nathanfallet.suitebde.models.stripe.CheckoutSession
import me.nathanfallet.usecases.models.id.RecursiveId
import me.nathanfallet.usecases.pagination.Pagination

class SubscriptionsInAssociationsRemoteRepository(
    client: ISuiteBDEClient,
    parentRepository: IAPIModelRemoteRepository<Association, String, *, *>,
) : APIChildModelRemoteRepository<SubscriptionInAssociation, String, CreateSubscriptionInAssociationPayload, UpdateSubscriptionInAssociationPayload, String>(
    typeInfo<SubscriptionInAssociation>(),
    typeInfo<CreateSubscriptionInAssociationPayload>(),
    typeInfo<UpdateSubscriptionInAssociationPayload>(),
    typeInfo<List<SubscriptionInAssociation>>(),
    client,
    parentRepository,
    route = "subscriptions",
    prefix = "/api/v1"
), ISubscriptionsInAssociationsRemoteRepository {

    override suspend fun list(pagination: Pagination, associationId: String): List<SubscriptionInAssociation> =
        list(pagination, RecursiveId<Association, String, Unit>(associationId), null)

    override suspend fun get(id: String, associationId: String): SubscriptionInAssociation? =
        get(id, RecursiveId<Association, String, Unit>(associationId), null)

    override suspend fun create(
        payload: CreateSubscriptionInAssociationPayload,
        associationId: String,
    ): SubscriptionInAssociation? =
        create(payload, RecursiveId<Association, String, Unit>(associationId), null)

    override suspend fun update(
        id: String,
        payload: UpdateSubscriptionInAssociationPayload,
        associationId: String,
    ): SubscriptionInAssociation? =
        update(id, payload, RecursiveId<Association, String, Unit>(associationId), null)

    override suspend fun delete(id: String, associationId: String): Boolean =
        delete(id, RecursiveId<Association, String, Unit>(associationId), null)

    override suspend fun checkout(id: String, associationId: String): CheckoutSession? =
        client
            .request(
                HttpMethod.Post,
                "${constructFullRoute(RecursiveId<Association, String, Unit>(associationId))}/$id/checkout"
            )
            .body()

}
