package me.nathanfallet.suitebde.controllers.associations

import me.nathanfallet.ktorx.controllers.base.IChildModelController
import me.nathanfallet.ktorx.routers.api.APIChildModelRouter
import me.nathanfallet.ktorx.routers.concat.ConcatChildModelRouter
import me.nathanfallet.suitebde.controllers.models.AdminChildModelRouter
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateDomainInAssociationPayload
import me.nathanfallet.suitebde.models.associations.DomainInAssociation
import me.nathanfallet.suitebde.usecases.application.ITranslateUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase

class DomainsInAssociationsRouter(
    domainsInAssociationsController: IChildModelController<DomainInAssociation, String, CreateDomainInAssociationPayload, Unit, Association, String>,
    translateUseCase: ITranslateUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
    associationsRouter: AssociationsRouter
) : ConcatChildModelRouter<DomainInAssociation, String, CreateDomainInAssociationPayload, Unit, Association, String>(
    listOf(
        APIChildModelRouter(
            DomainInAssociation::class,
            CreateDomainInAssociationPayload::class,
            Unit::class,
            domainsInAssociationsController,
            associationsRouter.routerOf(),
            route = "domains",
            prefix = "/api/v1"
        ),
        AdminChildModelRouter(
            DomainInAssociation::class,
            CreateDomainInAssociationPayload::class,
            Unit::class,
            domainsInAssociationsController,
            associationsRouter.routerOf(),
            translateUseCase,
            getAdminMenuForCallUseCase,
            route = "domains"
        )
    ),
    associationsRouter
)
