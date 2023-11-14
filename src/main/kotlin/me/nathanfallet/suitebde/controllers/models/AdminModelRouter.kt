package me.nathanfallet.suitebde.controllers.models

import me.nathanfallet.ktorx.controllers.base.IModelController
import me.nathanfallet.suitebde.usecases.application.ITranslateUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.usecases.models.IModel
import me.nathanfallet.usecases.models.UnitModel
import kotlin.reflect.KClass

open class AdminModelRouter<Model : IModel<Id, CreatePayload, UpdatePayload>, Id, CreatePayload : Any, UpdatePayload : Any>(
    modelClass: KClass<Model>,
    createPayloadClass: KClass<CreatePayload>,
    updatePayloadClass: KClass<UpdatePayload>,
    controller: IModelController<Model, Id, CreatePayload, UpdatePayload>,
    translateUseCase: ITranslateUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
    route: String? = null,
    id: String? = null,
    prefix: String? = null
) : AdminChildModelRouter<Model, Id, CreatePayload, UpdatePayload, UnitModel, Unit>(
    modelClass,
    createPayloadClass,
    updatePayloadClass,
    controller,
    null,
    translateUseCase,
    getAdminMenuForCallUseCase,
    route,
    id,
    prefix
)
