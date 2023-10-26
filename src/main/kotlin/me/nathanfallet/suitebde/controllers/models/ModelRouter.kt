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
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForDomainUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUserForCallUseCase

open class ModelRouter<out T, in P, in Q>(
    val route: String,
    val typeInfo: TypeInfo,
    val lTypeInfo: TypeInfo,
    val pTypeInfo: TypeInfo,
    val qTypeInfo: TypeInfo,
    private val controller: IModelController<T, P, Q>,
    private val getAssociationForDomainUseCase: IGetAssociationForDomainUseCase,
    private val getUserForCallUseCase: IGetUserForCallUseCase
) : IRouter {

    override fun createRoutes(root: Route) {
        root.route("/api/v1/$route") {
            authenticate("api-v1-jwt", optional = true) {
                createAPIv1GetRoute(this)
                createAPIv1GetIdRoute(this)
                createAPIv1PostRoute(this)
                createAPIv1PutIdRoute(this)
                createAPIv1DeleteIdRoute(this)
            }
        }
    }

    fun createAPIv1GetRoute(root: Route) {
        root.get {
            val association = getAssociationForDomainUseCase(call.request.host()) ?: run {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }
            val response = try {
                controller.getAll(association, getUserForCallUseCase(call))
            } catch (exception: ControllerException) {
                call.response.status(exception.code)
                call.respond(mapOf("error" to exception.message))
                return@get
            }
            call.respond(response, lTypeInfo)
        }
    }

    fun createAPIv1GetIdRoute(root: Route) {
        root.get("/{id}") {
            val association = getAssociationForDomainUseCase(call.request.host()) ?: run {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }
            val response = try {
                controller.get(association, getUserForCallUseCase(call), call.parameters["id"] ?: return@get)
            } catch (exception: ControllerException) {
                call.response.status(exception.code)
                call.respond(mapOf("error" to exception.message))
                return@get
            }
            call.respond(response, typeInfo)
        }
    }

    fun createAPIv1PostRoute(root: Route) {
        root.post {
            val association = getAssociationForDomainUseCase(call.request.host()) ?: run {
                call.respond(HttpStatusCode.NotFound)
                return@post
            }
            val response = try {
                controller.create(association, getUserForCallUseCase(call), call.receive(pTypeInfo))
            } catch (exception: ControllerException) {
                call.response.status(exception.code)
                call.respond(mapOf("error" to exception.message))
                return@post
            } catch (exception: ContentTransformationException) {
                call.response.status(HttpStatusCode.BadRequest)
                call.respond(mapOf("error" to LocalizedString.ERROR_BODY_INVALID.value))
                return@post
            }
            call.response.status(HttpStatusCode.Created)
            call.respond(response, typeInfo)
        }
    }

    fun createAPIv1PutIdRoute(root: Route) {
        root.put("/{id}") {
            val association = getAssociationForDomainUseCase(call.request.host()) ?: run {
                call.respond(HttpStatusCode.NotFound)
                return@put
            }
            val response = try {
                controller.update(
                    association,
                    getUserForCallUseCase(call),
                    call.parameters["id"] ?: return@put,
                    call.receive(qTypeInfo)
                )
            } catch (exception: ControllerException) {
                call.response.status(exception.code)
                call.respond(mapOf("error" to exception.message))
                return@put
            } catch (exception: ContentTransformationException) {
                call.response.status(HttpStatusCode.BadRequest)
                call.respond(mapOf("error" to LocalizedString.ERROR_BODY_INVALID.value))
                return@put
            }
            call.respond(response, typeInfo)
        }
    }

    fun createAPIv1DeleteIdRoute(root: Route) {
        root.delete("/{id}") {
            val association = getAssociationForDomainUseCase(call.request.host()) ?: run {
                call.respond(HttpStatusCode.NotFound)
                return@delete
            }
            try {
                controller.delete(association, getUserForCallUseCase(call), call.parameters["id"] ?: return@delete)
            } catch (exception: ControllerException) {
                call.response.status(exception.code)
                call.respond(mapOf("error" to exception.message))
                return@delete
            }
            call.respond(HttpStatusCode.NoContent)
        }
    }

}