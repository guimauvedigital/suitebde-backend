package com.suitebde.controllers.clubs

import com.suitebde.controllers.models.AdminChildModelRouter
import com.suitebde.models.associations.Association
import com.suitebde.models.clubs.*
import com.suitebde.usecases.associations.IGetAssociationForCallUseCase
import com.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import dev.kaccelero.commons.localization.IGetLocaleForCallUseCase
import dev.kaccelero.commons.localization.ITranslateUseCase
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import dev.kaccelero.routers.APIChildModelRouter
import dev.kaccelero.routers.ConcatChildModelRouter
import io.ktor.util.reflect.*

class UsersInClubsRouter(
    controller: IUsersInClubsController,
    clubsRouter: ClubsRouter,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    requireUserForCallUseCase: IRequireUserForCallUseCase,
    getAssociationForCallUseCase: IGetAssociationForCallUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
) : ConcatChildModelRouter<UserInClub, UUID, CreateUserInClubPayload, Unit, Club, UUID>(
    APIChildModelRouter(
        typeInfo<UserInClub>(),
        typeInfo<CreateUserInClubPayload>(),
        typeInfo<Unit>(),
        controller,
        IUsersInClubsController::class,
        clubsRouter,
        route = "users",
        prefix = "/api/v1"
    ),
    AdminChildModelRouter(
        typeInfo<UserInClub>(),
        typeInfo<CreateUserInClubPayload>(),
        typeInfo<Unit>(),
        controller,
        IUsersInClubsController::class,
        clubsRouter.routerOf<AdminChildModelRouter<Club, UUID, CreateClubPayload, UpdateClubPayload, Association, UUID>>(),
        getLocaleForCallUseCase,
        translateUseCase,
        requireUserForCallUseCase,
        getAssociationForCallUseCase,
        getAdminMenuForCallUseCase,
        route = "users",
    ),
)
