package me.nathanfallet.suitebde.controllers.web

import io.ktor.server.application.*
import me.nathanfallet.ktorx.controllers.IChildModelController
import me.nathanfallet.ktorx.models.annotations.*
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.suitebde.models.web.WebPagePayload

interface IWebPagesController :
    IChildModelController<WebPage, String, WebPagePayload, WebPagePayload, Association, String> {

    @APIMapping
    @AdminTemplateMapping
    @ListModelPath
    suspend fun list(call: ApplicationCall, @ParentModel parent: Association): List<WebPage>

    @APIMapping
    @AdminTemplateMapping
    @GetModelPath
    @DocumentedError(404, "web_pages_not_found")
    suspend fun get(call: ApplicationCall, @ParentModel parent: Association, @Id id: String): WebPage

    @TemplateMapping("public/web/page.ftl")
    @Path("GET", "/{webpageId}")
    suspend fun getByUrl(call: ApplicationCall, @ParentModel parent: Association, @Id url: String): WebPage

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
        @Id id: String,
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
    suspend fun delete(call: ApplicationCall, @ParentModel parent: Association, @Id id: String)

}
