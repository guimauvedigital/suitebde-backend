package me.nathanfallet.suitebde.controllers.associations

import io.ktor.http.*
import io.ktor.server.application.*
import me.nathanfallet.ktor.routers.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.associations.UpdateAssociationPayload
import me.nathanfallet.suitebde.models.roles.AdminPermission
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationsUseCase
import me.nathanfallet.suitebde.usecases.roles.ICheckPermissionUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUserForCallUseCase
import me.nathanfallet.usecases.models.get.IGetModelSuspendUseCase
import me.nathanfallet.usecases.models.update.IUpdateModelSuspendUseCase

class AssociationController(
    private val getAssociationsUseCase: IGetAssociationsUseCase,
    private val getUserForCallUseCase: IGetUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionUseCase,
    private val getAssociationUseCase: IGetModelSuspendUseCase<Association, String>,
    private val updateAssociationUseCase: IUpdateModelSuspendUseCase<Association, String, UpdateAssociationPayload>
) : IAssociationController {

    private suspend fun requireUser(call: ApplicationCall): User {
        return getUserForCallUseCase(call) ?: throw ControllerException(
            HttpStatusCode.Unauthorized, "auth_invalid_credentials"
        )
    }

    override suspend fun getAll(call: ApplicationCall): List<Association> {
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
        requireUser(call).takeIf {
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