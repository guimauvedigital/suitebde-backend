package com.suitebde.controllers.events

import com.suitebde.models.associations.Association
import com.suitebde.models.events.CreateEventPayload
import com.suitebde.models.events.Event
import com.suitebde.models.events.UpdateEventPayload
import com.suitebde.models.notifications.CreatePermissionNotificationPayload
import com.suitebde.models.roles.Permission
import com.suitebde.models.users.User
import com.suitebde.usecases.events.IListAllEventsUseCase
import com.suitebde.usecases.notifications.ISendNotificationToPermissionUseCase
import dev.kaccelero.commons.exceptions.ControllerException
import dev.kaccelero.commons.permissions.ICheckPermissionSuspendUseCase
import dev.kaccelero.commons.repositories.*
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*

class EventsController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val listAllEventsUseCase: IListAllEventsUseCase,
    private val getEventsInAssociationUseCase: IListChildModelSuspendUseCase<Event, UUID>,
    private val getEventsInAssociationSlicedUseCase: IListSliceChildModelSuspendUseCase<Event, UUID>,
    private val getEventUseCase: IGetChildModelSuspendUseCase<Event, UUID, UUID>,
    private val createEventUseCase: ICreateChildModelSuspendUseCase<Event, CreateEventPayload, UUID>,
    private val updateEventUseCase: IUpdateChildModelSuspendUseCase<Event, UUID, UpdateEventPayload, UUID>,
    private val deleteEventUseCase: IDeleteChildModelSuspendUseCase<Event, UUID, UUID>,
    private val sendNotificationToPermissionUseCase: ISendNotificationToPermissionUseCase,
) : IEventsController {

    override suspend fun create(call: ApplicationCall, parent: Association, payload: CreateEventPayload): Event {
        val user = requireUserForCallUseCase(call) as User
        payload.validated?.let {
            if (!checkPermissionUseCase(
                    user,
                    Permission.EVENTS_CREATE inAssociation parent.id
                )
            ) throw ControllerException(
                HttpStatusCode.Forbidden, "events_validated_not_allowed"
            )
        }
        val event = createEventUseCase(payload, parent.id) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
        if (!event.validated) sendNotificationToPermissionUseCase(
            CreatePermissionNotificationPayload(
                Permission.EVENTS_UPDATE,
                "notification_event_suggested_title",
                "notification_event_suggested_description",
                bodyArgs = listOf(event.name, user.firstName),
                data = mapOf("eventId" to event.id.toString())
            )
        )
        return event
    }

    override suspend fun delete(call: ApplicationCall, parent: Association, id: UUID) {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.EVENTS_DELETE inAssociation parent.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "events_delete_not_allowed"
        )
        val event = getEventUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "events_not_found"
        )
        if (!deleteEventUseCase(event.id, parent.id)) throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

    override suspend fun get(call: ApplicationCall, parent: Association, id: UUID): Event {
        return getEventUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "events_not_found"
        )
    }

    override suspend fun list(call: ApplicationCall, parent: Association, limit: Long?, offset: Long?): List<Event> {
        if (call.request.path().contains("/admin/")) return listAllEventsUseCase(parent.id)
        if (!call.request.path().contains("/api/")) return getEventsInAssociationUseCase(parent.id)
        return getEventsInAssociationSlicedUseCase(
            Pagination(
                limit ?: 25,
                offset ?: 0,
            ),
            parent.id
        )
    }

    override suspend fun update(
        call: ApplicationCall,
        parent: Association,
        id: UUID,
        payload: UpdateEventPayload,
    ): Event {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.EVENTS_UPDATE inAssociation parent.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "events_update_not_allowed"
        )
        val event = getEventUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "events_not_found"
        )
        return updateEventUseCase(event.id, payload, parent.id) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

}
