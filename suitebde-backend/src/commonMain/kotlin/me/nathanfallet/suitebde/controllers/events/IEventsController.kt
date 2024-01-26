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
    suspend fun create(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @Payload payload: CreateEventPayload,
    ): Event

    @APIMapping
    @AdminTemplateMapping
    @DeleteModelPath
    @DocumentedType(Event::class)
    suspend fun delete(call: ApplicationCall, @ParentModel parent: Association, @Id id: String)

    @APIMapping
    @GetModelPath
    suspend fun get(call: ApplicationCall, @ParentModel parent: Association, @Id id: String): Event

    @APIMapping
    @AdminTemplateMapping
    @ListModelPath
    suspend fun list(call: ApplicationCall, @ParentModel parent: Association): List<Event>

    @APIMapping
    @AdminTemplateMapping
    @UpdateModelPath
    suspend fun update(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @Id id: String,
        @Payload payload: UpdateEventPayload,
    ): Event

}
