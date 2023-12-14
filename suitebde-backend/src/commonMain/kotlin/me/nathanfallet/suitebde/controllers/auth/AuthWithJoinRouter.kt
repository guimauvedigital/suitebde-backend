package me.nathanfallet.suitebde.controllers.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.swagger.v3.oas.models.OpenAPI
import me.nathanfallet.ktorx.models.auth.AuthMapping
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.routers.auth.LocalizedAuthWithCodeTemplateRouter
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.models.auth.*

class AuthWithJoinRouter(
    override val controller: IAuthController,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
) : LocalizedAuthWithCodeTemplateRouter<LoginPayload, RegisterPayload, RegisterCodePayload>(
    LoginPayload::class,
    RegisterPayload::class,
    RegisterCodePayload::class,
    AuthMapping(
        loginTemplate = "auth/login.ftl",
        registerTemplate = "auth/register.ftl",
        authorizeTemplate = "auth/authorize.ftl",
        redirectTemplate = "auth/redirect.ftl",
        redirectUnauthorizedToUrl = "/auth/login?redirect={path}",
    ),
    { template, model -> respondTemplate(template, model) },
    controller,
    getLocaleForCallUseCase
) {

    override fun createLocalizedRoutes(root: Route, openAPI: OpenAPI?) {
        super.createLocalizedRoutes(root, openAPI)
        createGetJoinRoute(root)
        createPostJoinRoute(root)
        createGetJoinCodeRoute(root)
        createPostJoinCodeRoute(root)
    }

    private fun createGetJoinRoute(root: Route) {
        root.get("$fullRoute/join") {
            call.respondTemplate(
                "auth/join.ftl",
                mapOf()
            )
        }
    }

    private fun createPostJoinRoute(root: Route) {
        root.post("$fullRoute/join") {
            try {
                val parameters = call.receiveParameters()
                val email = parameters["email"] ?: throw ControllerException(
                    HttpStatusCode.BadRequest, "error_body_invalid"
                )
                controller.join(call, JoinPayload(email))
                call.respondTemplate(
                    "auth/join.ftl",
                    mapOf("success" to "auth_join_email_sent")
                )
            } catch (exception: ControllerException) {
                handleExceptionTemplate(exception, call, "auth/join.ftl")
            }
        }
    }

    private fun createGetJoinCodeRoute(root: Route) {
        root.get("$fullRoute/join/{code}") {
            try {
                val code = call.parameters["code"]!!
                val payload = controller.join(call, code)
                call.respondTemplate(
                    "auth/join.ftl",
                    mapOf("codePayload" to payload)
                )
            } catch (exception: ControllerException) {
                handleExceptionTemplate(exception, call, "auth/join.ftl")
            }
        }
    }

    private fun createPostJoinCodeRoute(root: Route) {
        root.post("$fullRoute/join/{code}") {
            try {
                val code = call.parameters["code"]!!
                val payload = controller.join(call, code)
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
                    call,
                    code,
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
                    mapOf("success" to "auth_join_submitted")
                )
            } catch (exception: ControllerException) {
                handleExceptionTemplate(exception, call, "auth/join.ftl")
            }
        }
    }

}
