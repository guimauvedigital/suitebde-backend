package me.nathanfallet.suitebde.controllers.users

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.roles.IGetPermissionsForUserUseCase
import me.nathanfallet.usecases.models.get.IGetChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.slice.IListSliceChildModelSuspendUseCase
import me.nathanfallet.usecases.models.update.IUpdateChildModelSuspendUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase

class UsersController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getUsersInAssociationUseCase: IListChildModelSuspendUseCase<User, String>,
    private val getUsersInAssociationSlicedUseCase: IListSliceChildModelSuspendUseCase<User, String>,
    private val getUserUseCase: IGetChildModelSuspendUseCase<User, String, String>,
    private val updateUserUseCase: IUpdateChildModelSuspendUseCase<User, String, UpdateUserPayload, String>,
    private val getPermissionsForUserUseCase: IGetPermissionsForUserUseCase,
) : IUsersController {

    override suspend fun list(call: ApplicationCall, parent: Association): List<User> {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.USERS_VIEW inAssociation parent.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "users_view_not_allowed"
        )
        if (call.request.path().contains("/admin/")) return getUsersInAssociationUseCase(parent.id)
        return getUsersInAssociationSlicedUseCase(
            call.parameters["limit"]?.toLongOrNull() ?: 25,
            call.parameters["offset"]?.toLongOrNull() ?: 0,
            parent.id
        )
    }

    override suspend fun get(call: ApplicationCall, parent: Association, id: String): User {
        (requireUserForCallUseCase(call) as User).takeIf {
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
        id: String,
        payload: UpdateUserPayload,
    ): User {
        (requireUserForCallUseCase(call) as User).takeIf {
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

    override suspend fun listPermissions(call: ApplicationCall, parent: Association, id: String): List<Permission> {
        (requireUserForCallUseCase(call) as User).takeIf {
            it.id == id || checkPermissionUseCase(it, Permission.USERS_VIEW inAssociation parent.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "users_view_not_allowed"
        )
        val targetUser = getUserUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "users_not_found"
        )
        return getPermissionsForUserUseCase(targetUser).toList()
    }

}
