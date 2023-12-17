package me.nathanfallet.suitebde.controllers.models

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.controllers.IModelController
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase
import me.nathanfallet.usecases.models.IModel
import me.nathanfallet.usecases.models.UnitModel

open class AdminModelRouter<Model : IModel<Id, CreatePayload, UpdatePayload>, Id, CreatePayload : Any, UpdatePayload : Any>(
    modelTypeInfo: TypeInfo,
    createPayloadTypeInfo: TypeInfo,
    updatePayloadTypeInfo: TypeInfo,
    listTypeInfo: TypeInfo,
    controller: IModelController<Model, Id, CreatePayload, UpdatePayload>,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
    route: String? = null,
    id: String? = null,
    prefix: String? = null,
) : AdminChildModelRouter<Model, Id, CreatePayload, UpdatePayload, UnitModel, Unit>(
    modelTypeInfo,
    createPayloadTypeInfo,
    updatePayloadTypeInfo,
    listTypeInfo,
    controller,
    null,
    getLocaleForCallUseCase,
    translateUseCase,
    getAdminMenuForCallUseCase,
    route,
    id,
    prefix
)
