package com.suitebde.controllers.files

import com.suitebde.controllers.models.AdminModelRouter
import com.suitebde.models.associations.Association
import com.suitebde.models.associations.CreateAssociationPayload
import com.suitebde.models.associations.UpdateAssociationPayload
import com.suitebde.usecases.associations.IGetAssociationForCallUseCase
import com.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import dev.kaccelero.commons.localization.IGetLocaleForCallUseCase
import dev.kaccelero.commons.localization.ITranslateUseCase
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import io.ktor.util.reflect.*

class FilesRouter(
    controller: IFilesController,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    requireUserForCallUseCase: IRequireUserForCallUseCase,
    getAssociationForCallUseCase: IGetAssociationForCallUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
) : AdminModelRouter<Association, UUID, CreateAssociationPayload, UpdateAssociationPayload>(
    typeInfo<Association>(),
    typeInfo<CreateAssociationPayload>(),
    typeInfo<UpdateAssociationPayload>(),
    controller,
    IFilesController::class,
    getLocaleForCallUseCase,
    translateUseCase,
    requireUserForCallUseCase,
    getAssociationForCallUseCase,
    getAdminMenuForCallUseCase,
    route = ""
)

