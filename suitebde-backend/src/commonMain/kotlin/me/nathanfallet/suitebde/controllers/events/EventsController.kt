package me.nathanfallet.suitebde.controllers.events

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.events.CreateEventPayload
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.models.events.UpdateEventPayload
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import me.nathanfallet.usecases.models.delete.IDeleteChildModelSuspendUseCase
import me.nathanfallet.usecases.models.get.IGetChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.slice.IListSliceChildModelSuspendUseCase
import me.nathanfallet.usecases.models.update.IUpdateChildModelSuspendUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase

class EventsController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getEventsInAssociationUseCase: IListChildModelSuspendUseCase<Event, String>,
    private val getEventsInAssociationSlicedUseCase: IListSliceChildModelSuspendUseCase<Event, String>,
    private val getEventUseCase: IGetChildModelSuspendUseCase<Event, String, String>,
    private val createEventUseCase: ICreateChildModelSuspendUseCase<Event, CreateEventPayload, String>,
    private val updateEventUseCase: IUpdateChildModelSuspendUseCase<Event, String, UpdateEventPayload, String>,
    private val deleteEventUseCase: IDeleteChildModelSuspendUseCase<Event, String, String>,
) : IEventsController {

    override suspend fun create(call: ApplicationCall, parent: Association, payload: CreateEventPayload): Event {
        val user = requireUserForCallUseCase(call)
        payload.validated?.let {
            if (!checkPermissionUseCase(
                    user,
                    Permission.EVENTS_CREATE inAssociation parent.id
                )
            ) throw ControllerException(
                HttpStatusCode.Forbidden, "events_validated_not_allowed"
            )
        }
        return createEventUseCase(payload, parent.id) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

    override suspend fun delete(call: ApplicationCall, parent: Association, id: String) {
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

    override suspend fun get(call: ApplicationCall, parent: Association, id: String): Event {
        return getEventUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "events_not_found"
        )
    }

    override suspend fun list(call: ApplicationCall, parent: Association): List<Event> {
        if (call.request.path().contains("/admin/")) return getEventsInAssociationUseCase(parent.id)
        return getEventsInAssociationSlicedUseCase(
            call.parameters["limit"]?.toLongOrNull() ?: 25,
            call.parameters["offset"]?.toLongOrNull() ?: 0,
            parent.id
        )
    }

    override suspend fun update(
        call: ApplicationCall,
        parent: Association,
        id: String,
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
