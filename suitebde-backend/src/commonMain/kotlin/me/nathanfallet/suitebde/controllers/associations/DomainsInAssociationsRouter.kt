package me.nathanfallet.suitebde.controllers.associations

import me.nathanfallet.ktorx.controllers.IChildModelController
import me.nathanfallet.ktorx.models.api.APIMapping
import me.nathanfallet.ktorx.routers.api.APIChildModelRouter
import me.nathanfallet.ktorx.routers.concat.ConcatChildModelRouter
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.controllers.models.AdminChildModelRouter
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateDomainInAssociationPayload
import me.nathanfallet.suitebde.models.associations.DomainInAssociation
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase

class DomainsInAssociationsRouter(
    domainsInAssociationsController: IChildModelController<DomainInAssociation, String, CreateDomainInAssociationPayload, Unit, Association, String>,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
    associationsRouter: AssociationsRouter,
) : ConcatChildModelRouter<DomainInAssociation, String, CreateDomainInAssociationPayload, Unit, Association, String>(
    listOf(
        APIChildModelRouter(
            DomainInAssociation::class,
            CreateDomainInAssociationPayload::class,
            Unit::class,
            domainsInAssociationsController,
            associationsRouter.routerOf(),
            mapping = APIMapping(
                updateEnabled = false
            ),
            route = "domains",
            prefix = "/api/v1"
        ),
        AdminChildModelRouter(
            DomainInAssociation::class,
            CreateDomainInAssociationPayload::class,
            Unit::class,
            domainsInAssociationsController,
            associationsRouter.routerOf(),
            getLocaleForCallUseCase,
            translateUseCase,
            getAdminMenuForCallUseCase,
            route = "domains"
        )
    ),
    associationsRouter
)
