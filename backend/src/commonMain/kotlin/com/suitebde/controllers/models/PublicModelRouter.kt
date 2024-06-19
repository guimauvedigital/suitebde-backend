package com.suitebde.controllers.models

import com.suitebde.usecases.web.IGetPublicMenuForCallUseCase
import dev.kaccelero.commons.localization.IGetLocaleForCallUseCase
import dev.kaccelero.commons.users.IGetUserForCallUseCase
import dev.kaccelero.controllers.IModelController
import dev.kaccelero.models.IModel
import dev.kaccelero.models.UnitModel
import io.ktor.server.application.*
import io.ktor.util.reflect.*
import kotlin.reflect.KClass

class PublicModelRouter<Model : IModel<Id, CreatePayload, UpdatePayload>, Id, CreatePayload : Any, UpdatePayload : Any>(
    modelTypeInfo: TypeInfo,
    createPayloadTypeInfo: TypeInfo,
    updatePayloadTypeInfo: TypeInfo,
    controller: IModelController<Model, Id, CreatePayload, UpdatePayload>,
    controllerClass: KClass<out IModelController<Model, Id, CreatePayload, UpdatePayload>>,
    getUserForCallUseCase: IGetUserForCallUseCase,
    getPublicMenuForCallUseCase: IGetPublicMenuForCallUseCase,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    respondTemplate: (suspend ApplicationCall.(String, Map<String, Any?>) -> Unit)? = null,
    route: String? = null,
    id: String? = null,
    prefix: String? = null,
) : PublicChildModelRouter<Model, Id, CreatePayload, UpdatePayload, UnitModel, Unit>(
    modelTypeInfo,
    createPayloadTypeInfo,
    updatePayloadTypeInfo,
    controller,
    controllerClass,
    null,
    getUserForCallUseCase,
    getPublicMenuForCallUseCase,
    getLocaleForCallUseCase,
    respondTemplate,
    route,
    id,
    prefix
)
