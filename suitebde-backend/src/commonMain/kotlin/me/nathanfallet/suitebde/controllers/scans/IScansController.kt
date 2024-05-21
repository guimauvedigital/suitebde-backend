package me.nathanfallet.suitebde.controllers.scans

import io.ktor.server.application.*
import kotlinx.datetime.LocalDate
import me.nathanfallet.ktorx.controllers.IChildModelController
import me.nathanfallet.ktorx.models.annotations.*
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.scans.CreateScanPayload
import me.nathanfallet.suitebde.models.scans.Scan
import me.nathanfallet.suitebde.models.scans.ScansForDay

interface IScansController : IChildModelController<Scan, String, CreateScanPayload, Unit, Association, String> {

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
