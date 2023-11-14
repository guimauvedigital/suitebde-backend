package me.nathanfallet.suitebde.controllers.web

import io.ktor.http.*
import io.ktor.server.application.*
import me.nathanfallet.ktor.routers.controllers.base.IChildModelController
import me.nathanfallet.ktor.routers.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.web.CreateWebPagePayload
import me.nathanfallet.suitebde.models.web.UpdateWebPagePayload
import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.usecases.models.get.IGetChildModelSuspendUseCase

class WebPagesController(
    private val getWebPageUseCase: IGetChildModelSuspendUseCase<WebPage, String, String>
) : IChildModelController<WebPage, String, CreateWebPagePayload, UpdateWebPagePayload, Association, String> {

    override suspend fun create(call: ApplicationCall, parent: Association, payload: CreateWebPagePayload): WebPage {
        TODO("Not yet implemented")
    }

    override suspend fun delete(call: ApplicationCall, parent: Association, id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun get(call: ApplicationCall, parent: Association, id: String): WebPage {
        return getWebPageUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "web_pages_not_found"
        )
    }

    override suspend fun getAll(call: ApplicationCall, parent: Association): List<WebPage> {
        TODO("Not yet implemented")
    }

    override suspend fun update(
        call: ApplicationCall,
        parent: Association,
        id: String,
        payload: UpdateWebPagePayload
    ): WebPage {
        TODO("Not yet implemented")
    }

}
