package me.nathanfallet.suitebde.controllers.auth

import com.github.aymanizz.ktori18n.locale
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.controllers.IRouter
import me.nathanfallet.suitebde.models.auth.*
import me.nathanfallet.suitebde.models.exceptions.ControllerException
import me.nathanfallet.suitebde.usecases.application.ITranslateUseCase

class AuthRouter(
    private val controller: IAuthController,
    private val translateUseCase: ITranslateUseCase
) : IRouter {

    override fun createRoutes(root: Route) {
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
            call.respond(
                FreeMarkerContent(
                    "auth/login.ftl",
                    mapOf("locale" to call.locale)
                )
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
                call.respondRedirect("/")
            } catch (exception: ControllerException) {
                call.response.status(exception.code)
                call.respond(
                    FreeMarkerContent(
                        "auth/login.ftl",
                        mapOf(
                            "locale" to call.locale,
                            "error" to translateUseCase(call.locale, exception.key)
                        )
                    )
                )
            }
        }
    }

    fun createGetRegisterRoute(root: Route) {
        root.get {
            call.respond(
                FreeMarkerContent(
                    "auth/register.ftl",
                    mapOf("locale" to call.locale)
                )
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
                controller.register(RegisterPayload(email), Clock.System.now(), call.locale, call)
                call.respond(
                    FreeMarkerContent(
                        "auth/register.ftl",
                        mapOf(
                            "locale" to call.locale,
                            "success" to translateUseCase(call.locale, "auth_register_email_sent")
                        )
                    )
                )
            } catch (exception: ControllerException) {
                call.response.status(exception.code)
                call.respond(
                    FreeMarkerContent(
                        "auth/register.ftl",
                        mapOf(
                            "locale" to call.locale,
                            "error" to translateUseCase(call.locale, exception.key)
                        )
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
                call.respond(
                    FreeMarkerContent(
                        "auth/register.ftl",
                        mapOf(
                            "locale" to call.locale,
                            "code" to payload
                        )
                    )
                )
            } catch (exception: ControllerException) {
                call.response.status(exception.code)
                call.respond(
                    FreeMarkerContent(
                        "auth/register.ftl",
                        mapOf(
                            "locale" to call.locale,
                            "error" to translateUseCase(call.locale, exception.key)
                        )
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
                    Clock.System.now(),
                    call
                )
                call.respondRedirect("/")
            } catch (exception: ControllerException) {
                call.response.status(exception.code)
                call.respond(
                    FreeMarkerContent(
                        "auth/register.ftl",
                        mapOf(
                            "locale" to call.locale,
                            "error" to translateUseCase(call.locale, exception.key)
                        )
                    )
                )
            }
        }
    }

    fun createGetJoinRoute(root: Route) {
        root.get {
            call.respond(
                FreeMarkerContent(
                    "auth/join.ftl",
                    mapOf("locale" to call.locale)
                )
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
                controller.join(JoinPayload(email), Clock.System.now(), call.locale)
                call.respond(
                    FreeMarkerContent(
                        "auth/join.ftl",
                        mapOf(
                            "locale" to call.locale,
                            "success" to translateUseCase(call.locale, "auth_join_email_sent")
                        )
                    )
                )
            } catch (exception: ControllerException) {
                call.response.status(exception.code)
                call.respond(
                    FreeMarkerContent(
                        "auth/join.ftl",
                        mapOf(
                            "locale" to call.locale,
                            "error" to translateUseCase(call.locale, exception.key)
                        )
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
                call.respond(
                    FreeMarkerContent(
                        "auth/join.ftl",
                        mapOf(
                            "locale" to call.locale,
                            "code" to payload
                        )
                    )
                )
            } catch (exception: ControllerException) {
                call.response.status(exception.code)
                call.respond(
                    FreeMarkerContent(
                        "auth/join.ftl",
                        mapOf(
                            "locale" to call.locale,
                            "error" to translateUseCase(call.locale, exception.key)
                        )
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
                    ),
                    Clock.System.now()
                )
                call.respond(
                    FreeMarkerContent(
                        "auth/join.ftl",
                        mapOf(
                            "locale" to call.locale,
                            "success" to translateUseCase(call.locale, "auth_join_submitted")
                        )
                    )
                )
            } catch (exception: ControllerException) {
                call.response.status(exception.code)
                call.respond(
                    FreeMarkerContent(
                        "auth/join.ftl",
                        mapOf(
                            "locale" to call.locale,
                            "error" to translateUseCase(call.locale, exception.key)
                        )
                    )
                )
            }
        }
    }

}