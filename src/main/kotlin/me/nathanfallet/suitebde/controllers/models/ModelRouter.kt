package me.nathanfallet.suitebde.controllers.models

import com.github.aymanizz.ktori18n.locale
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.nathanfallet.ktor.routers.controllers.base.IModelController
import me.nathanfallet.ktor.routers.models.exceptions.ControllerException
import me.nathanfallet.ktor.routers.routers.api.APIModelRouter
import me.nathanfallet.suitebde.usecases.application.ITranslateUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.usecases.models.IModel
import kotlin.reflect.KClass

open class ModelRouter<Model : IModel<Id, CreatePayload, UpdatePayload>, Id, CreatePayload : Any, UpdatePayload : Any>(
    modelClass: KClass<Model>,
    createPayloadClass: KClass<CreatePayload>,
    updatePayloadClass: KClass<UpdatePayload>,
    controller: IModelController<Model, Id, CreatePayload, UpdatePayload>,
    private val translateUseCase: ITranslateUseCase,
    private val getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase
) : APIModelRouter<Model, Id, CreatePayload, UpdatePayload>(
    modelClass,
    createPayloadClass,
    updatePayloadClass,
    controller,
    "api/v1"
) {

    override fun createRoutes(root: Route) {
        root.authenticate("api-v1-jwt", optional = true) {
            super.createRoutes(this)
        }
        createAdminGetRoute(root)
        createAdminGetCreateRoute(root)
        createAdminPostCreateRoute(root)
        createAdminGetIdRoute(root)
        createAdminPostIdRoute(root)
        createAdminDeleteIdRoute(root)
    }

    private suspend fun handleExceptionAdmin(exception: ControllerException, call: ApplicationCall) {
        if (exception.code == HttpStatusCode.Unauthorized) {
            call.respondRedirect("/auth/login?redirect=/admin/$route")
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
        root.get("admin/$route") {
            try {
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
        root.get("admin/$route/create") {
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
        root.post("admin/$route/create") {
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
        root.get("admin/$route/{$id}") {
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
        root.post("admin/$route/{$id}") {
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
        root.get("admin/$route/{$id}/delete") {
            try {
                delete(call)
                call.respondRedirect("/admin/$route")
            } catch (exception: ControllerException) {
                handleExceptionAdmin(exception, call)
            }
        }
    }

}