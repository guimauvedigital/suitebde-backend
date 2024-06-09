package me.nathanfallet.suitebde.controllers.clubs

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.users.IGetUserForCallUseCase
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.models.application.SearchOptions
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.clubs.*
import me.nathanfallet.suitebde.models.notifications.CreateClubNotificationPayload
import me.nathanfallet.suitebde.models.notifications.CreatePermissionNotificationPayload
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.OptionalUserContext
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.notifications.ISendNotificationToClubUseCase
import me.nathanfallet.suitebde.usecases.notifications.ISendNotificationToPermissionUseCase
import me.nathanfallet.usecases.models.create.context.ICreateChildModelWithContextSuspendUseCase
import me.nathanfallet.usecases.models.delete.IDeleteChildModelSuspendUseCase
import me.nathanfallet.usecases.models.get.context.IGetChildModelWithContextSuspendUseCase
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.context.IListChildModelWithContextSuspendUseCase
import me.nathanfallet.usecases.models.list.slice.context.IListSliceChildModelWithContextSuspendUseCase
import me.nathanfallet.usecases.models.update.context.IUpdateChildModelWithContextSuspendUseCase
import me.nathanfallet.usecases.pagination.Pagination
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase

class ClubsController(
    private val getUserForCallUseCase: IGetUserForCallUseCase,
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getClubsInAssociationUseCase: IListChildModelWithContextSuspendUseCase<Club, String>,
    private val getClubsInAssociationSlicedUseCase: IListSliceChildModelWithContextSuspendUseCase<Club, String>,
    private val getClubUseCase: IGetChildModelWithContextSuspendUseCase<Club, String, String>,
    private val createClubUseCase: ICreateChildModelWithContextSuspendUseCase<Club, CreateClubPayload, String>,
    private val updateClubUseCase: IUpdateChildModelWithContextSuspendUseCase<Club, String, UpdateClubPayload, String>,
    private val deleteClubUseCase: IDeleteChildModelSuspendUseCase<Club, String, String>,
    private val listUsersInClubsUseCase: IListChildModelSuspendUseCase<UserInClub, String>,
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
                data = mapOf("clubId" to club.id)
            )
        )
        return club
    }

    override suspend fun delete(call: ApplicationCall, parent: Association, id: String) {
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
                data = mapOf("clubId" to club.id)
            )
        )
    }

    override suspend fun get(call: ApplicationCall, parent: Association, id: String): Club {
        val user = (getUserForCallUseCase(call) as? User)
        return getClubUseCase(id, parent.id, OptionalUserContext(user?.id))
            ?: throw ControllerException(HttpStatusCode.NotFound, "clubs_not_found")
    }

    override suspend fun details(call: ApplicationCall, parent: Association, id: String): Map<String, Any> {
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
        id: String,
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
                data = mapOf("clubId" to club.id)
            )
        )
        return updatedClub
    }

}
