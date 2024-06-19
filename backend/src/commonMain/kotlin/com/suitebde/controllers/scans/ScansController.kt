package com.suitebde.controllers.scans

import com.suitebde.models.associations.Association
import com.suitebde.models.roles.Permission
import com.suitebde.models.scans.CreateScanPayload
import com.suitebde.models.scans.Scan
import com.suitebde.models.scans.ScansForDay
import com.suitebde.models.users.User
import com.suitebde.models.users.UserContext
import com.suitebde.usecases.scans.IListScansForDaysUseCase
import com.suitebde.usecases.users.IGetUserUseCase
import dev.kaccelero.commons.exceptions.ControllerException
import dev.kaccelero.commons.permissions.ICheckPermissionSuspendUseCase
import dev.kaccelero.commons.repositories.ICreateChildModelWithContextSuspendUseCase
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import io.ktor.http.*
import io.ktor.server.application.*
import kotlinx.datetime.LocalDate

class ScansController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getUserUseCase: IGetUserUseCase,
    private val createScanUseCase: ICreateChildModelWithContextSuspendUseCase<Scan, CreateScanPayload, UUID>,
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
