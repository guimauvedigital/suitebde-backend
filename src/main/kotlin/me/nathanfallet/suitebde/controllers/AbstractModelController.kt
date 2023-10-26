package me.nathanfallet.suitebde.controllers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.reflect.*
import me.nathanfallet.suitebde.models.LocalizedString
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForDomainUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUserForCallUseCase

abstract class AbstractModelController<out T, in P, in Q>(
    private val route: String,
    private val typeInfo: TypeInfo,
    private val lTypeInfo: TypeInfo,
    private val pTypeInfo: TypeInfo,
    private val qTypeInfo: TypeInfo,
    private val getAssociationForDomainUseCase: IGetAssociationForDomainUseCase,
    private val getUserForCallUseCase: IGetUserForCallUseCase
) : IController {

    abstract suspend fun getAll(association: Association, user: User?): List<T>
    abstract suspend fun get(association: Association, user: User?, id: String): T
    abstract suspend fun create(association: Association, user: User?, obj: P): T
    abstract suspend fun update(association: Association, user: User?, id: String, obj: Q): T
    abstract suspend fun delete(association: Association, user: User?, id: String)

    override fun createRoutes(root: Route) {
        root.route("/api/v1/$route") {
            authenticate("api-v1-jwt", optional = true) {
                createAPIv1Routes(this)
            }
        }
    }

    fun createAPIv1Routes(root: Route) {
        createAPIv1GetRoute(root)
        createAPIv1GetIdRoute(root)
        createAPIv1PostRoute(root)
        createAPIv1PutIdRoute(root)
        createAPIv1DeleteIdRoute(root)
    }

    fun createAPIv1GetRoute(root: Route) {
        root.get {
            val association = getAssociationForDomainUseCase(call.request.host()) ?: run {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }
            val response = try {
                getAll(association, getUserForCallUseCase(call))
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
                get(association, getUserForCallUseCase(call), call.parameters["id"] ?: return@get)
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
                create(association, getUserForCallUseCase(call), call.receive(pTypeInfo))
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
                update(
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
                delete(association, getUserForCallUseCase(call), call.parameters["id"] ?: return@delete)
            } catch (exception: ControllerException) {
                call.response.status(exception.code)
                call.respond(mapOf("error" to exception.message))
                return@delete
            }
            call.respond(HttpStatusCode.NoContent)
        }
    }

}