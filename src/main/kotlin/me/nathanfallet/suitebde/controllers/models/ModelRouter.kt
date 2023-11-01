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
import kotlin.reflect.KClass

open class ModelRouter<T : Any, P : Any, Q : Any>(
    val route: String,
    modelClass: KClass<T>,
    createPayloadClass: KClass<P>,
    updatePayloadClass: KClass<Q>,
    controller: IModelController<T, P, Q>,
    private val translateUseCase: ITranslateUseCase,
    private val getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase
) : APIModelRouter<T, P, Q>(
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
        root.route("/admin/$route") {
            createAdminRoutes(this)
        }
    }

    private fun createAdminRoutes(root: Route) {
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
        root.get {
            try {
                println(modelKeys)
                call.respondTemplate(
                    "admin/models/list.ftl",
                    mapOf(
                        "locale" to call.locale,
                        "title" to translateUseCase(call.locale, "admin_menu_$route"),
                        "route" to route,
                        "menu" to getAdminMenuForCallUseCase(call, call.locale),
                        "items" to controller.getAll(call, Unit),
                        "keys" to modelKeys
                    )
                )
            } catch (exception: ControllerException) {
                handleExceptionAdmin(exception, call)
            }
        }
    }

    fun createAdminGetCreateRoute(root: Route) {
        root.get("/create") {
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
        root.post("/create") {
            try {
                val payload = constructPayload(
                    createPayloadClass, call.receiveParameters()
                ) ?: throw ControllerException(HttpStatusCode.BadRequest, "error_body_invalid")
                controller.create(call, Unit, payload)
                call.respondRedirect("/admin/$route")
            } catch (exception: ControllerException) {
                handleExceptionAdmin(exception, call)
            }
        }
    }

    fun createAdminGetIdRoute(root: Route) {
        root.get("/{id}") {
            try {
                val id = call.parameters["id"]!!
                call.respondTemplate(
                    "admin/models/form.ftl",
                    mapOf(
                        "locale" to call.locale,
                        "title" to translateUseCase(call.locale, "admin_menu_$route"),
                        "route" to route,
                        "menu" to getAdminMenuForCallUseCase(call, call.locale),
                        "item" to controller.get(call, Unit, id),
                        "keys" to updatePayloadKeys
                    )
                )
            } catch (exception: ControllerException) {
                handleExceptionAdmin(exception, call)
            }
        }
    }

    fun createAdminPostIdRoute(root: Route) {
        root.post("/{id}") {
            try {
                val id = call.parameters["id"]!!
                val payload = constructPayload(
                    updatePayloadClass, call.receiveParameters()
                ) ?: throw ControllerException(HttpStatusCode.BadRequest, "error_body_invalid")
                controller.update(call, Unit, id, payload)
                call.respondRedirect("/admin/$route")
            } catch (exception: ControllerException) {
                handleExceptionAdmin(exception, call)
            }
        }
    }

    fun createAdminDeleteIdRoute(root: Route) {
        root.get("/{id}/delete") {
            try {
                val id = call.parameters["id"]!!
                controller.delete(call, Unit, id)
                call.respondRedirect("/admin/$route")
            } catch (exception: ControllerException) {
                handleExceptionAdmin(exception, call)
            }
        }
    }

}