package me.nathanfallet.suitebde.controllers.clubs

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.routers.api.APIChildModelRouter
import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.clubs.CreateUserInClubPayload
import me.nathanfallet.suitebde.models.clubs.UserInClub

class UsersInClubsRouter(
    clubsController: IUsersInClubsController,
    clubsRouter: ClubsRouter,
) : APIChildModelRouter<UserInClub, String, CreateUserInClubPayload, Unit, Club, String>(
    typeInfo<UserInClub>(),
    typeInfo<CreateUserInClubPayload>(),
    typeInfo<Unit>(),
    clubsController,
    IUsersInClubsController::class,
    clubsRouter,
    route = "users",
    prefix = "/api/v1"
)
