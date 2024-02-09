package me.nathanfallet.suitebde.controllers.associations

import io.ktor.server.application.*
import me.nathanfallet.ktorx.controllers.IChildModelController
import me.nathanfallet.ktorx.models.annotations.*
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateSubscriptionInAssociationPayload
import me.nathanfallet.suitebde.models.associations.SubscriptionInAssociation
import me.nathanfallet.suitebde.models.associations.UpdateSubscriptionInAssociationPayload

interface ISubscriptionsInAssociationsController :
    IChildModelController<SubscriptionInAssociation, String, CreateSubscriptionInAssociationPayload, UpdateSubscriptionInAssociationPayload, Association, String> {

    @APIMapping
    @AdminTemplateMapping
    @ListModelPath
    suspend fun list(
        call: ApplicationCall,
        @ParentModel parent: Association,
    ): List<SubscriptionInAssociation>

    @APIMapping
    @GetModelPath
    @DocumentedError(404, "subscriptions_in_associations_not_found")
    suspend fun get(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @Id id: String,
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
    suspend fun delete(call: ApplicationCall, @ParentModel parent: Association, @Id id: String)

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
        @Id id: String,
        @Payload payload: UpdateSubscriptionInAssociationPayload,
    ): SubscriptionInAssociation

}
