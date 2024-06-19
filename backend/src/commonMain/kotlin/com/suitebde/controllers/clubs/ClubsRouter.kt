package com.suitebde.controllers.clubs

import com.suitebde.controllers.associations.AssociationsRouter
import com.suitebde.controllers.associations.IAssociationForCallRouter
import com.suitebde.controllers.models.AdminChildModelRouter
import com.suitebde.controllers.models.PublicChildModelRouter
import com.suitebde.models.associations.Association
import com.suitebde.models.clubs.Club
import com.suitebde.models.clubs.CreateClubPayload
import com.suitebde.models.clubs.UpdateClubPayload
import com.suitebde.usecases.associations.IGetAssociationForCallUseCase
import com.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import com.suitebde.usecases.web.IGetPublicMenuForCallUseCase
import dev.kaccelero.commons.localization.IGetLocaleForCallUseCase
import dev.kaccelero.commons.localization.ITranslateUseCase
import dev.kaccelero.commons.users.IGetUserForCallUseCase
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import dev.kaccelero.routers.APIChildModelRouter
import dev.kaccelero.routers.ConcatChildModelRouter
import io.ktor.server.freemarker.*
import io.ktor.util.reflect.*

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
) : ConcatChildModelRouter<Club, UUID, CreateClubPayload, UpdateClubPayload, Association, UUID>(
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
