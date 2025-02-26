package com.suitebde.controllers.auth

import com.suitebde.models.associations.Association
import com.suitebde.models.auth.*
import com.suitebde.models.users.ResetInUser
import dev.kaccelero.annotations.*
import dev.kaccelero.commons.responses.RedirectResponse
import dev.kaccelero.controllers.IUnitController
import dev.kaccelero.models.UUID
import io.ktor.server.application.*

interface IAuthController : IUnitController {

    @TemplateMapping("auth/login.ftl")
    @Path("GET", "/login")
    fun login()

    @TemplateMapping("auth/login.ftl")
    @Path("POST", "/login")
    suspend fun login(
        call: ApplicationCall,
        @Payload payload: LoginPayload,
        @QueryParameter redirect: String?,
    ): RedirectResponse

    @TemplateMapping("auth/login.ftl")
    @Path("GET", "/logout")
    suspend fun logout(
        call: ApplicationCall,
        @QueryParameter redirect: String?,
    ): RedirectResponse

    @TemplateMapping("auth/login.ftl")
    @Path("GET", "/profile")
    suspend fun profile(call: ApplicationCall): RedirectResponse

    @TemplateMapping("auth/associations.ftl")
    @Path("GET", "/associations")
    suspend fun associations(): List<Association>

    @TemplateMapping("auth/register.ftl")
    @Path("GET", "/register")
    suspend fun register(call: ApplicationCall, @QueryParameter associationId: UUID?): Association

    @TemplateMapping("auth/register.ftl")
    @Path("POST", "/register")
    suspend fun register(
        call: ApplicationCall,
        @Payload payload: RegisterPayload,
        @QueryParameter associationId: UUID?,
    ): Map<String, Any>

    @TemplateMapping("auth/register.ftl")
    @Path("GET", "/register/{code}")
    suspend fun registerCode(call: ApplicationCall, @PathParameter code: String): RegisterPayload

    @TemplateMapping("auth/register.ftl")
    @Path("POST", "/register/{code}")
    suspend fun registerCode(
        call: ApplicationCall,
        @PathParameter code: String,
        @Payload payload: RegisterCodePayload,
        @QueryParameter redirect: String?,
    ): RedirectResponse

    @TemplateMapping("auth/authorize.ftl")
    @Path("GET", "/authorize")
    suspend fun authorize(call: ApplicationCall, @QueryParameter clientId: UUID?): ClientForUser

    @TemplateMapping("auth/redirect.ftl")
    @Path("POST", "/authorize")
    suspend fun authorizeRedirect(call: ApplicationCall, @QueryParameter clientId: UUID?): Map<String, Any>

    @TemplateMapping("auth/join.ftl")
    @Path("GET", "/join")
    fun join()

    @TemplateMapping("auth/join.ftl")
    @Path("POST", "/join")
    suspend fun join(call: ApplicationCall, @Payload payload: JoinPayload): Map<String, Any>

    @TemplateMapping("auth/join.ftl")
    @Path("GET", "/join/{code}")
    suspend fun joinCode(@PathParameter code: String): JoinPayload

    @TemplateMapping("auth/join.ftl")
    @Path("POST", "/join/{code}")
    suspend fun joinCode(@PathParameter code: String, @Payload payload: JoinCodePayload): Map<String, Any>

    @TemplateMapping("auth/reset.ftl")
    @Path("GET", "/reset")
    suspend fun reset()

    @TemplateMapping("auth/reset.ftl")
    @Path("POST", "/reset")
    suspend fun reset(call: ApplicationCall, @Payload payload: RegisterPayload): Map<String, Any>

    @TemplateMapping("auth/reset.ftl")
    @Path("GET", "/reset/{code}")
    suspend fun resetCode(call: ApplicationCall, @PathParameter code: String): ResetInUser

    @TemplateMapping("auth/reset.ftl")
    @Path("POST", "/reset/{code}")
    suspend fun resetCode(
        call: ApplicationCall,
        @PathParameter code: String,
        @Payload payload: ResetPasswordPayload,
    ): RedirectResponse

    @APIMapping("createToken")
    @Path("POST", "/token")
    @DocumentedTag("Auth")
    @DocumentedError(400, "auth_invalid_code")
    @DocumentedError(500, "error_internal")
    suspend fun token(@Payload payload: AuthRequest): AuthToken

    @APIMapping("refreshToken")
    @Path("POST", "/refresh")
    @DocumentedTag("Auth")
    @DocumentedError(400, "auth_invalid_credentials")
    suspend fun refreshToken(@Payload payload: RefreshTokenPayload): AuthToken

}
