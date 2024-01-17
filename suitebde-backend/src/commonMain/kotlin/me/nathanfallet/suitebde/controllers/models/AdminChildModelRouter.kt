package me.nathanfallet.suitebde.controllers.models

import io.ktor.server.freemarker.*
import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.controllers.IChildModelController
import me.nathanfallet.ktorx.models.templates.TemplateMapping
import me.nathanfallet.ktorx.routers.IChildModelRouter
import me.nathanfallet.ktorx.routers.templates.LocalizedTemplateChildModelRouter
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase
import me.nathanfallet.usecases.models.IChildModel
import me.nathanfallet.usecases.models.annotations.PayloadKey

open class AdminChildModelRouter<Model : IChildModel<Id, CreatePayload, UpdatePayload, ParentId>, Id, CreatePayload : Any, UpdatePayload : Any, ParentModel : IChildModel<ParentId, *, *, *>, ParentId>(
    modelTypeInfo: TypeInfo,
    createPayloadTypeInfo: TypeInfo,
    updatePayloadTypeInfo: TypeInfo,
    listTypeInfo: TypeInfo,
    controller: IChildModelController<Model, Id, CreatePayload, UpdatePayload, ParentModel, ParentId>,
    parentRouter: IChildModelRouter<ParentModel, ParentId, *, *, *, *>?,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    private val translateUseCase: ITranslateUseCase,
    private val getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
    route: String? = null,
    id: String? = null,
    prefix: String? = null,
) : LocalizedTemplateChildModelRouter<Model, Id, CreatePayload, UpdatePayload, ParentModel, ParentId>(
    modelTypeInfo,
    createPayloadTypeInfo,
    updatePayloadTypeInfo,
    listTypeInfo,
    controller,
    parentRouter,
    TemplateMapping(
        errorTemplate = "root/error.ftl",
        listTemplate = "admin/models/list.ftl",
        createTemplate = "admin/models/form.ftl",
        updateTemplate = "admin/models/form.ftl",
        deleteTemplate = "admin/models/delete.ftl",
        redirectUnauthorizedToUrl = "/auth/login?redirect={path}",
    ),
    { template, model ->
        val flatpickr = (model["keys"] as? List<*>)?.any { (it as? PayloadKey)?.type == "date" } == true
        respondTemplate(
            template, model + mapOf(
                "title" to translateUseCase(getLocaleForCallUseCase(this), "admin_menu_${model["route"]}"),
                "menu" to getAdminMenuForCallUseCase(this),
                "flatpickr" to flatpickr,
            )
        )
    },
    getLocaleForCallUseCase,
    route,
    id,
    prefix ?: "/admin"
)
