package me.nathanfallet.suitebde.controllers.web

import io.ktor.server.freemarker.*
import me.nathanfallet.ktorx.models.templates.TemplateMapping
import me.nathanfallet.ktorx.routers.api.APIChildModelRouter
import me.nathanfallet.ktorx.routers.concat.ConcatChildModelRouter
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.controllers.associations.IAssociationForCallRouter
import me.nathanfallet.suitebde.controllers.models.AdminChildModelRouter
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.web.CreateWebPagePayload
import me.nathanfallet.suitebde.models.web.UpdateWebPagePayload
import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.suitebde.usecases.web.IGetPublicMenuForCallUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase

class WebPagesRouter(
    webPagesController: IWebPagesController,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    getPublicMenuForCallUseCase: IGetPublicMenuForCallUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
    associationsRouter: IAssociationForCallRouter
) : ConcatChildModelRouter<WebPage, String, CreateWebPagePayload, UpdateWebPagePayload, Association, String>(
    listOf(
        APIChildModelRouter(
            WebPage::class,
            CreateWebPagePayload::class,
            UpdateWebPagePayload::class,
            webPagesController,
            associationsRouter,
            prefix = "/api/v1"
        ),
        AdminChildModelRouter(
            WebPage::class,
            CreateWebPagePayload::class,
            UpdateWebPagePayload::class,
            webPagesController,
            associationsRouter,
            getLocaleForCallUseCase,
            translateUseCase,
            getAdminMenuForCallUseCase
        ),
        WebPagesPublicRouter(
            webPagesController,
            associationsRouter,
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
