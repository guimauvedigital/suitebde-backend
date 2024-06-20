package com.suitebde.controllers.events

import com.suitebde.models.associations.Association
import com.suitebde.models.events.CreateEventPayload
import com.suitebde.models.events.Event
import com.suitebde.models.events.UpdateEventPayload
import dev.kaccelero.annotations.*
import dev.kaccelero.controllers.IChildModelController
import dev.kaccelero.models.UUID
import io.ktor.server.application.*

interface IEventsController :
    IChildModelController<Event, UUID, CreateEventPayload, UpdateEventPayload, Association, UUID> {

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
    suspend fun delete(call: ApplicationCall, @ParentModel parent: Association, @Id id: UUID)

    @APIMapping
    @GetModelPath
    @DocumentedError(404, "events_not_found")
    suspend fun get(call: ApplicationCall, @ParentModel parent: Association, @Id id: UUID): Event

    @APIMapping
    @AdminTemplateMapping
    @TemplateMapping("public/events/list.ftl")
    @ListModelPath
    suspend fun list(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @QueryParameter limit: Long?,
        @QueryParameter offset: Long?,
    ): List<Event>

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
        @Id id: UUID,
        @Payload payload: UpdateEventPayload,
    ): Event

}
