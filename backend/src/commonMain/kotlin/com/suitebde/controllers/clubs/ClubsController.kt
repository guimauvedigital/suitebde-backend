package com.suitebde.controllers.clubs

import com.suitebde.models.application.SearchOptions
import com.suitebde.models.associations.Association
import com.suitebde.models.clubs.*
import com.suitebde.models.notifications.CreateClubNotificationPayload
import com.suitebde.models.notifications.CreatePermissionNotificationPayload
import com.suitebde.models.roles.Permission
import com.suitebde.models.users.OptionalUserContext
import com.suitebde.models.users.User
import com.suitebde.usecases.notifications.ISendNotificationToClubUseCase
import com.suitebde.usecases.notifications.ISendNotificationToPermissionUseCase
import dev.kaccelero.commons.exceptions.ControllerException
import dev.kaccelero.commons.permissions.ICheckPermissionSuspendUseCase
import dev.kaccelero.commons.repositories.*
import dev.kaccelero.commons.users.IGetUserForCallUseCase
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*

class ClubsController(
    private val getUserForCallUseCase: IGetUserForCallUseCase,
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getClubsInAssociationUseCase: IListChildModelWithContextSuspendUseCase<Club, UUID>,
    private val getClubsInAssociationSlicedUseCase: IListSliceChildModelWithContextSuspendUseCase<Club, UUID>,
    private val getClubUseCase: IGetChildModelWithContextSuspendUseCase<Club, UUID, UUID>,
    private val createClubUseCase: ICreateChildModelWithContextSuspendUseCase<Club, CreateClubPayload, UUID>,
    private val updateClubUseCase: IUpdateChildModelWithContextSuspendUseCase<Club, UUID, UpdateClubPayload, UUID>,
    private val deleteClubUseCase: IDeleteChildModelSuspendUseCase<Club, UUID, UUID>,
    private val listUsersInClubsUseCase: IListChildModelSuspendUseCase<UserInClub, UUID>,
    private val sendNotificationToClubUseCase: ISendNotificationToClubUseCase,
    private val sendNotificationToPermissionUseCase: ISendNotificationToPermissionUseCase,
) : IClubsController {

    override suspend fun create(call: ApplicationCall, parent: Association, payload: CreateClubPayload): Club {
        val user = requireUserForCallUseCase(call) as User
        payload.validated?.let {
            if (!checkPermissionUseCase(
                    user,
                    Permission.CLUBS_CREATE inAssociation parent.id
                )
            ) throw ControllerException(
                HttpStatusCode.Forbidden, "clubs_validated_not_allowed"
            )
        }
        val club = createClubUseCase(payload, parent.id, OptionalUserContext(user.id)) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
        if (!club.validated) sendNotificationToPermissionUseCase(
            CreatePermissionNotificationPayload(
                Permission.EVENTS_UPDATE,
                "notification_club_suggested_title",
                "notification_club_suggested_description",
                bodyArgs = listOf(club.name, user.firstName),
                data = mapOf("clubId" to club.id.toString())
            )
        )
        return club
    }

    override suspend fun delete(call: ApplicationCall, parent: Association, id: UUID) {
        val user = requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.CLUBS_DELETE inAssociation parent.id)
        } as? User ?: throw ControllerException(
            HttpStatusCode.Forbidden, "clubs_delete_not_allowed"
        )
        val club = getClubUseCase(id, parent.id, OptionalUserContext(user.id)) ?: throw ControllerException(
            HttpStatusCode.NotFound, "clubs_not_found"
        )
        if (!deleteClubUseCase(club.id, parent.id)) throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
        if (!club.validated) sendNotificationToClubUseCase(
            CreateClubNotificationPayload(
                club.id,
                "notification_club_rejected_title",
                "notification_club_rejected_description",
                bodyArgs = listOf(user.firstName, club.name),
                data = mapOf("clubId" to club.id.toString())
            )
        )
    }

    override suspend fun get(call: ApplicationCall, parent: Association, id: UUID): Club {
        val user = (getUserForCallUseCase(call) as? User)
        return getClubUseCase(id, parent.id, OptionalUserContext(user?.id))
            ?: throw ControllerException(HttpStatusCode.NotFound, "clubs_not_found")
    }

    override suspend fun details(call: ApplicationCall, parent: Association, id: UUID): Map<String, Any> {
        val club = get(call, parent, id)
        return mapOf(
            "item" to club,
            "users" to listUsersInClubsUseCase(club.id)
        )
    }

    override suspend fun list(
        call: ApplicationCall,
        parent: Association,
        limit: Long?,
        offset: Long?,
        search: String?,
    ): List<Club> {
        val user = (getUserForCallUseCase(call) as? User)
        if (!call.request.path().contains("/api/")) return getClubsInAssociationUseCase(
            parent.id,
            ClubContext(user?.id, !call.request.path().contains("/admin/"))
        )
        return getClubsInAssociationSlicedUseCase(
            Pagination(
                limit ?: 25,
                offset ?: 0,
                search?.let { SearchOptions(it) }
            ),
            parent.id,
            OptionalUserContext(user?.id)
        )
    }

    override suspend fun update(
        call: ApplicationCall,
        parent: Association,
        id: UUID,
        payload: UpdateClubPayload,
    ): Club {
        val user = requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.CLUBS_UPDATE inAssociation parent.id)
        } as? User ?: throw ControllerException(
            HttpStatusCode.Forbidden, "clubs_update_not_allowed"
        )
        val club = getClubUseCase(id, parent.id, OptionalUserContext(user.id)) ?: throw ControllerException(
            HttpStatusCode.NotFound, "clubs_not_found"
        )
        val updatedClub = updateClubUseCase(club.id, payload, parent.id, OptionalUserContext(user.id))
            ?: throw ControllerException(HttpStatusCode.InternalServerError, "error_internal")
        if (!club.validated && updatedClub.validated) sendNotificationToClubUseCase(
            CreateClubNotificationPayload(
                club.id,
                "notification_club_validated_title",
                "notification_club_validated_description",
                bodyArgs = listOf(user.firstName, club.name),
                data = mapOf("clubId" to club.id.toString())
            )
        )
        return updatedClub
    }

}
