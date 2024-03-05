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
    suspend fun list(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @QueryParameter limit: Long?,
        @QueryParameter offset: Long?,
    ): List<WebMenu>

    @APIMapping
    @GetModelPath
    @DocumentedError(404, "web_menus_not_found")
    suspend fun get(call: ApplicationCall, @ParentModel parent: Association, @Id id: String): WebMenu

    @APIMapping
    @AdminTemplateMapping
    @CreateModelPath
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "web_menus_create_not_allowed")
    @DocumentedError(500, "error_internal")
    suspend fun create(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @Payload payload: CreateWebMenuPayload,
    ): WebMenu

    @APIMapping
    @AdminTemplateMapping
    @UpdateModelPath
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "web_menus_update_not_allowed")
    @DocumentedError(404, "web_menus_not_found")
    @DocumentedError(500, "error_internal")
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
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "web_menus_delete_not_allowed")
    @DocumentedError(404, "web_menus_not_found")
    @DocumentedError(500, "error_internal")
    suspend fun delete(call: ApplicationCall, @ParentModel parent: Association, @Id id: String)

}
