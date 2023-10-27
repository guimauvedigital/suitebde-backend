package me.nathanfallet.suitebde.controllers.models

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.reflect.*
import me.nathanfallet.suitebde.controllers.IRouter
import me.nathanfallet.suitebde.models.LocalizedString
import me.nathanfallet.suitebde.models.exceptions.ControllerException

open class ModelRouter<out T, in P, in Q>(
    val route: String,
    val typeInfo: TypeInfo,
    val lTypeInfo: TypeInfo,
    val pTypeInfo: TypeInfo,
    val qTypeInfo: TypeInfo,
    private val controller: IModelController<T, P, Q>
) : IRouter {

    override fun createRoutes(root: Route) {
        root.route("/api/v1/$route") {
            authenticate("api-v1-jwt", optional = true) {
                createAPIv1Routes(this)
            }
        }
    }

    private fun createAPIv1Routes(root: Route) {
        createAPIv1GetRoute(root)
        createAPIv1GetIdRoute(root)
        createAPIv1PostRoute(root)
        createAPIv1PutIdRoute(root)
        createAPIv1DeleteIdRoute(root)
    }

    fun createAPIv1GetRoute(root: Route) {
        root.get {
            try {
                call.respond(controller.getAll(call), lTypeInfo)
            } catch (exception: ControllerException) {
                call.response.status(exception.code)
                call.respond(mapOf("error" to exception.message))
            }
        }
    }

    fun createAPIv1GetIdRoute(root: Route) {
        root.get("/{id}") {
            try {
                call.respond(controller.get(call), typeInfo)
            } catch (exception: ControllerException) {
                call.response.status(exception.code)
                call.respond(mapOf("error" to exception.message))
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
                call.response.status(exception.code)
                call.respond(mapOf("error" to exception.message))
            } catch (exception: ContentTransformationException) {
                call.response.status(HttpStatusCode.BadRequest)
                call.respond(mapOf("error" to LocalizedString.ERROR_BODY_INVALID.value))
            }
        }
    }

    fun createAPIv1PutIdRoute(root: Route) {
        root.put("/{id}") {
            try {
                call.respond(controller.update(call, call.receive(qTypeInfo)), typeInfo)
            } catch (exception: ControllerException) {
                call.response.status(exception.code)
                call.respond(mapOf("error" to exception.message))
            } catch (exception: ContentTransformationException) {
                call.response.status(HttpStatusCode.BadRequest)
                call.respond(mapOf("error" to LocalizedString.ERROR_BODY_INVALID.value))
            }
        }
    }

    fun createAPIv1DeleteIdRoute(root: Route) {
        root.delete("/{id}") {
            try {
                controller.delete(call)
                call.respond(HttpStatusCode.NoContent)
            } catch (exception: ControllerException) {
                call.response.status(exception.code)
                call.respond(mapOf("error" to exception.message))
            }
        }
    }

}