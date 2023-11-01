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
import io.sentry.Sentry
import me.nathanfallet.suitebde.controllers.IRouter
import me.nathanfallet.suitebde.models.exceptions.ControllerException
import me.nathanfallet.suitebde.usecases.application.ITranslateUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import kotlin.reflect.KClass
import kotlin.reflect.KTypeProjection
import kotlin.reflect.KVariance
import kotlin.reflect.full.createType
import kotlin.reflect.full.starProjectedType

open class ModelRouter<T : Any, in P : Any, in Q : Any>(
    val route: String,
    modelClass: KClass<T>,
    createPayloadClass: KClass<P>,
    updatePayloadClass: KClass<Q>,
    private val controller: IModelController<T, P, Q>,
    private val translateUseCase: ITranslateUseCase,
    private val getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase
) : IRouter {

    private val modelTypeInfo = TypeInfo(
        modelClass, modelClass.java,
        modelClass.starProjectedType
    )
    private val createPayloadTypeInfo = TypeInfo(
        createPayloadClass, createPayloadClass.java,
        createPayloadClass.starProjectedType
    )
    private val updatePayloadTypeInfo = TypeInfo(
        updatePayloadClass, updatePayloadClass.java,
        updatePayloadClass.starProjectedType
    )
    private val listTypeInfo = TypeInfo(
        List::class, List::class.java,
        List::class.createType(
            listOf(KTypeProjection(KVariance.INVARIANT, modelClass.starProjectedType))
        )
    )

    @Suppress("UNCHECKED_CAST")
    private fun <O> constructObject(typeInfo: TypeInfo, parameters: Parameters): O? {
        return try {
            val constructor = typeInfo.type.constructors.firstOrNull {
                it.parameters.all { parameter ->
                    parameter.name in parameters.names()
                }
            } ?: return null
            val params = constructor.parameters.associateWith {
                it.name?.let { name -> parameters[name] }
            }
            constructor.callBy(params) as? O
        } catch (exception: Exception) {
            exception.printStackTrace()
            Sentry.captureException(exception)
            null
        }
    }

    override fun createRoutes(root: Route) {
        root.route("/api/v1/$route") {
            authenticate("api-v1-jwt", optional = true) {
                createAPIv1Routes(this)
            }
        }
        root.route("/admin/$route") {
            createAdminRoutes(this)
        }
    }

    private fun createAPIv1Routes(root: Route) {
        createAPIv1GetRoute(root)
        createAPIv1GetIdRoute(root)
        createAPIv1PostRoute(root)
        createAPIv1PutIdRoute(root)
        createAPIv1DeleteIdRoute(root)
    }

    private fun createAdminRoutes(root: Route) {
        createAdminGetRoute(root)
        createAdminGetCreateRoute(root)
        createAdminPostCreateRoute(root)
        createAdminGetIdRoute(root)
        createAdminPostIdRoute(root)
        createAdminDeleteIdRoute(root)
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
                call.respond(controller.getAll(call), listTypeInfo)
            } catch (exception: ControllerException) {
                handleExceptionAPI(exception, call)
            }
        }
    }

    fun createAPIv1GetIdRoute(root: Route) {
        root.get("/{id}") {
            try {
                val id = call.parameters["id"]!!
                call.respond(controller.get(call, id), modelTypeInfo)
            } catch (exception: ControllerException) {
                handleExceptionAPI(exception, call)
            }
        }
    }

    fun createAPIv1PostRoute(root: Route) {
        root.post {
            try {
                val response = controller.create(call, call.receive(createPayloadTypeInfo))
                call.response.status(HttpStatusCode.Created)
                call.respond(response, modelTypeInfo)
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
                call.respond(controller.update(call, id, call.receive(updatePayloadTypeInfo)), modelTypeInfo)
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
                        "keys" to controller.modelKeys
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
                val payload =
                    constructObject<P>(updatePayloadTypeInfo, call.receiveParameters()) ?: throw ControllerException(
                    HttpStatusCode.BadRequest, "error_body_invalid"
                )
                controller.create(call, payload)
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
                val payload =
                    constructObject<Q>(updatePayloadTypeInfo, call.receiveParameters()) ?: throw ControllerException(
                    HttpStatusCode.BadRequest, "error_body_invalid"
                )
                controller.update(call, id, payload)
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
                controller.delete(call, id)
                call.respondRedirect("/admin/$route")
            } catch (exception: ControllerException) {
                handleExceptionAdmin(exception, call)
            }
        }
    }

}