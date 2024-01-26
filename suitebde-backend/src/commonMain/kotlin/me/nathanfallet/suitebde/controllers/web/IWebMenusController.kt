package me.nathanfallet.suitebde.controllers.web

import io.ktor.server.application.*
import me.nathanfallet.ktorx.controllers.IChildModelController
import me.nathanfallet.ktorx.models.annotations.*
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.web.CreateWebMenuPayload
import me.nathanfallet.suitebde.models.web.UpdateWebMenuPayload
import me.nathanfallet.suitebde.models.web.WebMenu

interface IWebMenusController :
    IChildModelController<WebMenu, String, CreateWebMenuPayload, UpdateWebMenuPayload, Association, String> {

    @APIMapping
    @AdminTemplateMapping
    @ListModelPath
    suspend fun list(call: ApplicationCall, @ParentModel parent: Association): List<WebMenu>

    @APIMapping
    @GetModelPath
    suspend fun get(call: ApplicationCall, @ParentModel parent: Association, @Id id: String): WebMenu

    @APIMapping
    @AdminTemplateMapping
    @CreateModelPath
    suspend fun create(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @Payload payload: CreateWebMenuPayload,
    ): WebMenu

    @APIMapping
    @AdminTemplateMapping
    @UpdateModelPath
    suspend fun update(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @Id id: String,
        @Payload payload: UpdateWebMenuPayload,
    ): WebMenu

    @APIMapping
    @AdminTemplateMapping
    @DeleteModelPath
    @DocumentedType(WebMenu::class)
    suspend fun delete(call: ApplicationCall, @ParentModel parent: Association, @Id id: String)

}
