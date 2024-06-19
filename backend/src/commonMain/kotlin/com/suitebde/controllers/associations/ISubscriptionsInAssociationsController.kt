package com.suitebde.controllers.associations

import com.suitebde.models.associations.Association
import com.suitebde.models.associations.CreateSubscriptionInAssociationPayload
import com.suitebde.models.associations.SubscriptionInAssociation
import com.suitebde.models.associations.UpdateSubscriptionInAssociationPayload
import com.suitebde.models.stripe.CheckoutSession
import dev.kaccelero.annotations.*
import dev.kaccelero.controllers.IChildModelController
import dev.kaccelero.models.UUID
import io.ktor.server.application.*

interface ISubscriptionsInAssociationsController :
    IChildModelController<SubscriptionInAssociation, UUID, CreateSubscriptionInAssociationPayload, UpdateSubscriptionInAssociationPayload, Association, UUID> {

    @APIMapping
    @ListModelPath
    suspend fun list(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @QueryParameter limit: Long?,
        @QueryParameter offset: Long?,
    ): List<SubscriptionInAssociation>

    @AdminTemplateMapping
    @ListModelPath
    suspend fun listAdmin(
        call: ApplicationCall,
        @ParentModel parent: Association,
    ): Map<String, Any>

    @APIMapping
    @GetModelPath
    @DocumentedError(404, "subscriptions_in_associations_not_found")
    suspend fun get(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @Id id: UUID,
    ): SubscriptionInAssociation

    @APIMapping
    @AdminTemplateMapping
    @CreateModelPath
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "subscriptions_in_associations_create_not_allowed")
    @DocumentedError(500, "error_internal")
    suspend fun create(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @Payload payload: CreateSubscriptionInAssociationPayload,
    ): SubscriptionInAssociation

    @APIMapping
    @AdminTemplateMapping
    @DeleteModelPath
    @DocumentedType(SubscriptionInAssociation::class)
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "subscriptions_in_associations_delete_not_allowed")
    @DocumentedError(404, "subscriptions_in_associations_not_found")
    @DocumentedError(500, "error_internal")
    suspend fun delete(call: ApplicationCall, @ParentModel parent: Association, @Id id: UUID)

    @APIMapping
    @AdminTemplateMapping
    @UpdateModelPath
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "subscriptions_in_associations_update_not_allowed")
    @DocumentedError(404, "subscriptions_in_associations_not_found")
    @DocumentedError(500, "error_internal")
    suspend fun update(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @Id id: UUID,
        @Payload payload: UpdateSubscriptionInAssociationPayload,
    ): SubscriptionInAssociation

    @APIMapping("checkoutSubscriptionInAssociation")
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(404, "subscriptions_in_associations_not_found")
    @Path("POST", "/{subscriptioninassociationId}/checkout")
    suspend fun checkout(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @Id id: UUID,
    ): CheckoutSession

}
