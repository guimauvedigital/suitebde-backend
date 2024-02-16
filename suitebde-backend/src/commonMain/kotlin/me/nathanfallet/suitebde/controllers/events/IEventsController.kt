package me.nathanfallet.suitebde.controllers.events

import io.ktor.server.application.*
import me.nathanfallet.ktorx.controllers.IChildModelController
import me.nathanfallet.ktorx.models.annotations.*
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.events.CreateEventPayload
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.models.events.UpdateEventPayload

interface IEventsController :
    IChildModelController<Event, String, CreateEventPayload, UpdateEventPayload, Association, String> {

    @APIMapping
    @AdminTemplateMapping
    @CreateModelPath
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "events_validated_not_allowed")
    @DocumentedError(500, "error_internal")
    suspend fun create(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @Payload payload: CreateEventPayload,
    ): Event

    @APIMapping
    @AdminTemplateMapping
    @DeleteModelPath
    @DocumentedType(Event::class)
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "events_delete_not_allowed")
    @DocumentedError(404, "events_not_found")
    @DocumentedError(500, "error_internal")
    suspend fun delete(call: ApplicationCall, @ParentModel parent: Association, @Id id: String)

    @APIMapping
    @GetModelPath
    @DocumentedError(404, "events_not_found")
    suspend fun get(call: ApplicationCall, @ParentModel parent: Association, @Id id: String): Event

    @APIMapping
    @AdminTemplateMapping
    @TemplateMapping("public/events/list.ftl")
    @ListModelPath
    suspend fun list(call: ApplicationCall, @ParentModel parent: Association): List<Event>

    @APIMapping
    @AdminTemplateMapping
    @UpdateModelPath
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "events_update_not_allowed")
    @DocumentedError(404, "events_not_found")
    @DocumentedError(500, "error_internal")
    suspend fun update(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @Id id: String,
        @Payload payload: UpdateEventPayload,
    ): Event

}
