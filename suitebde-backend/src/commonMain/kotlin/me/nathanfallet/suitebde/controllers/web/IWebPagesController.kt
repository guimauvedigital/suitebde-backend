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
    suspend fun get(call: ApplicationCall, @ParentModel parent: Association, @Id id: String): WebPage

    @TemplateMapping("public/web/page.ftl")
    @Path("GET", "/")
    suspend fun getHome(call: ApplicationCall, @ParentModel parent: Association): WebPage

    @TemplateMapping("public/web/page.ftl")
    @Path("GET", "/pages/{webpageId}")
    suspend fun getByUrl(call: ApplicationCall, @ParentModel parent: Association, @Id url: String): WebPage

    @APIMapping
    @AdminTemplateMapping
    @CreateModelPath
    suspend fun create(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @Payload payload: WebPagePayload,
    ): WebPage

    @APIMapping
    @AdminTemplateMapping
    @UpdateModelPath
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
    suspend fun delete(call: ApplicationCall, @ParentModel parent: Association, @Id id: String)

}
