package me.nathanfallet.suitebde.controllers.associations

import io.ktor.http.*
import io.ktor.server.application.*
import me.nathanfallet.ktor.routers.controllers.base.IChildModelController
import me.nathanfallet.ktor.routers.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateDomainInAssociationPayload
import me.nathanfallet.suitebde.models.associations.DomainInAssociation
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.associations.IGetDomainsInAssociationsUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUserForCallUseCase
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import me.nathanfallet.usecases.models.delete.IDeleteChildModelSuspendUseCase
import me.nathanfallet.usecases.models.get.IGetChildModelSuspendUseCase
import me.nathanfallet.usecases.models.update.IUpdateChildModelSuspendUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase

class DomainsInAssociationsController(
    private val getUserForCallUseCase: IGetUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getDomainsInAssociationsUseCase: IGetDomainsInAssociationsUseCase,
    private val getDomainUseCase: IGetChildModelSuspendUseCase<DomainInAssociation, String, String>,
    private val createDomainUseCase: ICreateChildModelSuspendUseCase<DomainInAssociation, CreateDomainInAssociationPayload, String>,
    private val updateDomainUseCase: IUpdateChildModelSuspendUseCase<DomainInAssociation, String, Unit, String>,
    private val deleteDomainUseCase: IDeleteChildModelSuspendUseCase<DomainInAssociation, String, String>,
) : IChildModelController<DomainInAssociation, String, CreateDomainInAssociationPayload, Unit, Association, String> {

    private suspend fun requireUser(call: ApplicationCall): User {
        return getUserForCallUseCase(call) ?: throw ControllerException(
            HttpStatusCode.Unauthorized, "auth_invalid_credentials"
        )
    }

    override suspend fun getAll(call: ApplicationCall, parent: Association): List<DomainInAssociation> {
        return getDomainsInAssociationsUseCase(parent.id)
    }

    override suspend fun get(call: ApplicationCall, parent: Association, id: String): DomainInAssociation {
        return getDomainUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "domains_not_found"
        )
    }

    override suspend fun create(
        call: ApplicationCall,
        parent: Association,
        payload: CreateDomainInAssociationPayload
    ): DomainInAssociation {
        requireUser(call).takeIf {
            checkPermissionUseCase(it, Permission.DOMAINS_CREATE inAssociation parent)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "domains_update_not_allowed"
        )
        return createDomainUseCase(payload, parent.id) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

    override suspend fun update(
        call: ApplicationCall,
        parent: Association,
        id: String,
        payload: Unit
    ): DomainInAssociation {
        requireUser(call).takeIf {
            checkPermissionUseCase(it, Permission.DOMAINS_UPDATE inAssociation parent)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "domains_update_not_allowed"
        )
        return updateDomainUseCase(id, payload, parent.id) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

    override suspend fun delete(call: ApplicationCall, parent: Association, id: String) {
        requireUser(call).takeIf {
            checkPermissionUseCase(it, Permission.DOMAINS_DELETE inAssociation parent)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "domains_update_not_allowed"
        )
        if (!deleteDomainUseCase(id, parent.id)) throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

}
