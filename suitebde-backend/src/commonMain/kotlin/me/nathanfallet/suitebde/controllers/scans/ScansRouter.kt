package me.nathanfallet.suitebde.controllers.scans

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.routers.api.APIChildModelRouter
import me.nathanfallet.suitebde.controllers.associations.AssociationsRouter
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.scans.CreateScanPayload
import me.nathanfallet.suitebde.models.scans.Scan

class ScansRouter(
    controller: IScansController,
    associationsRouter: AssociationsRouter,
) : APIChildModelRouter<Scan, String, CreateScanPayload, Unit, Association, String>(
    typeInfo<Scan>(),
    typeInfo<CreateScanPayload>(),
    typeInfo<Unit>(),
    controller,
    IScansController::class,
    associationsRouter,
    prefix = "/api/v1"
)
