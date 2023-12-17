package me.nathanfallet.suitebde.controllers.models

import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.controllers.IChildModelController
import me.nathanfallet.ktorx.models.templates.TemplateMapping
import me.nathanfallet.ktorx.routers.IChildModelRouter
import me.nathanfallet.ktorx.routers.templates.LocalizedTemplateChildModelRouter
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.usecases.web.IGetPublicMenuForCallUseCase
import me.nathanfallet.usecases.models.IChildModel

open class PublicChildModelRouter<Model : IChildModel<Id, CreatePayload, UpdatePayload, ParentId>, Id, CreatePayload : Any, UpdatePayload : Any, ParentModel : IChildModel<ParentId, *, *, *>, ParentId>(
    modelTypeInfo: TypeInfo,
    createPayloadTypeInfo: TypeInfo,
    updatePayloadTypeInfo: TypeInfo,
    listTypeInfo: TypeInfo,
    controller: IChildModelController<Model, Id, CreatePayload, UpdatePayload, ParentModel, ParentId>,
    parentRouter: IChildModelRouter<ParentModel, ParentId, *, *, *, *>?,
    private val getPublicMenuForCallUseCase: IGetPublicMenuForCallUseCase,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    mapping: TemplateMapping,
    respondTemplate: (suspend ApplicationCall.(String, Map<String, Any>) -> Unit)? = null,
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
    mapping,
    { template, model ->
        val newModel = model + mapOf(
            "menu" to getPublicMenuForCallUseCase(this),
        )
        respondTemplate?.invoke(this, template, newModel) ?: respondTemplate(template, newModel)
    },
    getLocaleForCallUseCase,
    route,
    id,
    prefix
)
