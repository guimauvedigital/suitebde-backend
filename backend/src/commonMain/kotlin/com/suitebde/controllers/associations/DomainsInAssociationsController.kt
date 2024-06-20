package com.suitebde.controllers.associations

import com.suitebde.models.associations.Association
import com.suitebde.models.associations.CreateDomainInAssociationPayload
import com.suitebde.models.associations.DomainInAssociation
import com.suitebde.models.roles.Permission
import dev.kaccelero.commons.exceptions.ControllerException
import dev.kaccelero.commons.permissions.ICheckPermissionSuspendUseCase
import dev.kaccelero.commons.repositories.*
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*

class DomainsInAssociationsController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getDomainsInAssociationsUseCase: IListChildModelSuspendUseCase<DomainInAssociation, UUID>,
    private val getDomainsInAssociationsSlicedUseCase: IListSliceChildModelSuspendUseCase<DomainInAssociation, UUID>,
    private val getDomainUseCase: IGetChildModelSuspendUseCase<DomainInAssociation, String, UUID>,
    private val createDomainUseCase: ICreateChildModelSuspendUseCase<DomainInAssociation, CreateDomainInAssociationPayload, UUID>,
    private val deleteDomainUseCase: IDeleteChildModelSuspendUseCase<DomainInAssociation, String, UUID>,
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
