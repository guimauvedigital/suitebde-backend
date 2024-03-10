package me.nathanfallet.suitebde.controllers.models

import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.controllers.IChildModelController
import me.nathanfallet.ktorx.routers.IChildModelRouter
import me.nathanfallet.ktorx.routers.templates.LocalizedTemplateChildModelRouter
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.ktorx.usecases.users.IGetUserForCallUseCase
import me.nathanfallet.suitebde.usecases.web.IGetPublicMenuForCallUseCase
import me.nathanfallet.usecases.models.IChildModel
import kotlin.reflect.KClass

open class PublicChildModelRouter<Model : IChildModel<Id, CreatePayload, UpdatePayload, ParentId>, Id, CreatePayload : Any, UpdatePayload : Any, ParentModel : IChildModel<ParentId, *, *, *>, ParentId>(
    modelTypeInfo: TypeInfo,
    createPayloadTypeInfo: TypeInfo,
    updatePayloadTypeInfo: TypeInfo,
    controller: IChildModelController<Model, Id, CreatePayload, UpdatePayload, ParentModel, ParentId>,
    controllerClass: KClass<out IChildModelController<Model, Id, CreatePayload, UpdatePayload, ParentModel, ParentId>>,
    parentRouter: IChildModelRouter<ParentModel, ParentId, *, *, *, *>?,
    getUserForCallUseCase: IGetUserForCallUseCase,
    getPublicMenuForCallUseCase: IGetPublicMenuForCallUseCase,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    respondTemplate: (suspend ApplicationCall.(String, Map<String, Any?>) -> Unit)? = null,
    route: String? = null,
    id: String? = null,
    prefix: String? = null,
) : LocalizedTemplateChildModelRouter<Model, Id, CreatePayload, UpdatePayload, ParentModel, ParentId>(
    modelTypeInfo,
    createPayloadTypeInfo,
    updatePayloadTypeInfo,
    controller,
    controllerClass,
    parentRouter,
    { template, model ->
        if (template == "root/error.ftl") respondTemplate(template, model)
        else (model + mapOf(
            "user" to getUserForCallUseCase(this),
            "menu" to getPublicMenuForCallUseCase(this),
        )).let { newModel ->
            respondTemplate?.invoke(this, template, newModel) ?: respondTemplate(template, newModel)
        }
    },
    getLocaleForCallUseCase,
    "root/error.ftl",
    "/auth/login?redirect={path}",
    route,
    id,
    prefix
)
