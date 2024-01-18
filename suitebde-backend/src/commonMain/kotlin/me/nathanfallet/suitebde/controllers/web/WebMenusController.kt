package me.nathanfallet.suitebde.controllers.web

import io.ktor.http.*
import io.ktor.server.application.*
import me.nathanfallet.ktorx.controllers.IChildModelController
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.web.CreateWebMenuPayload
import me.nathanfallet.suitebde.models.web.UpdateWebMenuPayload
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import me.nathanfallet.usecases.models.delete.IDeleteChildModelSuspendUseCase
import me.nathanfallet.usecases.models.get.IGetChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.slice.IListSliceChildModelSuspendUseCase
import me.nathanfallet.usecases.models.update.IUpdateChildModelSuspendUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase

class WebMenusController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getWebMenusUseCase: IListSliceChildModelSuspendUseCase<WebMenu, String>,
    private val getWebMenuUseCase: IGetChildModelSuspendUseCase<WebMenu, String, String>,
    private val createWebMenuUseCase: ICreateChildModelSuspendUseCase<WebMenu, CreateWebMenuPayload, String>,
    private val updateWebMenuUseCase: IUpdateChildModelSuspendUseCase<WebMenu, String, UpdateWebMenuPayload, String>,
    private val deleteWebMenuUseCase: IDeleteChildModelSuspendUseCase<WebMenu, String, String>,
) : IChildModelController<WebMenu, String, CreateWebMenuPayload, UpdateWebMenuPayload, Association, String> {

    override suspend fun list(call: ApplicationCall, parent: Association): List<WebMenu> {
        return getWebMenusUseCase(
            call.parameters["limit"]?.toLongOrNull() ?: 25,
            call.parameters["offset"]?.toLongOrNull() ?: 0,
            parent.id
        )
    }

    override suspend fun get(call: ApplicationCall, parent: Association, id: String): WebMenu {
        return getWebMenuUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "webmenus_not_found"
        )
    }

    override suspend fun create(call: ApplicationCall, parent: Association, payload: CreateWebMenuPayload): WebMenu {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.WEBMENUS_CREATE inAssociation parent)
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
        id: String,
        payload: UpdateWebMenuPayload,
    ): WebMenu {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.WEBMENUS_UPDATE inAssociation parent)
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

    override suspend fun delete(call: ApplicationCall, parent: Association, id: String) {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.WEBMENUS_DELETE inAssociation parent)
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
