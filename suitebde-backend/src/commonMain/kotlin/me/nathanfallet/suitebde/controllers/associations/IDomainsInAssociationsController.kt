package me.nathanfallet.suitebde.controllers.associations

import io.ktor.server.application.*
import me.nathanfallet.ktorx.controllers.IChildModelController
import me.nathanfallet.ktorx.models.annotations.*
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateDomainInAssociationPayload
import me.nathanfallet.suitebde.models.associations.DomainInAssociation

interface IDomainsInAssociationsController :
    IChildModelController<DomainInAssociation, String, CreateDomainInAssociationPayload, Unit, Association, String> {

    @APIMapping
    @AdminTemplateMapping
    @ListModelPath
    suspend fun list(
        call: ApplicationCall,
        @ParentModel parent: Association,
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
    suspend fun create(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @Payload payload: CreateDomainInAssociationPayload,
    ): DomainInAssociation

    @APIMapping
    @AdminTemplateMapping
    @DeleteModelPath
    @DocumentedType(DomainInAssociation::class)
    suspend fun delete(call: ApplicationCall, @ParentModel parent: Association, @Id id: String)

}
