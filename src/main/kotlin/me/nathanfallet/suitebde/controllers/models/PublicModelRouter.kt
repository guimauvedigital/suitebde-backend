package me.nathanfallet.suitebde.controllers.models

import io.ktor.server.application.*
import me.nathanfallet.ktorx.controllers.base.IModelController
import me.nathanfallet.ktorx.models.templates.TemplateMapping
import me.nathanfallet.suitebde.usecases.web.IGetPublicMenuForCallUseCase
import me.nathanfallet.usecases.models.IModel
import me.nathanfallet.usecases.models.UnitModel
import kotlin.reflect.KClass

class PublicModelRouter<Model : IModel<Id, CreatePayload, UpdatePayload>, Id, CreatePayload : Any, UpdatePayload : Any>(
    modelClass: KClass<Model>,
    createPayloadClass: KClass<CreatePayload>,
    updatePayloadClass: KClass<UpdatePayload>,
    controller: IModelController<Model, Id, CreatePayload, UpdatePayload>,
    getPublicMenuForCallUseCase: IGetPublicMenuForCallUseCase,
    mapping: TemplateMapping,
    respondTemplate: (suspend ApplicationCall.(String, Map<String, Any>) -> Unit)? = null,
    route: String? = null,
    id: String? = null,
    prefix: String? = null
) : PublicChildModelRouter<Model, Id, CreatePayload, UpdatePayload, UnitModel, Unit>(
    modelClass,
    createPayloadClass,
    updatePayloadClass,
    controller,
    null,
    getPublicMenuForCallUseCase,
    mapping,
    respondTemplate,
    route,
    id,
    prefix
)
