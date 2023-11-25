package me.nathanfallet.suitebde.controllers.web

import io.ktor.server.application.*
import io.ktor.server.routing.*
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.models.templates.TemplateMapping
import me.nathanfallet.ktorx.routers.IChildModelRouter
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.controllers.models.PublicChildModelRouter
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.web.CreateWebPagePayload
import me.nathanfallet.suitebde.models.web.UpdateWebPagePayload
import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.suitebde.usecases.web.IGetPublicMenuForCallUseCase
import me.nathanfallet.usecases.models.annotations.ModelAnnotations

class WebPagesPublicRouter(
    webPagesController: IWebPagesController,
    associationsRouter: IChildModelRouter<Association, String, *, *, *, *>,
    getPublicMenuForCallUseCase: IGetPublicMenuForCallUseCase,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    mapping: TemplateMapping,
    respondTemplate: suspend ApplicationCall.(String, Map<String, Any>) -> Unit,
    route: String
) : PublicChildModelRouter<WebPage, String, CreateWebPagePayload, UpdateWebPagePayload, Association, String>(
    WebPage::class,
    CreateWebPagePayload::class,
    UpdateWebPagePayload::class,
    webPagesController,
    associationsRouter,
    getPublicMenuForCallUseCase,
    getLocaleForCallUseCase,
    mapping,
    respondTemplate,
    route
) {

    override fun createRoutes(root: Route) {
        super.createRoutes(root)
        createTemplateGetHomeRoute(root)
    }

    private suspend fun getHome(call: ApplicationCall): WebPage {
        return (controller as IWebPagesController).getHome(
            call,
            parentRouter!!.get(call)
        )
    }

    override suspend fun get(call: ApplicationCall): WebPage {
        return (controller as IWebPagesController).getByUrl(
            call,
            parentRouter!!.get(call),
            ModelAnnotations.constructIdFromString(modelClass, call.parameters[id]!!)
        )
    }

    private fun createTemplateGetHomeRoute(root: Route) {
        mapping.getTemplate ?: return
        root.get {
            try {
                call.respondTemplate(
                    mapping.getTemplate!!,
                    mapOf(
                        "route" to route,
                        "item" to getHome(call),
                        "keys" to modelKeys
                    )
                )
            } catch (exception: ControllerException) {
                handleExceptionTemplate(exception, call)
            }
        }
    }

}
