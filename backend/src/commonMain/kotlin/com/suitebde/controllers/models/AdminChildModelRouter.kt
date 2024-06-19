package com.suitebde.controllers.models

import com.suitebde.usecases.associations.IGetAssociationForCallUseCase
import com.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import dev.kaccelero.annotations.PayloadKey
import dev.kaccelero.commons.localization.IGetLocaleForCallUseCase
import dev.kaccelero.commons.localization.ITranslateUseCase
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.controllers.IChildModelController
import dev.kaccelero.models.IChildModel
import dev.kaccelero.routers.IChildModelRouter
import dev.kaccelero.routers.LocalizedAdminChildModelRouter
import io.ktor.server.freemarker.*
import io.ktor.util.reflect.*
import kotlin.reflect.KClass

open class AdminChildModelRouter<Model : IChildModel<Id, CreatePayload, UpdatePayload, ParentId>, Id, CreatePayload : Any, UpdatePayload : Any, ParentModel : IChildModel<ParentId, *, *, *>, ParentId>(
    modelTypeInfo: TypeInfo,
    createPayloadTypeInfo: TypeInfo,
    updatePayloadTypeInfo: TypeInfo,
    controller: IChildModelController<Model, Id, CreatePayload, UpdatePayload, ParentModel, ParentId>,
    controllerClass: KClass<out IChildModelController<Model, Id, CreatePayload, UpdatePayload, ParentModel, ParentId>>,
    parentRouter: IChildModelRouter<ParentModel, ParentId, *, *, *, *>?,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    requireUserForCallUseCase: IRequireUserForCallUseCase,
    getAssociationForCallUseCase: IGetAssociationForCallUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
    route: String? = null,
    id: String? = null,
) : LocalizedAdminChildModelRouter<Model, Id, CreatePayload, UpdatePayload, ParentModel, ParentId>(
    modelTypeInfo,
    createPayloadTypeInfo,
    updatePayloadTypeInfo,
    controller,
    controllerClass,
    parentRouter,
    { template, model ->
        if (template == "root/error.ftl") respondTemplate(template, model)
        else respondTemplate(
            template, model + mapOf(
                "title" to translateUseCase(
                    getLocaleForCallUseCase(this),
                    ((model["route"] as String).takeIf { it.isNotEmpty() } ?: "dashboard").let { "admin_menu_$it" }
                ),
                "user" to requireUserForCallUseCase(this),
                "association" to getAssociationForCallUseCase(this),
                "menu" to getAdminMenuForCallUseCase(this),
                "flatpickr" to ((model["keys"] as? List<*>)?.any { (it as? PayloadKey)?.type == "date" } == true),
                "usersearch" to ((model["keys"] as? List<*>)?.any { (it as? PayloadKey)?.type == "user" } == true),
            )
        )
    },
    getLocaleForCallUseCase,
    "root/error.ftl",
    "/auth/login?redirect={path}",
    "admin/models/list.ftl",
    null,
    "admin/models/form.ftl",
    "admin/models/form.ftl",
    "admin/models/delete.ftl",
    route,
    id,
)
