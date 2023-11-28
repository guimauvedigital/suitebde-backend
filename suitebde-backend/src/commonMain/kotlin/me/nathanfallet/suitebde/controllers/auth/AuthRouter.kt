package me.nathanfallet.suitebde.controllers.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.Clock
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.routers.IRouter
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.models.auth.*
import me.nathanfallet.usecases.localization.ITranslateUseCase

class AuthRouter(
    private val controller: IAuthController,
    private val getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    private val translateUseCase: ITranslateUseCase
) : IRouter {

    override fun createRoutes(root: Route) {
        createWebRoutes(root)
        root.route("/{locale}") {
            createWebRoutes(this)
        }
    }

    private fun createWebRoutes(root: Route) {
        root.route("/auth") {
            route("/login") {
                createGetLoginRoute(this)
                createPostLoginRoute(this)
            }
            route("/register") {
                createGetRegisterRoute(this)
                createPostRegisterRoute(this)
                createGetRegisterCodeRoute(this)
                createPostRegisterCodeRoute(this)
            }
            route("/join") {
                createGetJoinRoute(this)
                createPostJoinRoute(this)
                createGetJoinCodeRoute(this)
                createPostJoinCodeRoute(this)
            }
        }
    }

    fun createGetLoginRoute(root: Route) {
        root.get {
            call.respondTemplate(
                "auth/login.ftl",
                mapOf("locale" to getLocaleForCallUseCase(call))
            )
        }
    }

    fun createPostLoginRoute(root: Route) {
        root.post {
            try {
                val parameters = call.receiveParameters()
                val email = parameters["email"]
                val password = parameters["password"]
                if (email == null || password == null) {
                    throw ControllerException(HttpStatusCode.BadRequest, "error_body_invalid")
                }
                controller.login(
                    LoginPayload(
                        email,
                        password
                    ),
                    call
                )
                call.respondRedirect(call.request.queryParameters["redirect"] ?: "/")
            } catch (exception: ControllerException) {
                call.response.status(exception.code)
                call.respondTemplate(
                    "auth/login.ftl",
                    mapOf(
                        "locale" to getLocaleForCallUseCase(call),
                        "error" to translateUseCase(getLocaleForCallUseCase(call), exception.key)
                    )
                )
            }
        }
    }

    fun createGetRegisterRoute(root: Route) {
        root.get {
            call.respondTemplate(
                "auth/register.ftl",
                mapOf("locale" to getLocaleForCallUseCase(call))
            )
        }
    }

    fun createPostRegisterRoute(root: Route) {
        root.post {
            try {
                val parameters = call.receiveParameters()
                val email = parameters["email"] ?: throw ControllerException(
                    HttpStatusCode.BadRequest, "error_body_invalid"
                )
                controller.register(RegisterPayload(email), Clock.System.now(), getLocaleForCallUseCase(call), call)
                call.respondTemplate(
                    "auth/register.ftl",
                    mapOf(
                        "locale" to getLocaleForCallUseCase(call),
                        "success" to translateUseCase(getLocaleForCallUseCase(call), "auth_register_email_sent")
                    )
                )
            } catch (exception: ControllerException) {
                call.response.status(exception.code)
                call.respondTemplate(
                    "auth/register.ftl",
                    mapOf(
                        "locale" to getLocaleForCallUseCase(call),
                        "error" to translateUseCase(getLocaleForCallUseCase(call), exception.key)
                    )
                )
            }
        }
    }

    fun createGetRegisterCodeRoute(root: Route) {
        root.get("/{code}") {
            try {
                val code = call.parameters["code"]!!
                val payload = controller.register(code, Clock.System.now())
                call.respondTemplate(
                    "auth/register.ftl",
                    mapOf(
                        "locale" to getLocaleForCallUseCase(call),
                        "code" to payload
                    )
                )
            } catch (exception: ControllerException) {
                call.response.status(exception.code)
                call.respondTemplate(
                    "auth/register.ftl",
                    mapOf(
                        "locale" to getLocaleForCallUseCase(call),
                        "error" to translateUseCase(getLocaleForCallUseCase(call), exception.key)
                    )
                )
            }
        }
    }

    fun createPostRegisterCodeRoute(root: Route) {
        root.post("/{code}") {
            try {
                val code = call.parameters["code"]!!
                val payload = controller.register(code, Clock.System.now())
                val parameters = call.receiveParameters()
                val password = parameters["password"]?.takeIf { it.isNotBlank() }
                val firstName = parameters["first_name"]?.takeIf { it.isNotBlank() }
                val lastName = parameters["last_name"]?.takeIf { it.isNotBlank() }
                if (
                    password == null ||
                    firstName == null ||
                    lastName == null
                ) {
                    throw ControllerException(HttpStatusCode.BadRequest, "error_body_invalid")
                }
                controller.register(
                    RegisterCodePayload(
                        code,
                        payload.email,
                        payload.associationId,
                        password,
                        firstName,
                        lastName
                    ),
                    call
                )
                call.respondRedirect(call.request.queryParameters["redirect"] ?: "/")
            } catch (exception: ControllerException) {
                call.response.status(exception.code)
                call.respondTemplate(
                    "auth/register.ftl",
                    mapOf(
                        "locale" to getLocaleForCallUseCase(call),
                        "error" to translateUseCase(getLocaleForCallUseCase(call), exception.key)
                    )
                )
            }
        }
    }

    fun createGetJoinRoute(root: Route) {
        root.get {
            call.respondTemplate(
                "auth/join.ftl",
                mapOf("locale" to getLocaleForCallUseCase(call))
            )
        }
    }

    fun createPostJoinRoute(root: Route) {
        root.post {
            try {
                val parameters = call.receiveParameters()
                val email = parameters["email"] ?: throw ControllerException(
                    HttpStatusCode.BadRequest, "error_body_invalid"
                )
                controller.join(JoinPayload(email), Clock.System.now(), getLocaleForCallUseCase(call))
                call.respondTemplate(
                    "auth/join.ftl",
                    mapOf(
                        "locale" to getLocaleForCallUseCase(call),
                        "success" to translateUseCase(getLocaleForCallUseCase(call), "auth_join_email_sent")
                    )
                )
            } catch (exception: ControllerException) {
                call.response.status(exception.code)
                call.respondTemplate(
                    "auth/join.ftl",
                    mapOf(
                        "locale" to getLocaleForCallUseCase(call),
                        "error" to translateUseCase(getLocaleForCallUseCase(call), exception.key)
                    )
                )
            }
        }
    }

    fun createGetJoinCodeRoute(root: Route) {
        root.get("/{code}") {
            try {
                val code = call.parameters["code"]!!
                val payload = controller.join(code, Clock.System.now())
                call.respondTemplate(
                    "auth/join.ftl",
                    mapOf(
                        "locale" to getLocaleForCallUseCase(call),
                        "code" to payload
                    )
                )
            } catch (exception: ControllerException) {
                call.response.status(exception.code)
                call.respondTemplate(
                    "auth/join.ftl",
                    mapOf(
                        "locale" to getLocaleForCallUseCase(call),
                        "error" to translateUseCase(getLocaleForCallUseCase(call), exception.key)
                    )
                )
            }
        }
    }

    fun createPostJoinCodeRoute(root: Route) {
        root.post("/{code}") {
            try {
                val code = call.parameters["code"]!!
                val payload = controller.join(code, Clock.System.now())
                val parameters = call.receiveParameters()
                val name = parameters["name"]?.takeIf { it.isNotBlank() }
                val school = parameters["school"]?.takeIf { it.isNotBlank() }
                val city = parameters["city"]?.takeIf { it.isNotBlank() }
                val password = parameters["password"]?.takeIf { it.isNotBlank() }
                val firstName = parameters["first_name"]?.takeIf { it.isNotBlank() }
                val lastName = parameters["last_name"]?.takeIf { it.isNotBlank() }
                if (
                    name == null ||
                    school == null ||
                    city == null ||
                    password == null ||
                    firstName == null ||
                    lastName == null
                ) {
                    throw ControllerException(HttpStatusCode.BadRequest, "error_body_invalid")
                }
                controller.join(
                    JoinCodePayload(
                        code,
                        payload.email,
                        name,
                        school,
                        city,
                        password,
                        firstName,
                        lastName
                    )
                )
                call.respondTemplate(
                    "auth/join.ftl",
                    mapOf(
                        "locale" to getLocaleForCallUseCase(call),
                        "success" to translateUseCase(getLocaleForCallUseCase(call), "auth_join_submitted")
                    )
                )
            } catch (exception: ControllerException) {
                call.response.status(exception.code)
                call.respondTemplate(
                    "auth/join.ftl",
                    mapOf(
                        "locale" to getLocaleForCallUseCase(call),
                        "error" to translateUseCase(getLocaleForCallUseCase(call), exception.key)
                    )
                )
            }
        }
    }

}
