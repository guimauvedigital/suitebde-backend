package me.nathanfallet.suitebde.controllers.web

import io.ktor.server.freemarker.*
import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.routers.api.APIChildModelRouter
import me.nathanfallet.ktorx.routers.concat.ConcatChildModelRouter
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.ktorx.usecases.users.IGetUserForCallUseCase
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
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
    getUserForCallUseCase: IGetUserForCallUseCase,
    requireUserForCallUseCase: IRequireUserForCallUseCase,
    getPublicMenuForCallUseCase: IGetPublicMenuForCallUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
    associationForCallRouter: IAssociationForCallRouter,
    associationsRouter: AssociationsRouter,
) : ConcatChildModelRouter<WebPage, String, WebPagePayload, WebPagePayload, Association, String>(
    APIChildModelRouter(
        typeInfo<WebPage>(),
        typeInfo<WebPagePayload>(),
        typeInfo<WebPagePayload>(),
        webPagesController,
        IWebPagesController::class,
        associationsRouter,
        prefix = "/api/v1"
    ),
    AdminChildModelRouter(
        typeInfo<WebPage>(),
        typeInfo<WebPagePayload>(),
        typeInfo<WebPagePayload>(),
        webPagesController,
        IWebPagesController::class,
        associationForCallRouter,
        getLocaleForCallUseCase,
        translateUseCase,
        requireUserForCallUseCase,
        getAdminMenuForCallUseCase
    ),
    WebPagesPublicRouter(
        webPagesController,
        associationForCallRouter,
        getUserForCallUseCase,
        getPublicMenuForCallUseCase,
        getLocaleForCallUseCase
    ) { template, model ->
        respondTemplate(
            template, model + mapOf(
                "title" to (model["item"] as? WebPage)?.title
            )
        )
    }
)
