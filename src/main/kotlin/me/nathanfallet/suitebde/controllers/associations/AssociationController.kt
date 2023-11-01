package me.nathanfallet.suitebde.controllers.associations

import io.ktor.http.*
import io.ktor.server.application.*
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.models.ModelKey
import me.nathanfallet.suitebde.models.models.ModelKeyType
import me.nathanfallet.suitebde.models.roles.AdminPermission
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationUseCase
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationsUseCase
import me.nathanfallet.suitebde.usecases.roles.ICheckPermissionUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUserForCallUseCase

class AssociationController(
    private val getAssociationsUseCase: IGetAssociationsUseCase,
    private val getAssociationUseCase: IGetAssociationUseCase,
    private val getUserForCallUseCase: IGetUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionUseCase,
) : IAssociationController {

    override val modelKeys = listOf(
        ModelKey("id", ModelKeyType.ID),
        ModelKey("name", ModelKeyType.STRING),
    )

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

    override suspend fun create(call: ApplicationCall, payload: Unit): Association {
        TODO("Not yet implemented")
    }

    override suspend fun update(call: ApplicationCall, id: String, payload: Unit): Association {
        TODO("Not yet implemented")
    }

    override suspend fun delete(call: ApplicationCall, id: String) {
        TODO("Not yet implemented")
    }

}