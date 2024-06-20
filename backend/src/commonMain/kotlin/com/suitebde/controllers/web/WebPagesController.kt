package com.suitebde.controllers.web

import com.suitebde.models.associations.Association
import com.suitebde.models.roles.Permission
import com.suitebde.models.web.WebPage
import com.suitebde.models.web.WebPagePayload
import com.suitebde.usecases.web.IGetWebPageByUrlUseCase
import dev.kaccelero.commons.exceptions.ControllerException
import dev.kaccelero.commons.permissions.ICheckPermissionSuspendUseCase
import dev.kaccelero.commons.repositories.*
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*

class WebPagesController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getWebPagesUseCase: IListChildModelSuspendUseCase<WebPage, UUID>,
    private val getWebPagesSlicedUseCase: IListSliceChildModelSuspendUseCase<WebPage, UUID>,
    private val getWebPageByUrlUseCase: IGetWebPageByUrlUseCase,
    private val getWebPageUseCase: IGetChildModelSuspendUseCase<WebPage, UUID, UUID>,
    private val createWebPageUseCase: ICreateChildModelSuspendUseCase<WebPage, WebPagePayload, UUID>,
    private val updateWebPageUseCase: IUpdateChildModelSuspendUseCase<WebPage, UUID, WebPagePayload, UUID>,
    private val deleteWebPageUseCase: IDeleteChildModelSuspendUseCase<WebPage, UUID, UUID>,
) : IWebPagesController {

    override suspend fun list(call: ApplicationCall, parent: Association, limit: Long?, offset: Long?): List<WebPage> {
        if (call.request.path().contains("/admin/")) return getWebPagesUseCase(parent.id)
        return getWebPagesSlicedUseCase(
            Pagination(
                limit ?: 25,
                offset ?: 0,
            ),
            parent.id
        )
    }

    override suspend fun get(call: ApplicationCall, parent: Association, id: UUID): WebPage {
        return getWebPageUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "webpages_not_found"
        )
    }

    override suspend fun getByUrl(call: ApplicationCall, parent: Association, url: String): WebPage {
        return getWebPageByUrlUseCase(url, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "webpages_not_found"
        )
    }

    override suspend fun create(call: ApplicationCall, parent: Association, payload: WebPagePayload): WebPage {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.WEBPAGES_CREATE inAssociation parent.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "webpages_create_not_allowed"
        )
        return createWebPageUseCase(payload, parent.id) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

    override suspend fun update(
        call: ApplicationCall,
        parent: Association,
        id: UUID,
        payload: WebPagePayload,
    ): WebPage {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.WEBPAGES_UPDATE inAssociation parent.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "webpages_update_not_allowed"
        )
        val page = getWebPageUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "webpages_not_found"
        )
        return updateWebPageUseCase(page.id, payload, parent.id) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

    override suspend fun delete(call: ApplicationCall, parent: Association, id: UUID) {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.WEBPAGES_DELETE inAssociation parent.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "webpages_delete_not_allowed"
        )
        val page = getWebPageUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "webpages_not_found"
        )
        if (!deleteWebPageUseCase(page.id, parent.id)) throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

}
