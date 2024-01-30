package me.nathanfallet.suitebde.controllers.clubs

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.routers.api.APIChildModelRouter
import me.nathanfallet.ktorx.routers.concat.ConcatChildModelRouter
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.controllers.associations.AssociationsRouter
import me.nathanfallet.suitebde.controllers.associations.IAssociationForCallRouter
import me.nathanfallet.suitebde.controllers.models.AdminChildModelRouter
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.clubs.CreateClubPayload
import me.nathanfallet.suitebde.models.clubs.UpdateClubPayload
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase

class ClubsRouter(
    clubsController: IClubsController,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
    associationForCallRouter: IAssociationForCallRouter,
    associationsRouter: AssociationsRouter,
) : ConcatChildModelRouter<Club, String, CreateClubPayload, UpdateClubPayload, Association, String>(
    APIChildModelRouter(
        typeInfo<Club>(),
        typeInfo<CreateClubPayload>(),
        typeInfo<UpdateClubPayload>(),
        clubsController,
        IClubsController::class,
        associationsRouter,
        prefix = "/api/v1"
    ),
    AdminChildModelRouter(
        typeInfo<Club>(),
        typeInfo<CreateClubPayload>(),
        typeInfo<UpdateClubPayload>(),
        clubsController,
        IClubsController::class,
        associationForCallRouter,
        getLocaleForCallUseCase,
        translateUseCase,
        getAdminMenuForCallUseCase
    )
)
