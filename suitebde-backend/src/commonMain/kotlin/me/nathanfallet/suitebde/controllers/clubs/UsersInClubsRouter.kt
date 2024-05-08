package me.nathanfallet.suitebde.controllers.clubs

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.routers.api.APIChildModelRouter
import me.nathanfallet.ktorx.routers.concat.ConcatChildModelRouter
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.controllers.models.AdminChildModelRouter
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.clubs.*
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForCallUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase

class UsersInClubsRouter(
    controller: IUsersInClubsController,
    clubsRouter: ClubsRouter,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    requireUserForCallUseCase: IRequireUserForCallUseCase,
    getAssociationForCallUseCase: IGetAssociationForCallUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
) : ConcatChildModelRouter<UserInClub, String, CreateUserInClubPayload, Unit, Club, String>(
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
        clubsRouter.routerOf<AdminChildModelRouter<Club, String, CreateClubPayload, UpdateClubPayload, Association, String>>(),
        getLocaleForCallUseCase,
        translateUseCase,
        requireUserForCallUseCase,
        getAssociationForCallUseCase,
        getAdminMenuForCallUseCase,
        route = "users",
    ),
)
