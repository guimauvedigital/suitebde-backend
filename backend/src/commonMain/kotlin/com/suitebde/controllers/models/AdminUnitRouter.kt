package com.suitebde.controllers.models

import com.suitebde.usecases.associations.IGetAssociationForCallUseCase
import com.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import dev.kaccelero.commons.localization.IGetLocaleForCallUseCase
import dev.kaccelero.commons.localization.ITranslateUseCase
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.controllers.IUnitController
import dev.kaccelero.models.UnitModel
import dev.kaccelero.routers.IUnitRouter
import io.ktor.util.reflect.*
import kotlin.reflect.KClass

open class AdminUnitRouter(
    controller: IUnitController,
    controllerClass: KClass<out IUnitController>,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    requireUserForCallUseCase: IRequireUserForCallUseCase,
    getAssociationForCallUseCase: IGetAssociationForCallUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
    route: String? = null,
) : AdminModelRouter<UnitModel, Unit, Unit, Unit>(
    typeInfo<UnitModel>(),
    typeInfo<Unit>(),
    typeInfo<Unit>(),
    controller,
    controllerClass,
    getLocaleForCallUseCase,
    translateUseCase,
    requireUserForCallUseCase,
    getAssociationForCallUseCase,
    getAdminMenuForCallUseCase,
    route ?: "",
), IUnitRouter
