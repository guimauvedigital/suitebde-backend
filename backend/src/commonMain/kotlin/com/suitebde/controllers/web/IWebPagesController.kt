package com.suitebde.controllers.web

import com.suitebde.models.associations.Association
import com.suitebde.models.web.WebPage
import com.suitebde.models.web.WebPagePayload
import dev.kaccelero.annotations.*
import dev.kaccelero.controllers.IChildModelController
import dev.kaccelero.models.UUID
import io.ktor.server.application.*

interface IWebPagesController :
    IChildModelController<WebPage, UUID, WebPagePayload, WebPagePayload, Association, UUID> {

    @APIMapping
    @AdminTemplateMapping
    @ListModelPath
    suspend fun list(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @QueryParameter limit: Long?,
        @QueryParameter offset: Long?,
    ): List<WebPage>

    @APIMapping
    @AdminTemplateMapping
    @GetModelPath
    @DocumentedError(404, "web_pages_not_found")
    suspend fun get(call: ApplicationCall, @ParentModel parent: Association, @Id id: UUID): WebPage

    @TemplateMapping("public/web/page.ftl")
    @Path("GET", "/{url}")
    suspend fun getByUrl(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @PathParameter url: String,
    ): Map<String, Any>

    @APIMapping
    @AdminTemplateMapping
    @CreateModelPath
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "web_pages_create_not_allowed")
    @DocumentedError(500, "error_internal")
    suspend fun create(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @Payload payload: WebPagePayload,
    ): WebPage

    @APIMapping
    @AdminTemplateMapping
    @UpdateModelPath
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "web_pages_update_not_allowed")
    @DocumentedError(404, "web_pages_not_found")
    @DocumentedError(500, "error_internal")
    suspend fun update(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @Id id: UUID,
        @Payload payload: WebPagePayload,
    ): WebPage

    @APIMapping
    @AdminTemplateMapping
    @DeleteModelPath
    @DocumentedType(WebPage::class)
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "web_pages_delete_not_allowed")
    @DocumentedError(404, "web_pages_not_found")
    @DocumentedError(500, "error_internal")
    suspend fun delete(call: ApplicationCall, @ParentModel parent: Association, @Id id: UUID)

}
