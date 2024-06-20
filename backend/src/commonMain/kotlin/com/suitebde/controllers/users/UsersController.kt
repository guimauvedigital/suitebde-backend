package com.suitebde.controllers.users

import com.suitebde.models.application.SearchOptions
import com.suitebde.models.associations.Association
import com.suitebde.models.roles.Permission
import com.suitebde.models.users.UpdateUserPayload
import com.suitebde.models.users.User
import com.suitebde.usecases.roles.IGetPermissionsForUserUseCase
import com.suitebde.usecases.users.IExportUsersAsCsvUseCase
import dev.kaccelero.commons.exceptions.ControllerException
import dev.kaccelero.commons.permissions.ICheckPermissionSuspendUseCase
import dev.kaccelero.commons.repositories.*
import dev.kaccelero.commons.responses.BytesResponse
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class UsersController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getUsersInAssociationUseCase: IListChildModelSuspendUseCase<User, UUID>,
    private val getUsersInAssociationSlicedUseCase: IListSliceChildModelSuspendUseCase<User, UUID>,
    private val getUserUseCase: IGetChildModelSuspendUseCase<User, UUID, UUID>,
    private val updateUserUseCase: IUpdateChildModelSuspendUseCase<User, UUID, UpdateUserPayload, UUID>,
    private val deleteUserUseCase: IDeleteChildModelSuspendUseCase<User, UUID, UUID>,
    private val getPermissionsForUserUseCase: IGetPermissionsForUserUseCase,
    private val exportUsersAsCsvUseCase: IExportUsersAsCsvUseCase,
) : IUsersController {

    override suspend fun list(
        call: ApplicationCall,
        parent: Association,
        limit: Long?,
        offset: Long?,
        search: String?,
    ): List<User> {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.USERS_VIEW inAssociation parent.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "users_view_not_allowed"
        )
        if (call.request.path().contains("/admin/")) return getUsersInAssociationUseCase(parent.id)
        return getUsersInAssociationSlicedUseCase(
            Pagination(
                limit ?: 25,
                offset ?: 0,
                search?.let { SearchOptions(it) }
            ),
            parent.id
        )
    }

    override suspend fun get(call: ApplicationCall, parent: Association, id: UUID): User {
        (requireUserForCallUseCase(call) as? User)?.takeIf {
            it.id == id || checkPermissionUseCase(it, Permission.USERS_VIEW inAssociation parent.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "users_view_not_allowed"
        )
        return getUserUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "users_not_found"
        )
    }

    override suspend fun update(
        call: ApplicationCall,
        parent: Association,
        id: UUID,
        payload: UpdateUserPayload,
    ): User {
        (requireUserForCallUseCase(call) as? User)?.takeIf {
            it.id == id || checkPermissionUseCase(it, Permission.USERS_UPDATE inAssociation parent.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "users_update_not_allowed"
        )
        val targetUser = getUserUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "users_not_found"
        )
        return updateUserUseCase(
            targetUser.id, payload, parent.id
        ) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

    override suspend fun delete(call: ApplicationCall, parent: Association, id: UUID) {
        (requireUserForCallUseCase(call) as? User)?.takeIf {
            it.id == id || checkPermissionUseCase(it, Permission.USERS_DELETE inAssociation parent.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "users_delete_not_allowed"
        )
        val targetUser = getUserUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "users_not_found"
        )
        if (!deleteUserUseCase(targetUser.id, parent.id)) throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

    override suspend fun listPermissions(call: ApplicationCall, parent: Association, id: UUID): List<Permission> {
        (requireUserForCallUseCase(call) as? User)?.takeIf {
            it.id == id || checkPermissionUseCase(it, Permission.USERS_VIEW inAssociation parent.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "users_view_not_allowed"
        )
        val targetUser = getUserUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "users_not_found"
        )
        return getPermissionsForUserUseCase(targetUser).toList()
    }

    override suspend fun csv(call: ApplicationCall, parent: Association): BytesResponse {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.USERS_VIEW inAssociation parent.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "users_view_not_allowed"
        )
        call.response.header("Content-Disposition", "attachment; filename=\"users.csv\"")
        return BytesResponse(
            exportUsersAsCsvUseCase(getUsersInAssociationUseCase(parent.id).sortedBy { it.lastName }).toByteArray(),
            ContentType.Text.CSV
        )
    }

}
