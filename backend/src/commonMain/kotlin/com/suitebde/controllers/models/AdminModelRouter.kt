package com.suitebde.controllers.models

import com.suitebde.usecases.associations.IGetAssociationForCallUseCase
import com.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import dev.kaccelero.commons.localization.IGetLocaleForCallUseCase
import dev.kaccelero.commons.localization.ITranslateUseCase
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.controllers.IModelController
import dev.kaccelero.models.IModel
import dev.kaccelero.models.UnitModel
import dev.kaccelero.routers.IModelRouter
import io.ktor.util.reflect.*
import kotlin.reflect.KClass

open class AdminModelRouter<Model : IModel<Id, CreatePayload, UpdatePayload>, Id, CreatePayload : Any, UpdatePayload : Any>(
    modelTypeInfo: TypeInfo,
    createPayloadTypeInfo: TypeInfo,
    updatePayloadTypeInfo: TypeInfo,
    controller: IModelController<Model, Id, CreatePayload, UpdatePayload>,
    controllerClass: KClass<out IModelController<Model, Id, CreatePayload, UpdatePayload>>,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    requireUserForCallUseCase: IRequireUserForCallUseCase,
    getAssociationForCallUseCase: IGetAssociationForCallUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
    route: String? = null,
    id: String? = null,
) : AdminChildModelRouter<Model, Id, CreatePayload, UpdatePayload, UnitModel, Unit>(
    modelTypeInfo,
    createPayloadTypeInfo,
    updatePayloadTypeInfo,
    controller,
    controllerClass,
    null,
    getLocaleForCallUseCase,
    translateUseCase,
    requireUserForCallUseCase,
    getAssociationForCallUseCase,
    getAdminMenuForCallUseCase,
    route,
    id,
), IModelRouter<Model, Id, CreatePayload, UpdatePayload>
