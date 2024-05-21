package me.nathanfallet.suitebde.controllers.scans

import io.ktor.server.application.*
import me.nathanfallet.ktorx.controllers.IChildModelController
import me.nathanfallet.ktorx.models.annotations.*
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.scans.CreateScanPayload
import me.nathanfallet.suitebde.models.scans.Scan

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

}
