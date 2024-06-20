package com.suitebde.controllers.associations

import com.suitebde.models.associations.Association
import com.suitebde.models.associations.UpdateAssociationPayload
import com.suitebde.models.roles.AdminPermission
import com.suitebde.usecases.associations.IGetAssociationsUseCase
import dev.kaccelero.commons.exceptions.ControllerException
import dev.kaccelero.commons.permissions.ICheckPermissionSuspendUseCase
import dev.kaccelero.commons.repositories.IGetModelSuspendUseCase
import dev.kaccelero.commons.repositories.IUpdateModelSuspendUseCase
import dev.kaccelero.commons.users.IGetUserForCallUseCase
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import io.ktor.http.*
import io.ktor.server.application.*

class AssociationsController(
    private val getAssociationsUseCase: IGetAssociationsUseCase,
    private val getUserForCallUseCase: IGetUserForCallUseCase,
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getAssociationUseCase: IGetModelSuspendUseCase<Association, UUID>,
    private val updateAssociationUseCase: IUpdateModelSuspendUseCase<Association, UUID, UpdateAssociationPayload>,
) : IAssociationsController {

    override suspend fun list(call: ApplicationCall): List<Association> {
        val showAll = getUserForCallUseCase(call)?.let {
            checkPermissionUseCase(it, AdminPermission)
        } ?: false
        return getAssociationsUseCase(!showAll)
    }

    override suspend fun get(call: ApplicationCall, id: UUID): Association {
        return getAssociationUseCase(id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "associations_not_found"
        )
    }

    override suspend fun update(call: ApplicationCall, id: UUID, payload: UpdateAssociationPayload): Association {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, AdminPermission)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "associations_update_not_allowed"
        )
        val association = getAssociationUseCase(id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "associations_not_found"
        )
        return updateAssociationUseCase(
            association.id, payload
        ) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

}
