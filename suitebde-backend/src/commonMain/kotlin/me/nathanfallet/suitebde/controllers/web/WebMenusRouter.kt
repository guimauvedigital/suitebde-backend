package me.nathanfallet.suitebde.controllers.web

import me.nathanfallet.ktorx.controllers.IChildModelController
import me.nathanfallet.ktorx.routers.api.APIChildModelRouter
import me.nathanfallet.ktorx.routers.concat.ConcatChildModelRouter
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.controllers.associations.IAssociationForCallRouter
import me.nathanfallet.suitebde.controllers.models.AdminChildModelRouter
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.web.CreateWebMenuPayload
import me.nathanfallet.suitebde.models.web.UpdateWebMenuPayload
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase

class WebMenusRouter(
    webMenusController: IChildModelController<WebMenu, String, CreateWebMenuPayload, UpdateWebMenuPayload, Association, String>,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
    associationsRouter: IAssociationForCallRouter
) : ConcatChildModelRouter<WebMenu, String, CreateWebMenuPayload, UpdateWebMenuPayload, Association, String>(
    listOf(
        APIChildModelRouter(
            WebMenu::class,
            CreateWebMenuPayload::class,
            UpdateWebMenuPayload::class,
            webMenusController,
            associationsRouter,
            prefix = "/api/v1"
        ),
        AdminChildModelRouter(
            WebMenu::class,
            CreateWebMenuPayload::class,
            UpdateWebMenuPayload::class,
            webMenusController,
            associationsRouter,
            getLocaleForCallUseCase,
            translateUseCase,
            getAdminMenuForCallUseCase
        ),
    ),
    associationsRouter
)
