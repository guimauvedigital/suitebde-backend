package me.nathanfallet.suitebde.controllers.scans

import io.ktor.http.*
import io.ktor.server.application.*
import kotlinx.datetime.LocalDate
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.scans.CreateScanPayload
import me.nathanfallet.suitebde.models.scans.Scan
import me.nathanfallet.suitebde.models.scans.ScansForDay
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.models.users.UserContext
import me.nathanfallet.suitebde.usecases.scans.IListScansForDaysUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUserUseCase
import me.nathanfallet.usecases.models.create.context.ICreateChildModelWithContextSuspendUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase

class ScansController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getUserUseCase: IGetUserUseCase,
    private val createScanUseCase: ICreateChildModelWithContextSuspendUseCase<Scan, CreateScanPayload, String>,
    private val listScansForDaysUseCase: IListScansForDaysUseCase,
) : IScansController {

    override suspend fun create(call: ApplicationCall, parent: Association, payload: CreateScanPayload): Scan {
        val scanner = requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.USERS_VIEW inAssociation parent.id)
        } as? User ?: throw ControllerException(HttpStatusCode.Forbidden, "scans_not_allowed")
        val user = getUserUseCase(payload.userId) ?: throw ControllerException(
            HttpStatusCode.NotFound, "users_not_found"
        )
        return createScanUseCase(
            CreateScanPayload(user.id), parent.id, UserContext(scanner.id)
        ) ?: throw ControllerException(HttpStatusCode.InternalServerError, "error_internal")
    }

    override suspend fun list(
        call: ApplicationCall,
        parent: Association,
        startsAt: LocalDate?,
        endsAt: LocalDate?,
    ): List<ScansForDay> {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.USERS_VIEW inAssociation parent.id)
        } ?: throw ControllerException(HttpStatusCode.Forbidden, "scans_not_allowed")
        if (startsAt == null || endsAt == null || startsAt > endsAt) throw ControllerException(
            HttpStatusCode.BadRequest, "scans_invalid_dates"
        )
        return listScansForDaysUseCase(parent.id, startsAt, endsAt)
    }

}
