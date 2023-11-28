package me.nathanfallet.suitebde.controllers.models

import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import me.nathanfallet.ktorx.controllers.base.IChildModelController
import me.nathanfallet.ktorx.models.templates.TemplateMapping
import me.nathanfallet.ktorx.routers.IChildModelRouter
import me.nathanfallet.ktorx.routers.localization.LocalizedTemplateChildModelRouter
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.usecases.web.IGetPublicMenuForCallUseCase
import me.nathanfallet.usecases.models.IChildModel
import kotlin.reflect.KClass

open class PublicChildModelRouter<Model : IChildModel<Id, CreatePayload, UpdatePayload, ParentId>, Id, CreatePayload : Any, UpdatePayload : Any, ParentModel : IChildModel<ParentId, *, *, *>, ParentId>(
    modelClass: KClass<Model>,
    createPayloadClass: KClass<CreatePayload>,
    updatePayloadClass: KClass<UpdatePayload>,
    controller: IChildModelController<Model, Id, CreatePayload, UpdatePayload, ParentModel, ParentId>,
    parentRouter: IChildModelRouter<ParentModel, ParentId, *, *, *, *>?,
    private val getPublicMenuForCallUseCase: IGetPublicMenuForCallUseCase,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    mapping: TemplateMapping,
    respondTemplate: (suspend ApplicationCall.(String, Map<String, Any>) -> Unit)? = null,
    route: String? = null,
    id: String? = null,
    prefix: String? = null
) : LocalizedTemplateChildModelRouter<Model, Id, CreatePayload, UpdatePayload, ParentModel, ParentId>(
    modelClass,
    createPayloadClass,
    updatePayloadClass,
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
