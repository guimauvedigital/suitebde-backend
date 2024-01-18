package me.nathanfallet.suitebde.controllers.roles

import io.ktor.http.*
import io.ktor.server.application.*
import me.nathanfallet.ktorx.controllers.IChildModelController
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.roles.CreateRolePayload
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.roles.Role
import me.nathanfallet.suitebde.models.roles.UpdateRolePayload
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import me.nathanfallet.usecases.models.delete.IDeleteChildModelSuspendUseCase
import me.nathanfallet.usecases.models.get.IGetChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.slice.IListSliceChildModelSuspendUseCase
import me.nathanfallet.usecases.models.update.IUpdateChildModelSuspendUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase

class RolesController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getRolesInAssociationUseCase: IListSliceChildModelSuspendUseCase<Role, String>,
    private val createRoleUseCase: ICreateChildModelSuspendUseCase<Role, CreateRolePayload, String>,
    private val getRoleUseCase: IGetChildModelSuspendUseCase<Role, String, String>,
    private val updateRoleUseCase: IUpdateChildModelSuspendUseCase<Role, String, UpdateRolePayload, String>,
    private val deleteRoleUseCase: IDeleteChildModelSuspendUseCase<Role, String, String>,
) : IChildModelController<Role, String, CreateRolePayload, UpdateRolePayload, Association, String> {

    override suspend fun list(call: ApplicationCall, parent: Association): List<Role> {
        return getRolesInAssociationUseCase(
            call.parameters["limit"]?.toLongOrNull() ?: 25,
            call.parameters["offset"]?.toLongOrNull() ?: 0,
            parent.id
        )
    }

    override suspend fun create(call: ApplicationCall, parent: Association, payload: CreateRolePayload): Role {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.ROLES_CREATE inAssociation parent)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "roles_create_not_allowed"
        )
        return createRoleUseCase(payload, parent.id) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

    override suspend fun get(call: ApplicationCall, parent: Association, id: String): Role {
        return getRoleUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "roles_not_found"
        )
    }

    override suspend fun update(
        call: ApplicationCall,
        parent: Association,
        id: String,
        payload: UpdateRolePayload,
    ): Role {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.ROLES_UPDATE inAssociation parent)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "roles_update_not_allowed"
        )
        val role = getRoleUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "roles_not_found"
        )
        return updateRoleUseCase(role.id, payload, parent.id) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

    override suspend fun delete(call: ApplicationCall, parent: Association, id: String) {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.ROLES_DELETE inAssociation parent)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "roles_delete_not_allowed"
        )
        val role = getRoleUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "roles_not_found"
        )
        if (!deleteRoleUseCase(role.id, parent.id)) throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

}
