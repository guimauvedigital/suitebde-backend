package com.suitebde.controllers.clubs

import com.suitebde.models.clubs.Club
import com.suitebde.models.clubs.CreateUserInClubPayload
import com.suitebde.models.clubs.UserInClub
import com.suitebde.models.roles.Permission
import com.suitebde.models.users.User
import dev.kaccelero.commons.exceptions.ControllerException
import dev.kaccelero.commons.permissions.ICheckPermissionSuspendUseCase
import dev.kaccelero.commons.repositories.*
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*

class UsersInClubsController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getUsersInClubsUseCase: IListChildModelSuspendUseCase<UserInClub, UUID>,
    private val getUsersInClubsSlicedUseCase: IListSliceChildModelSuspendUseCase<UserInClub, UUID>,
    private val createUserInClubsUseCase: ICreateChildModelSuspendUseCase<UserInClub, CreateUserInClubPayload, UUID>,
    private val getUserInClubUseCase: IGetChildModelSuspendUseCase<UserInClub, UUID, UUID>,
    private val deleteUserInClubUseCase: IDeleteChildModelSuspendUseCase<UserInClub, UUID, UUID>,
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

    override suspend fun delete(call: ApplicationCall, parent: Club, id: UUID) {
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

    override suspend fun get(call: ApplicationCall, parent: Club, id: UUID): UserInClub {
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
