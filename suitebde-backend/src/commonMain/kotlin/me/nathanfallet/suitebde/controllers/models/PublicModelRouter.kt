package me.nathanfallet.suitebde.controllers.models

import io.ktor.server.application.*
import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.controllers.IModelController
import me.nathanfallet.ktorx.models.templates.TemplateMapping
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.usecases.web.IGetPublicMenuForCallUseCase
import me.nathanfallet.usecases.models.IModel
import me.nathanfallet.usecases.models.UnitModel

class PublicModelRouter<Model : IModel<Id, CreatePayload, UpdatePayload>, Id, CreatePayload : Any, UpdatePayload : Any>(
    modelTypeInfo: TypeInfo,
    createPayloadTypeInfo: TypeInfo,
    updatePayloadTypeInfo: TypeInfo,
    listTypeInfo: TypeInfo,
    controller: IModelController<Model, Id, CreatePayload, UpdatePayload>,
    getPublicMenuForCallUseCase: IGetPublicMenuForCallUseCase,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    mapping: TemplateMapping,
    respondTemplate: (suspend ApplicationCall.(String, Map<String, Any>) -> Unit)? = null,
    route: String? = null,
    id: String? = null,
    prefix: String? = null,
) : PublicChildModelRouter<Model, Id, CreatePayload, UpdatePayload, UnitModel, Unit>(
    modelTypeInfo,
    createPayloadTypeInfo,
    updatePayloadTypeInfo,
    listTypeInfo,
    controller,
    null,
    getPublicMenuForCallUseCase,
    getLocaleForCallUseCase,
    mapping,
    respondTemplate,
    route,
    id,
    prefix
)
