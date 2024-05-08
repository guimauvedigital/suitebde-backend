package me.nathanfallet.suitebde.controllers.clubs

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
import me.nathanfallet.suitebde.controllers.models.PublicChildModelRouter
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.clubs.CreateClubPayload
import me.nathanfallet.suitebde.models.clubs.UpdateClubPayload
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForCallUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.suitebde.usecases.web.IGetPublicMenuForCallUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase

class ClubsRouter(
    controller: IClubsController,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    getUserForCallUseCase: IGetUserForCallUseCase,
    requireUserForCallUseCase: IRequireUserForCallUseCase,
    getAssociationForCallUseCase: IGetAssociationForCallUseCase,
    getPublicMenuForCallUseCase: IGetPublicMenuForCallUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
    associationForCallRouter: IAssociationForCallRouter,
    associationsRouter: AssociationsRouter,
) : ConcatChildModelRouter<Club, String, CreateClubPayload, UpdateClubPayload, Association, String>(
    APIChildModelRouter(
        typeInfo<Club>(),
        typeInfo<CreateClubPayload>(),
        typeInfo<UpdateClubPayload>(),
        controller,
        IClubsController::class,
        associationsRouter,
        prefix = "/api/v1"
    ),
    AdminChildModelRouter(
        typeInfo<Club>(),
        typeInfo<CreateClubPayload>(),
        typeInfo<UpdateClubPayload>(),
        controller,
        IClubsController::class,
        associationForCallRouter,
        getLocaleForCallUseCase,
        translateUseCase,
        requireUserForCallUseCase,
        getAssociationForCallUseCase,
        getAdminMenuForCallUseCase
    ),
    PublicChildModelRouter(
        typeInfo<Club>(),
        typeInfo<CreateClubPayload>(),
        typeInfo<UpdateClubPayload>(),
        controller,
        IClubsController::class,
        associationForCallRouter,
        getUserForCallUseCase,
        getPublicMenuForCallUseCase,
        getLocaleForCallUseCase,
        { template, model ->
            respondTemplate(
                template, model + mapOf(
                    "title" to (model["item"] as? Club)?.name
                )
            )
        }
    )
)
