package me.nathanfallet.suitebde.controllers.auth

import io.ktor.server.application.*
import me.nathanfallet.ktorx.controllers.auth.IAuthWithCodeController
import me.nathanfallet.ktorx.models.annotations.*
import me.nathanfallet.ktorx.models.auth.ClientForUser
import me.nathanfallet.suitebde.models.auth.*
import me.nathanfallet.usecases.auth.AuthRequest
import me.nathanfallet.usecases.auth.AuthToken

interface IAuthController : IAuthWithCodeController<LoginPayload, RegisterPayload, RegisterCodePayload> {

    @TemplateMapping("auth/login.ftl")
    @LoginPath
    suspend fun login(call: ApplicationCall, @Payload payload: LoginPayload)

    @TemplateMapping("auth/register.ftl")
    @RegisterPath
    suspend fun register(call: ApplicationCall, @Payload payload: RegisterPayload)

    @TemplateMapping("auth/register.ftl")
    @RegisterCodePath
    suspend fun register(call: ApplicationCall, code: String): RegisterPayload

    @TemplateMapping("auth/register.ftl")
    @RegisterCodeRedirectPath
    suspend fun register(call: ApplicationCall, code: String, @Payload payload: RegisterCodePayload)

    @TemplateMapping("auth/authorize.ftl")
    @AuthorizePath
    suspend fun authorize(call: ApplicationCall, clientId: String?): ClientForUser

    @TemplateMapping("auth/authorize.ftl")
    @AuthorizeRedirectPath
    suspend fun authorize(call: ApplicationCall, client: ClientForUser): String

    @APIMapping("createToken", "Create a token")
    @CreateModelPath("/token")
    @DocumentedError(400, "auth_invalid_code")
    @DocumentedError(500, "error_internal")
    suspend fun token(call: ApplicationCall, @Payload request: AuthRequest): AuthToken

    suspend fun join(call: ApplicationCall, payload: JoinPayload)
    suspend fun join(call: ApplicationCall, code: String): JoinPayload
    suspend fun join(call: ApplicationCall, code: String, payload: JoinCodePayload)

}
