package me.nathanfallet.suitebde.controllers.web

import com.github.aymanizz.ktori18n.locale
import io.ktor.server.freemarker.*
import me.nathanfallet.ktorx.models.templates.TemplateMapping
import me.nathanfallet.ktorx.routers.api.APIChildModelRouter
import me.nathanfallet.ktorx.routers.concat.ConcatChildModelRouter
import me.nathanfallet.suitebde.controllers.associations.IAssociationForCallRouter
import me.nathanfallet.suitebde.controllers.models.AdminChildModelRouter
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.web.CreateWebPagePayload
import me.nathanfallet.suitebde.models.web.UpdateWebPagePayload
import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.suitebde.usecases.application.ITranslateUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase

class WebPagesRouter(
    webPagesController: IWebPagesController,
    translateUseCase: ITranslateUseCase,
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
            translateUseCase,
            getAdminMenuForCallUseCase
        ),
        WebPagesPublicRouter(
            webPagesController,
            associationsRouter,
            TemplateMapping(
                errorTemplate = "root/error.ftl",
                getTemplate = "public/web/page.ftl",
            ),
            { template, model ->
                respondTemplate(
                    template, model + mapOf(
                        "locale" to locale,
                        "title" to (model["item"] as? WebPage)?.title
                    )
                )
            },
            route = "pages"
        )
    ),
    associationsRouter
)
