package me.nathanfallet.suitebde.controllers.web

import me.nathanfallet.ktor.routers.controllers.base.IChildModelController
import me.nathanfallet.ktor.routers.routers.api.APIChildModelRouter
import me.nathanfallet.ktor.routers.routers.base.ConcatChildModelRouter
import me.nathanfallet.suitebde.controllers.associations.AssociationsRouter
import me.nathanfallet.suitebde.controllers.models.AdminChildModelRouter
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.web.CreateWebPagePayload
import me.nathanfallet.suitebde.models.web.UpdateWebPagePayload
import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.suitebde.usecases.application.ITranslateUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase

class WebPagesRouter(
    webPagesController: IChildModelController<WebPage, String, CreateWebPagePayload, UpdateWebPagePayload, Association, String>,
    translateUseCase: ITranslateUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
    associationsRouter: AssociationsRouter
) : ConcatChildModelRouter<WebPage, String, CreateWebPagePayload, UpdateWebPagePayload, Association, String>(
    listOf(
        APIChildModelRouter(
            WebPage::class,
            CreateWebPagePayload::class,
            UpdateWebPagePayload::class,
            webPagesController,
            associationsRouter.routerOf(),
            prefix = "/api/v1"
        ),
        AdminChildModelRouter(
            WebPage::class,
            CreateWebPagePayload::class,
            UpdateWebPagePayload::class,
            webPagesController,
            associationsRouter.routerOf(),
            translateUseCase,
            getAdminMenuForCallUseCase
        )
    ),
    associationsRouter
)
