package me.nathanfallet.suitebde.controllers.models

import com.github.aymanizz.ktori18n.locale
import io.ktor.server.freemarker.*
import me.nathanfallet.ktorx.controllers.base.IChildModelController
import me.nathanfallet.ktorx.models.templates.TemplateMapping
import me.nathanfallet.ktorx.routers.IChildModelRouter
import me.nathanfallet.ktorx.routers.templates.TemplateChildModelRouter
import me.nathanfallet.suitebde.usecases.application.ITranslateUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.usecases.models.IChildModel
import kotlin.reflect.KClass

open class AdminChildModelRouter<Model : IChildModel<Id, CreatePayload, UpdatePayload, ParentId>, Id, CreatePayload : Any, UpdatePayload : Any, ParentModel : IChildModel<ParentId, *, *, *>, ParentId>(
    modelClass: KClass<Model>,
    createPayloadClass: KClass<CreatePayload>,
    updatePayloadClass: KClass<UpdatePayload>,
    controller: IChildModelController<Model, Id, CreatePayload, UpdatePayload, ParentModel, ParentId>,
    parentRouter: IChildModelRouter<ParentModel, ParentId, *, *, *, *>?,
    private val translateUseCase: ITranslateUseCase,
    private val getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
    route: String? = null,
    id: String? = null,
    prefix: String? = null
) : TemplateChildModelRouter<Model, Id, CreatePayload, UpdatePayload, ParentModel, ParentId>(
    modelClass,
    createPayloadClass,
    updatePayloadClass,
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
        respondTemplate(
            template, model + mapOf(
                "locale" to locale,
                "title" to translateUseCase(locale, "admin_menu_${model["route"]}"),
                "menu" to getAdminMenuForCallUseCase(this, locale),
            )
        )
    },
    route,
    id,
    prefix ?: "/admin"
)
