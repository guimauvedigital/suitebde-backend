package com.suitebde.controllers.roles

import com.suitebde.models.roles.CreateUserInRolePayload
import com.suitebde.models.roles.Permission
import com.suitebde.models.roles.Role
import com.suitebde.models.roles.UserInRole
import dev.kaccelero.commons.exceptions.ControllerException
import dev.kaccelero.commons.permissions.ICheckPermissionSuspendUseCase
import dev.kaccelero.commons.repositories.*
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*

class UsersInRolesController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getUsersInRolesUseCase: IListChildModelSuspendUseCase<UserInRole, UUID>,
    private val getUsersInRolesSlicedUseCase: IListSliceChildModelSuspendUseCase<UserInRole, UUID>,
    private val createUserInRolesUseCase: ICreateChildModelSuspendUseCase<UserInRole, CreateUserInRolePayload, UUID>,
    private val getUserInRoleUseCase: IGetChildModelSuspendUseCase<UserInRole, UUID, UUID>,
    private val deleteUserInRoleUseCase: IDeleteChildModelSuspendUseCase<UserInRole, UUID, UUID>,
) : IUsersInRolesController {

    override suspend fun create(call: ApplicationCall, parent: Role, payload: CreateUserInRolePayload): UserInRole {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.ROLES_USERS inAssociation parent.associationId)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "users_in_roles_not_allowed"
        )
        return createUserInRolesUseCase(payload, parent.id) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

    override suspend fun delete(call: ApplicationCall, parent: Role, id: UUID) {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.ROLES_USERS inAssociation parent.associationId)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "users_in_roles_not_allowed"
        )
        val userInRole = getUserInRoleUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "users_in_roles_not_found"
        )
        if (!deleteUserInRoleUseCase(userInRole.id, parent.id)) throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

    override suspend fun get(call: ApplicationCall, parent: Role, id: UUID): UserInRole {
        return getUserInRoleUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "users_in_roles_not_found"
        )
    }

    override suspend fun list(call: ApplicationCall, parent: Role, limit: Long?, offset: Long?): List<UserInRole> {
        if (call.request.path().contains("/admin/")) return getUsersInRolesUseCase(parent.id)
        return getUsersInRolesSlicedUseCase(
            Pagination(
                limit ?: 25,
                offset ?: 0,
            ),
            parent.id
        )
    }

}
