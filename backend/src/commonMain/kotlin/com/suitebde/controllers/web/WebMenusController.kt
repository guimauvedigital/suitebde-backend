package com.suitebde.controllers.web

import com.suitebde.models.associations.Association
import com.suitebde.models.roles.Permission
import com.suitebde.models.web.CreateWebMenuPayload
import com.suitebde.models.web.UpdateWebMenuPayload
import com.suitebde.models.web.WebMenu
import dev.kaccelero.commons.exceptions.ControllerException
import dev.kaccelero.commons.permissions.ICheckPermissionSuspendUseCase
import dev.kaccelero.commons.repositories.*
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*

class WebMenusController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getWebMenusUseCase: IListChildModelSuspendUseCase<WebMenu, UUID>,
    private val getWebMenusSlicedUseCase: IListSliceChildModelSuspendUseCase<WebMenu, UUID>,
    private val getWebMenuUseCase: IGetChildModelSuspendUseCase<WebMenu, UUID, UUID>,
    private val createWebMenuUseCase: ICreateChildModelSuspendUseCase<WebMenu, CreateWebMenuPayload, UUID>,
    private val updateWebMenuUseCase: IUpdateChildModelSuspendUseCase<WebMenu, UUID, UpdateWebMenuPayload, UUID>,
    private val deleteWebMenuUseCase: IDeleteChildModelSuspendUseCase<WebMenu, UUID, UUID>,
) : IWebMenusController {

    override suspend fun list(call: ApplicationCall, parent: Association, limit: Long?, offset: Long?): List<WebMenu> {
        if (call.request.path().contains("/admin/")) return getWebMenusUseCase(parent.id)
        return getWebMenusSlicedUseCase(
            Pagination(
                limit ?: 25,
                offset ?: 0,
            ),
            parent.id
        )
    }

    override suspend fun get(call: ApplicationCall, parent: Association, id: UUID): WebMenu {
        return getWebMenuUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "webmenus_not_found"
        )
    }

    override suspend fun create(call: ApplicationCall, parent: Association, payload: CreateWebMenuPayload): WebMenu {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.WEBMENUS_CREATE inAssociation parent.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "webmenus_create_not_allowed"
        )
        return createWebMenuUseCase(payload, parent.id) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

    override suspend fun update(
        call: ApplicationCall,
        parent: Association,
        id: UUID,
        payload: UpdateWebMenuPayload,
    ): WebMenu {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.WEBMENUS_UPDATE inAssociation parent.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "webmenus_update_not_allowed"
        )
        val menu = getWebMenuUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "webmenus_not_found"
        )
        return updateWebMenuUseCase(menu.id, payload, parent.id) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

    override suspend fun delete(call: ApplicationCall, parent: Association, id: UUID) {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.WEBMENUS_DELETE inAssociation parent.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "webmenus_delete_not_allowed"
        )
        val menu = getWebMenuUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "webmenus_not_found"
        )
        if (!deleteWebMenuUseCase(menu.id, parent.id)) throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

}
