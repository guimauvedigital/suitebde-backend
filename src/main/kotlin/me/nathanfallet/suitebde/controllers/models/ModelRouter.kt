package me.nathanfallet.suitebde.controllers.models

import com.github.aymanizz.ktori18n.locale
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.reflect.*
import me.nathanfallet.suitebde.controllers.IRouter
import me.nathanfallet.suitebde.models.exceptions.ControllerException
import me.nathanfallet.suitebde.usecases.application.ITranslateUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase

open class ModelRouter<out T, in P, in Q>(
    val route: String,
    val typeInfo: TypeInfo,
    val lTypeInfo: TypeInfo,
    val pTypeInfo: TypeInfo,
    val qTypeInfo: TypeInfo,
    private val controller: IModelController<T, P, Q>,
    private val translateUseCase: ITranslateUseCase,
    private val getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase
) : IRouter {

    override fun createRoutes(root: Route) {
        root.route("/api/v1/$route") {
            authenticate("api-v1-jwt", optional = true) {
                createAPIv1Routes(this)
            }
        }
        root.route("/admin/$route") {
            createAdminGetRoute(this)
            createAdminGetIdRoute(this)
        }
    }

    private fun createAPIv1Routes(root: Route) {
        createAPIv1GetRoute(root)
        createAPIv1GetIdRoute(root)
        createAPIv1PostRoute(root)
        createAPIv1PutIdRoute(root)
        createAPIv1DeleteIdRoute(root)
    }

    private suspend fun handleExceptionAPI(exception: ControllerException, call: ApplicationCall) {
        call.response.status(exception.code)
        call.respond(mapOf("error" to translateUseCase(call.locale, exception.key)))
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

    fun createAPIv1GetRoute(root: Route) {
        root.get {
            try {
                call.respond(controller.getAll(call), lTypeInfo)
            } catch (exception: ControllerException) {
                handleExceptionAPI(exception, call)
            }
        }
    }

    fun createAPIv1GetIdRoute(root: Route) {
        root.get("/{id}") {
            try {
                val id = call.parameters["id"]!!
                call.respond(controller.get(call, id), typeInfo)
            } catch (exception: ControllerException) {
                handleExceptionAPI(exception, call)
            }
        }
    }

    fun createAPIv1PostRoute(root: Route) {
        root.post {
            try {
                val response = controller.create(call, call.receive(pTypeInfo))
                call.response.status(HttpStatusCode.Created)
                call.respond(response, typeInfo)
            } catch (exception: ControllerException) {
                handleExceptionAPI(exception, call)
            } catch (exception: ContentTransformationException) {
                handleExceptionAPI(
                    ControllerException(
                        HttpStatusCode.BadRequest, "error_body_invalid"
                    ), call
                )
            }
        }
    }

    fun createAPIv1PutIdRoute(root: Route) {
        root.put("/{id}") {
            try {
                val id = call.parameters["id"]!!
                call.respond(controller.update(call, id, call.receive(qTypeInfo)), typeInfo)
            } catch (exception: ControllerException) {
                handleExceptionAPI(exception, call)
            } catch (exception: ContentTransformationException) {
                handleExceptionAPI(
                    ControllerException(
                        HttpStatusCode.BadRequest, "error_body_invalid"
                    ), call
                )
            }
        }
    }

    fun createAPIv1DeleteIdRoute(root: Route) {
        root.delete("/{id}") {
            try {
                val id = call.parameters["id"]!!
                controller.delete(call, id)
                call.respond(HttpStatusCode.NoContent)
            } catch (exception: ControllerException) {
                handleExceptionAPI(exception, call)
            }
        }
    }

    fun createAdminGetRoute(root: Route) {
        root.get {
            try {
                call.respondTemplate(
                    "admin/models/list.ftl",
                    mapOf(
                        "locale" to call.locale,
                        "title" to translateUseCase(call.locale, "admin_menu_$route"),
                        "route" to route,
                        "menu" to getAdminMenuForCallUseCase(call, call.locale),
                        "items" to controller.getAll(call),
                        "keys" to controller.modelKeys
                    )
                )
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
                        "item" to controller.get(call, id),
                        "keys" to controller.modelKeys
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
                controller.update(call, id, call.receive(qTypeInfo))
                call.respondRedirect("/admin/$route")
            } catch (exception: ControllerException) {
                handleExceptionAdmin(exception, call)
            }
        }
    }

}