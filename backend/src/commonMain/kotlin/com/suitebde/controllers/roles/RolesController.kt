package com.suitebde.controllers.roles

import com.suitebde.models.associations.Association
import com.suitebde.models.roles.CreateRolePayload
import com.suitebde.models.roles.Permission
import com.suitebde.models.roles.Role
import com.suitebde.models.roles.UpdateRolePayload
import dev.kaccelero.commons.exceptions.ControllerException
import dev.kaccelero.commons.permissions.ICheckPermissionSuspendUseCase
import dev.kaccelero.commons.repositories.*
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*

class RolesController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getRolesInAssociationUseCase: IListChildModelSuspendUseCase<Role, UUID>,
    private val getRolesInAssociationSlicedUseCase: IListSliceChildModelSuspendUseCase<Role, UUID>,
    private val createRoleUseCase: ICreateChildModelSuspendUseCase<Role, CreateRolePayload, UUID>,
    private val getRoleUseCase: IGetChildModelSuspendUseCase<Role, UUID, UUID>,
    private val updateRoleUseCase: IUpdateChildModelSuspendUseCase<Role, UUID, UpdateRolePayload, UUID>,
    private val deleteRoleUseCase: IDeleteChildModelSuspendUseCase<Role, UUID, UUID>,
) : IRolesController {

    override suspend fun list(call: ApplicationCall, parent: Association, limit: Long?, offset: Long?): List<Role> {
        if (call.request.path().contains("/admin/")) return getRolesInAssociationUseCase(parent.id)
        return getRolesInAssociationSlicedUseCase(
            Pagination(
                limit ?: 25,
                offset ?: 0,
            ),
            parent.id
        )
    }

    override suspend fun create(call: ApplicationCall, parent: Association, payload: CreateRolePayload): Role {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.ROLES_CREATE inAssociation parent.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "roles_create_not_allowed"
        )
        return createRoleUseCase(payload, parent.id) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

    override suspend fun get(call: ApplicationCall, parent: Association, id: UUID): Role {
        return getRoleUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "roles_not_found"
        )
    }

    override suspend fun update(
        call: ApplicationCall,
        parent: Association,
        id: UUID,
        payload: UpdateRolePayload,
    ): Role {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.ROLES_UPDATE inAssociation parent.id)
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

    override suspend fun delete(call: ApplicationCall, parent: Association, id: UUID) {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.ROLES_DELETE inAssociation parent.id)
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
