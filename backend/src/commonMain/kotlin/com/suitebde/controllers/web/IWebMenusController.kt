package com.suitebde.controllers.web

import com.suitebde.models.associations.Association
import com.suitebde.models.web.CreateWebMenuPayload
import com.suitebde.models.web.UpdateWebMenuPayload
import com.suitebde.models.web.WebMenu
import dev.kaccelero.annotations.*
import dev.kaccelero.controllers.IChildModelController
import dev.kaccelero.models.UUID
import io.ktor.server.application.*

interface IWebMenusController :
    IChildModelController<WebMenu, UUID, CreateWebMenuPayload, UpdateWebMenuPayload, Association, UUID> {

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
    suspend fun get(call: ApplicationCall, @ParentModel parent: Association, @Id id: UUID): WebMenu

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
        @Id id: UUID,
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
    suspend fun delete(call: ApplicationCall, @ParentModel parent: Association, @Id id: UUID)

}
