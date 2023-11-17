package me.nathanfallet.suitebde.controllers.web

import io.ktor.http.*
import io.ktor.server.application.*
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.web.CreateWebPagePayload
import me.nathanfallet.suitebde.models.web.UpdateWebPagePayload
import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.suitebde.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.usecases.web.IGetHomeWebPageUseCase
import me.nathanfallet.suitebde.usecases.web.IGetWebPageByUrlUseCase
import me.nathanfallet.suitebde.usecases.web.IGetWebPagesUseCase
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import me.nathanfallet.usecases.models.delete.IDeleteChildModelSuspendUseCase
import me.nathanfallet.usecases.models.get.IGetChildModelSuspendUseCase
import me.nathanfallet.usecases.models.update.IUpdateChildModelSuspendUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase

class WebPagesController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getWebPagesUseCase: IGetWebPagesUseCase,
    private val getWebPageByUrlUseCase: IGetWebPageByUrlUseCase,
    private val getHomeWebPageUseCase: IGetHomeWebPageUseCase,
    private val getWebPageUseCase: IGetChildModelSuspendUseCase<WebPage, String, String>,
    private val createWebPageUseCase: ICreateChildModelSuspendUseCase<WebPage, CreateWebPagePayload, String>,
    private val updateWebPageUseCase: IUpdateChildModelSuspendUseCase<WebPage, String, UpdateWebPagePayload, String>,
    private val deleteWebPageUseCase: IDeleteChildModelSuspendUseCase<WebPage, String, String>
) : IWebPagesController {

    override suspend fun getAll(call: ApplicationCall, parent: Association): List<WebPage> {
        return getWebPagesUseCase(parent.id)
    }

    override suspend fun get(call: ApplicationCall, parent: Association, id: String): WebPage {
        return getWebPageUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "webpages_not_found"
        )
    }

    override suspend fun getByUrl(call: ApplicationCall, parent: Association, url: String): WebPage {
        return getWebPageByUrlUseCase(url, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "webpages_not_found"
        )
    }

    override suspend fun getHome(call: ApplicationCall, parent: Association): WebPage {
        return getHomeWebPageUseCase(parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "webpages_not_found"
        )
    }

    override suspend fun create(call: ApplicationCall, parent: Association, payload: CreateWebPagePayload): WebPage {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.WEB_PAGES_CREATE inAssociation parent)
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
        id: String,
        payload: UpdateWebPagePayload
    ): WebPage {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.WEB_PAGES_UPDATE inAssociation parent)
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

    override suspend fun delete(call: ApplicationCall, parent: Association, id: String) {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.WEB_PAGES_DELETE inAssociation parent)
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
