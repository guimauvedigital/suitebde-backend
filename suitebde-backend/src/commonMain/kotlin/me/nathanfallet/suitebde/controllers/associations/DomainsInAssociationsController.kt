package me.nathanfallet.suitebde.controllers.associations

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateDomainInAssociationPayload
import me.nathanfallet.suitebde.models.associations.DomainInAssociation
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import me.nathanfallet.usecases.models.delete.IDeleteChildModelSuspendUseCase
import me.nathanfallet.usecases.models.get.IGetChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.slice.IListSliceChildModelSuspendUseCase
import me.nathanfallet.usecases.pagination.Pagination
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase

class DomainsInAssociationsController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getDomainsInAssociationsUseCase: IListChildModelSuspendUseCase<DomainInAssociation, String>,
    private val getDomainsInAssociationsSlicedUseCase: IListSliceChildModelSuspendUseCase<DomainInAssociation, String>,
    private val getDomainUseCase: IGetChildModelSuspendUseCase<DomainInAssociation, String, String>,
    private val createDomainUseCase: ICreateChildModelSuspendUseCase<DomainInAssociation, CreateDomainInAssociationPayload, String>,
    private val deleteDomainUseCase: IDeleteChildModelSuspendUseCase<DomainInAssociation, String, String>,
) : IDomainsInAssociationsController {

    override suspend fun list(
        call: ApplicationCall,
        parent: Association,
        limit: Long?,
        offset: Long?,
    ): List<DomainInAssociation> {
        if (call.request.path().contains("/admin/")) return getDomainsInAssociationsUseCase(parent.id)
        return getDomainsInAssociationsSlicedUseCase(
            Pagination(
                limit ?: 25,
                offset ?: 0,
            ),
            parent.id
        )
    }

    override suspend fun get(call: ApplicationCall, parent: Association, id: String): DomainInAssociation {
        return getDomainUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "domains_not_found"
        )
    }

    override suspend fun create(
        call: ApplicationCall,
        parent: Association,
        payload: CreateDomainInAssociationPayload,
    ): DomainInAssociation {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.DOMAINS_CREATE inAssociation parent.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "domains_update_not_allowed"
        )
        return createDomainUseCase(payload, parent.id) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

    override suspend fun delete(call: ApplicationCall, parent: Association, id: String) {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.DOMAINS_DELETE inAssociation parent.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "domains_delete_not_allowed"
        )
        val domain = getDomainUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "domains_not_found"
        )
        if (!deleteDomainUseCase(domain.id, parent.id)) throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

}
