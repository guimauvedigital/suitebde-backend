package com.suitebde.controllers.scans

import com.suitebde.models.associations.Association
import com.suitebde.models.scans.CreateScanPayload
import com.suitebde.models.scans.Scan
import com.suitebde.models.scans.ScansForDay
import dev.kaccelero.annotations.*
import dev.kaccelero.controllers.IChildModelController
import dev.kaccelero.models.UUID
import io.ktor.server.application.*
import kotlinx.datetime.LocalDate

interface IScansController : IChildModelController<Scan, UUID, CreateScanPayload, Unit, Association, UUID> {

    @APIMapping
    @AdminTemplateMapping
    @CreateModelPath
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "scans_not_allowed")
    @DocumentedError(404, "users_not_found")
    @DocumentedError(500, "error_internal")
    suspend fun create(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @Payload payload: CreateScanPayload,
    ): Scan

    @APIMapping("listScan", "List scans between two dates")
    @Path("GET", "/")
    @DocumentedError(400, "scans_invalid_dates")
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "scans_not_allowed")
    suspend fun list(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @QueryParameter startsAt: LocalDate?,
        @QueryParameter endsAt: LocalDate?,
    ): List<ScansForDay>

}
