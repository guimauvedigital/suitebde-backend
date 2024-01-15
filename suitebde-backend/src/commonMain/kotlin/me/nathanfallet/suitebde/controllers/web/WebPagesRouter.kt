package me.nathanfallet.suitebde.controllers.web

import io.ktor.server.freemarker.*
import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.models.templates.TemplateMapping
import me.nathanfallet.ktorx.routers.api.APIChildModelRouter
import me.nathanfallet.ktorx.routers.concat.ConcatChildModelRouter
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.controllers.associations.AssociationsRouter
import me.nathanfallet.suitebde.controllers.associations.IAssociationForCallRouter
import me.nathanfallet.suitebde.controllers.models.AdminChildModelRouter
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.suitebde.models.web.WebPagePayload
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.suitebde.usecases.web.IGetPublicMenuForCallUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase

class WebPagesRouter(
    webPagesController: IWebPagesController,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    getPublicMenuForCallUseCase: IGetPublicMenuForCallUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
    associationForCallRouter: IAssociationForCallRouter,
    associationsRouter: AssociationsRouter,
) : ConcatChildModelRouter<WebPage, String, WebPagePayload, WebPagePayload, Association, String>(
    listOf(
        APIChildModelRouter(
            typeInfo<WebPage>(),
            typeInfo<WebPagePayload>(),
            typeInfo<WebPagePayload>(),
            typeInfo<List<WebPage>>(),
            webPagesController,
            associationsRouter,
            prefix = "/api/v1"
        ),
        AdminChildModelRouter(
            typeInfo<WebPage>(),
            typeInfo<WebPagePayload>(),
            typeInfo<WebPagePayload>(),
            typeInfo<List<WebPage>>(),
            webPagesController,
            associationForCallRouter,
            getLocaleForCallUseCase,
            translateUseCase,
            getAdminMenuForCallUseCase
        ),
        WebPagesPublicRouter(
            webPagesController,
            associationForCallRouter,
            getPublicMenuForCallUseCase,
            getLocaleForCallUseCase,
            TemplateMapping(
                errorTemplate = "root/error.ftl",
                getTemplate = "public/web/page.ftl",
            ),
            { template, model ->
                respondTemplate(
                    template, model + mapOf(
                        "title" to (model["item"] as? WebPage)?.title
                    )
                )
            },
            route = "pages"
        )
    ),
    associationsRouter
)
