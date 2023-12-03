package me.nathanfallet.suitebde.controllers.associations

import io.ktor.http.*
import io.ktor.server.application.*
import me.nathanfallet.ktorx.controllers.IModelController
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.users.IGetUserForCallUseCase
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.associations.UpdateAssociationPayload
import me.nathanfallet.suitebde.models.roles.AdminPermission
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationsUseCase
import me.nathanfallet.usecases.models.get.IGetModelSuspendUseCase
import me.nathanfallet.usecases.models.update.IUpdateModelSuspendUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase

class AssociationsController(
    private val getAssociationsUseCase: IGetAssociationsUseCase,
    private val getUserForCallUseCase: IGetUserForCallUseCase,
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getAssociationUseCase: IGetModelSuspendUseCase<Association, String>,
    private val updateAssociationUseCase: IUpdateModelSuspendUseCase<Association, String, UpdateAssociationPayload>
) : IModelController<Association, String, CreateAssociationPayload, UpdateAssociationPayload> {

    override suspend fun list(call: ApplicationCall): List<Association> {
        val showAll = getUserForCallUseCase(call)?.let {
            checkPermissionUseCase(it, AdminPermission)
        } ?: false
        return getAssociationsUseCase(!showAll)
    }

    override suspend fun get(call: ApplicationCall, id: String): Association {
        return getAssociationUseCase(id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "associations_not_found"
        )
    }

    override suspend fun create(call: ApplicationCall, payload: CreateAssociationPayload): Association {
        throw ControllerException(HttpStatusCode.MethodNotAllowed, "associations_create_not_allowed")
    }

    override suspend fun update(call: ApplicationCall, id: String, payload: UpdateAssociationPayload): Association {
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

    override suspend fun delete(call: ApplicationCall, id: String) {
        TODO("Not yet implemented")
    }

}
