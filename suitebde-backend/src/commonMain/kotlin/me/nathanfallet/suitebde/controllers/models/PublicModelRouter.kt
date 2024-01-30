package me.nathanfallet.suitebde.controllers.models

import io.ktor.server.application.*
import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.controllers.IModelController
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.usecases.web.IGetPublicMenuForCallUseCase
import me.nathanfallet.usecases.models.IModel
import me.nathanfallet.usecases.models.UnitModel
import kotlin.reflect.KClass

class PublicModelRouter<Model : IModel<Id, CreatePayload, UpdatePayload>, Id, CreatePayload : Any, UpdatePayload : Any>(
    modelTypeInfo: TypeInfo,
    createPayloadTypeInfo: TypeInfo,
    updatePayloadTypeInfo: TypeInfo,
    controller: IModelController<Model, Id, CreatePayload, UpdatePayload>,
    controllerClass: KClass<out IModelController<Model, Id, CreatePayload, UpdatePayload>>,
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
    getPublicMenuForCallUseCase,
    getLocaleForCallUseCase,
    respondTemplate,
    route,
    id,
    prefix
)
