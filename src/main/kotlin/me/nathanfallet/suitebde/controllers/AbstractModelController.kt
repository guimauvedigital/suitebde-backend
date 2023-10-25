package me.nathanfallet.suitebde.controllers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.reflect.*
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForDomainUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUserForCallUseCase

abstract class AbstractModelController<T, P, Q>(
    private val route: String,
    private val typeInfo: TypeInfo,
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
        createAPIRoutes(root)
    }

    private fun createAPIRoutes(root: Route) {
        root.route("/api/v1/$route") {
            authenticate("api-v1-jwt", optional = true) {
                get {
                    val association = getAssociationForDomainUseCase(call.request.host()) ?: run {
                        call.respond(HttpStatusCode.NotFound)
                        return@get
                    }
                    call.respond(getAll(association, getUserForCallUseCase(call)))
                }
                get("/{id}") {
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
                post {
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
                        call.respond(mapOf("error" to "Invalid request body"))
                        return@post
                    }
                    call.response.status(HttpStatusCode.Created)
                    call.respond(response, typeInfo)
                }
                put("/{id}") {
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
                        call.respond(mapOf("error" to "Invalid request body"))
                        return@put
                    }
                    call.respond(response, typeInfo)
                }
                delete("/{id}") {
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
    }

}