package com.suitebde.controllers.associations

import com.suitebde.models.associations.Association
import com.suitebde.models.associations.CreateDomainInAssociationPayload
import com.suitebde.models.associations.DomainInAssociation
import dev.kaccelero.annotations.*
import dev.kaccelero.controllers.IChildModelController
import dev.kaccelero.models.UUID
import io.ktor.server.application.*

interface IDomainsInAssociationsController :
    IChildModelController<DomainInAssociation, String, CreateDomainInAssociationPayload, Unit, Association, UUID> {

    @APIMapping
    @AdminTemplateMapping
    @ListModelPath
    suspend fun list(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @QueryParameter limit: Long?,
        @QueryParameter offset: Long?,
    ): List<DomainInAssociation>

    @GetModelPath
    suspend fun get(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @Id id: String,
    ): DomainInAssociation

    @APIMapping
    @AdminTemplateMapping
    @CreateModelPath
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "domains_create_not_allowed")
    @DocumentedError(500, "error_internal")
    suspend fun create(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @Payload payload: CreateDomainInAssociationPayload,
    ): DomainInAssociation

    @APIMapping
    @AdminTemplateMapping
    @DeleteModelPath
    @DocumentedType(DomainInAssociation::class)
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "domains_delete_not_allowed")
    @DocumentedError(404, "domains_not_found")
    @DocumentedError(500, "error_internal")
    suspend fun delete(call: ApplicationCall, @ParentModel parent: Association, @Id id: String)

}
