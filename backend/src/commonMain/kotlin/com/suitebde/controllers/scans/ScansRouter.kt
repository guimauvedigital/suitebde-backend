package com.suitebde.controllers.scans

import com.suitebde.controllers.associations.AssociationsRouter
import com.suitebde.models.associations.Association
import com.suitebde.models.scans.CreateScanPayload
import com.suitebde.models.scans.Scan
import dev.kaccelero.models.UUID
import dev.kaccelero.routers.APIChildModelRouter
import io.ktor.util.reflect.*

class ScansRouter(
    controller: IScansController,
    associationsRouter: AssociationsRouter,
) : APIChildModelRouter<Scan, UUID, CreateScanPayload, Unit, Association, UUID>(
    typeInfo<Scan>(),
    typeInfo<CreateScanPayload>(),
    typeInfo<Unit>(),
    controller,
    IScansController::class,
    associationsRouter,
    prefix = "/api/v1"
)
