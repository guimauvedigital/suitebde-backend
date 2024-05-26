package me.nathanfallet.suitebde.controllers.clubs

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.clubs.CreateUserInClubPayload
import me.nathanfallet.suitebde.models.clubs.UserInClub
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import me.nathanfallet.usecases.models.delete.IDeleteChildModelSuspendUseCase
import me.nathanfallet.usecases.models.get.IGetChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.slice.IListSliceChildModelSuspendUseCase
import me.nathanfallet.usecases.pagination.Pagination
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase

class UsersInClubsController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getUsersInClubsUseCase: IListChildModelSuspendUseCase<UserInClub, String>,
    private val getUsersInClubsSlicedUseCase: IListSliceChildModelSuspendUseCase<UserInClub, String>,
    private val createUserInClubsUseCase: ICreateChildModelSuspendUseCase<UserInClub, CreateUserInClubPayload, String>,
    private val getUserInClubUseCase: IGetChildModelSuspendUseCase<UserInClub, String, String>,
    private val deleteUserInClubUseCase: IDeleteChildModelSuspendUseCase<UserInClub, String, String>,
) : IUsersInClubsController {

    override suspend fun create(call: ApplicationCall, parent: Club, payload: CreateUserInClubPayload): UserInClub {
        (requireUserForCallUseCase(call) as? User)?.takeIf {
            it.id == payload.userId ||
                    checkPermissionUseCase(it, Permission.CLUBS_USERS inAssociation parent.associationId)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "users_in_clubs_not_allowed"
        )
        return createUserInClubsUseCase(payload, parent.id) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

    override suspend fun delete(call: ApplicationCall, parent: Club, id: String) {
        (requireUserForCallUseCase(call) as? User)?.takeIf {
            it.id == id || checkPermissionUseCase(it, Permission.CLUBS_USERS inAssociation parent.associationId)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "users_in_clubs_not_allowed"
        )
        val userInClub = getUserInClubUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "users_in_clubs_not_found"
        )
        if (!deleteUserInClubUseCase(userInClub.id, parent.id)) throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

    override suspend fun get(call: ApplicationCall, parent: Club, id: String): UserInClub {
        return getUserInClubUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "users_in_clubs_not_found"
        )
    }

    override suspend fun list(call: ApplicationCall, parent: Club, limit: Long?, offset: Long?): List<UserInClub> {
        if (call.request.path().contains("/admin/")) return getUsersInClubsUseCase(parent.id)
        return getUsersInClubsSlicedUseCase(
            Pagination(
                limit ?: 25,
                offset ?: 0,
            ),
            parent.id
        )
    }
}
