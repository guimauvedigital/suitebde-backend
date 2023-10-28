package me.nathanfallet.suitebde.controllers.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.controllers.IRouter
import me.nathanfallet.suitebde.models.LocalizedString
import me.nathanfallet.suitebde.models.auth.JoinCodePayload
import me.nathanfallet.suitebde.models.auth.JoinPayload
import me.nathanfallet.suitebde.models.auth.LoginPayload
import me.nathanfallet.suitebde.models.exceptions.ControllerException

class AuthRouter(
    private val controller: IAuthController
) : IRouter {

    override fun createRoutes(root: Route) {
        root.route("/auth") {
            route("/login") {
                createGetLoginRoute(this)
                createPostLoginRoute(this)
            }
            route("/register") {
                createGetRegisterRoute(this)
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
                    null
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
                    throw ControllerException(HttpStatusCode.BadRequest, LocalizedString.ERROR_BODY_INVALID)
                }
                val user = controller.login(
                    LoginPayload(
                        email,
                        password
                    )
                )
                call.respond(HttpStatusCode.OK)
            } catch (exception: ControllerException) {
                call.response.status(exception.code)
                call.respond(
                    FreeMarkerContent(
                        "auth/login.ftl",
                        mapOf("error" to exception.message)
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
                    null
                )
            )
        }
    }

    fun createGetJoinRoute(root: Route) {
        root.get {
            call.respond(
                FreeMarkerContent(
                    "auth/join.ftl",
                    null
                )
            )
        }
    }

    fun createPostJoinRoute(root: Route) {
        root.post {
            try {
                val parameters = call.receiveParameters()
                val email = parameters["email"] ?: throw ControllerException(
                    HttpStatusCode.BadRequest, LocalizedString.ERROR_BODY_INVALID
                )
                controller.join(JoinPayload(email), Clock.System.now())
                call.respond(
                    FreeMarkerContent(
                        "auth/join.ftl",
                        mapOf("success" to LocalizedString.AUTH_JOIN_EMAIL_SENT.value)
                    )
                )
            } catch (exception: ControllerException) {
                call.response.status(exception.code)
                call.respond(
                    FreeMarkerContent(
                        "auth/join.ftl",
                        mapOf("error" to exception.message)
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
                        mapOf("code" to payload)
                    )
                )
            } catch (exception: ControllerException) {
                call.response.status(exception.code)
                call.respond(
                    FreeMarkerContent(
                        "auth/join.ftl",
                        mapOf("error" to exception.message)
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
                    throw ControllerException(HttpStatusCode.BadRequest, LocalizedString.ERROR_BODY_INVALID)
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
                        mapOf("success" to LocalizedString.AUTH_JOIN_SUBMITTED.value)
                    )
                )
            } catch (exception: ControllerException) {
                call.response.status(exception.code)
                call.respond(
                    FreeMarkerContent(
                        "auth/join.ftl",
                        mapOf("error" to exception.message)
                    )
                )
            }
        }
    }
}