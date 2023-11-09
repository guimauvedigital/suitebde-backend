package me.nathanfallet.suitebde.controllers.models

import com.github.aymanizz.ktori18n.locale
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.nathanfallet.ktor.routers.controllers.base.IChildModelController
import me.nathanfallet.ktor.routers.models.exceptions.ControllerException
import me.nathanfallet.ktor.routers.routers.base.AbstractChildModelRouter
import me.nathanfallet.suitebde.usecases.application.ITranslateUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.usecases.models.IChildModel
import kotlin.reflect.KClass

open class AdminChildModelRouter<Model : IChildModel<Id, CreatePayload, UpdatePayload, ParentId>, Id, CreatePayload : Any, UpdatePayload : Any, ParentModel : IChildModel<ParentId, *, *, *>, ParentId>(
    modelClass: KClass<Model>,
    createPayloadClass: KClass<CreatePayload>,
    updatePayloadClass: KClass<UpdatePayload>,
    controller: IChildModelController<Model, Id, CreatePayload, UpdatePayload, ParentModel, ParentId>,
    parentRouter: AdminChildModelRouter<ParentModel, ParentId, *, *, *, *>?,
    private val translateUseCase: ITranslateUseCase,
    private val getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
    route: String? = null,
    id: String? = null,
    prefix: String? = null
) : AbstractChildModelRouter<Model, Id, CreatePayload, UpdatePayload, ParentModel, ParentId>(
    modelClass,
    createPayloadClass,
    updatePayloadClass,
    controller,
    parentRouter,
    route,
    id,
    prefix ?: "/admin"
) {

    override fun createRoutes(root: Route) {
        createAdminGetRoute(root)
        createAdminGetCreateRoute(root)
        createAdminPostCreateRoute(root)
        createAdminGetIdRoute(root)
        createAdminPostIdRoute(root)
        createAdminDeleteIdRoute(root)
    }

    private suspend fun handleExceptionAdmin(exception: ControllerException, call: ApplicationCall) {
        if (exception.code == HttpStatusCode.Unauthorized) {
            call.respondRedirect("/auth/login?redirect=$fullRoute")
            return
        }
        call.response.status(exception.code)
        call.respondTemplate(
            "root/error.ftl",
            mapOf(
                "locale" to call.locale,
                "code" to exception.code.value,
                "error" to translateUseCase(call.locale, exception.key)
            )
        )
    }

    fun createAdminGetRoute(root: Route) {
        root.get(fullRoute) {
            try {
                println(getAll(call))
                println(modelKeys)
                call.respondTemplate(
                    "admin/models/list.ftl",
                    mapOf(
                        "locale" to call.locale,
                        "title" to translateUseCase(call.locale, "admin_menu_$route"),
                        "route" to route,
                        "menu" to getAdminMenuForCallUseCase(call, call.locale),
                        "items" to getAll(call),
                        "keys" to modelKeys
                    )
                )
            } catch (exception: ControllerException) {
                handleExceptionAdmin(exception, call)
            }
        }
    }

    fun createAdminGetCreateRoute(root: Route) {
        root.get("$fullRoute/create") {
            try {
                call.respondTemplate(
                    "admin/models/form.ftl",
                    mapOf(
                        "locale" to call.locale,
                        "title" to translateUseCase(call.locale, "admin_menu_$route"),
                        "route" to route,
                        "menu" to getAdminMenuForCallUseCase(call, call.locale),
                        "keys" to createPayloadKeys
                    )
                )
            } catch (exception: ControllerException) {
                handleExceptionAdmin(exception, call)
            }
        }
    }

    fun createAdminPostCreateRoute(root: Route) {
        root.post("$fullRoute/create") {
            try {
                val payload = constructPayload(
                    createPayloadClass, call.receiveParameters()
                ) ?: throw ControllerException(HttpStatusCode.BadRequest, "error_body_invalid")
                create(call, payload)
                call.respondRedirect("/admin/$route")
            } catch (exception: ControllerException) {
                handleExceptionAdmin(exception, call)
            }
        }
    }

    fun createAdminGetIdRoute(root: Route) {
        root.get("$fullRoute/{$id}") {
            try {
                call.respondTemplate(
                    "admin/models/form.ftl",
                    mapOf(
                        "locale" to call.locale,
                        "title" to translateUseCase(call.locale, "admin_menu_$route"),
                        "route" to route,
                        "menu" to getAdminMenuForCallUseCase(call, call.locale),
                        "item" to get(call),
                        "keys" to updatePayloadKeys
                    )
                )
            } catch (exception: ControllerException) {
                handleExceptionAdmin(exception, call)
            }
        }
    }

    fun createAdminPostIdRoute(root: Route) {
        root.post("$fullRoute/{$id}") {
            try {
                val payload = constructPayload(
                    updatePayloadClass, call.receiveParameters()
                ) ?: throw ControllerException(HttpStatusCode.BadRequest, "error_body_invalid")
                update(call, payload)
                call.respondRedirect("/admin/$route")
            } catch (exception: ControllerException) {
                handleExceptionAdmin(exception, call)
            }
        }
    }

    fun createAdminDeleteIdRoute(root: Route) {
        root.get("$fullRoute/{$id}/delete") {
            try {
                delete(call)
                call.respondRedirect("/admin/$route")
            } catch (exception: ControllerException) {
                handleExceptionAdmin(exception, call)
            }
        }
    }

}
