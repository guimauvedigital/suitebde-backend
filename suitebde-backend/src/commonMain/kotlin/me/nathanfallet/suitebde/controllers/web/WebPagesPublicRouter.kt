package me.nathanfallet.suitebde.controllers.web

import io.ktor.server.application.*
import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.routers.IChildModelRouter
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.controllers.models.PublicChildModelRouter
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.suitebde.models.web.WebPagePayload
import me.nathanfallet.suitebde.usecases.web.IGetPublicMenuForCallUseCase
import me.nathanfallet.usecases.models.annotations.ModelAnnotations

class WebPagesPublicRouter(
    webPagesController: IWebPagesController,
    associationsRouter: IChildModelRouter<Association, String, *, *, *, *>,
    getPublicMenuForCallUseCase: IGetPublicMenuForCallUseCase,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    respondTemplate: suspend ApplicationCall.(String, Map<String, Any?>) -> Unit,
) : PublicChildModelRouter<WebPage, String, WebPagePayload, WebPagePayload, Association, String>(
    typeInfo<WebPage>(),
    typeInfo<WebPagePayload>(),
    typeInfo<WebPagePayload>(),
    webPagesController,
    IWebPagesController::class,
    associationsRouter,
    getPublicMenuForCallUseCase,
    getLocaleForCallUseCase,
    respondTemplate,
    "pages"
) {

    override suspend fun get(call: ApplicationCall): WebPage {
        return (controller as IWebPagesController).getByUrl(
            call,
            parentRouter!!.get(call),
            ModelAnnotations.constructIdFromString(WebPage::class, call.parameters[id]!!)
        )
    }

}
