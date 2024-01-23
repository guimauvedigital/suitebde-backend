package me.nathanfallet.suitebde.controllers.clubs

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import me.nathanfallet.ktorx.controllers.IChildModelController
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.clubs.CreateClubPayload
import me.nathanfallet.suitebde.models.clubs.UpdateClubPayload
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import me.nathanfallet.usecases.models.delete.IDeleteChildModelSuspendUseCase
import me.nathanfallet.usecases.models.get.IGetChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.slice.IListSliceChildModelSuspendUseCase
import me.nathanfallet.usecases.models.update.IUpdateChildModelSuspendUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase

class ClubsController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getClubsInAssociationUseCase: IListChildModelSuspendUseCase<Club, String>,
    private val getClubsInAssociationSlicedUseCase: IListSliceChildModelSuspendUseCase<Club, String>,
    private val getClubUseCase: IGetChildModelSuspendUseCase<Club, String, String>,
    private val createClubUseCase: ICreateChildModelSuspendUseCase<Club, CreateClubPayload, String>,
    private val updateClubUseCase: IUpdateChildModelSuspendUseCase<Club, String, UpdateClubPayload, String>,
    private val deleteClubUseCase: IDeleteChildModelSuspendUseCase<Club, String, String>,
) : IChildModelController<Club, String, CreateClubPayload, UpdateClubPayload, Association, String> {

    override suspend fun create(call: ApplicationCall, parent: Association, payload: CreateClubPayload): Club {
        val user = requireUserForCallUseCase(call)
        payload.validated?.let {
            if (!checkPermissionUseCase(
                    user,
                    Permission.CLUBS_CREATE inAssociation parent.id
                )
            ) throw ControllerException(
                HttpStatusCode.Forbidden, "clubs_validated_not_allowed"
            )
        }
        return createClubUseCase(payload, parent.id) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

    override suspend fun delete(call: ApplicationCall, parent: Association, id: String) {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.CLUBS_DELETE inAssociation parent.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "clubs_delete_not_allowed"
        )
        val event = getClubUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "clubs_not_found"
        )
        if (!deleteClubUseCase(event.id, parent.id)) throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

    override suspend fun get(call: ApplicationCall, parent: Association, id: String): Club {
        return getClubUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "clubs_not_found"
        )
    }

    override suspend fun list(call: ApplicationCall, parent: Association): List<Club> {
        if (call.request.path().contains("/admin/")) return getClubsInAssociationUseCase(parent.id)
        return getClubsInAssociationSlicedUseCase(
            call.parameters["limit"]?.toLongOrNull() ?: 25,
            call.parameters["offset"]?.toLongOrNull() ?: 0,
            parent.id
        )
    }

    override suspend fun update(
        call: ApplicationCall,
        parent: Association,
        id: String,
        payload: UpdateClubPayload,
    ): Club {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.CLUBS_UPDATE inAssociation parent.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "clubs_update_not_allowed"
        )
        val event = getClubUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "clubs_not_found"
        )
        return updateClubUseCase(event.id, payload, parent.id) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

}
