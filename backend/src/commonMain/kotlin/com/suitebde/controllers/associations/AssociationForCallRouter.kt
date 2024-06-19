package com.suitebde.controllers.associations

import com.suitebde.models.associations.Association
import com.suitebde.models.associations.CreateAssociationPayload
import com.suitebde.models.associations.UpdateAssociationPayload
import com.suitebde.usecases.associations.IRequireAssociationForCallUseCase
import dev.kaccelero.models.UUID
import dev.kaccelero.routers.AbstractModelRouter
import dev.kaccelero.routers.ControllerRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.util.reflect.*
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.parameters.Parameter
import kotlin.reflect.KClass

class AssociationForCallRouter(
    private val requireAssociationForCallUseCase: IRequireAssociationForCallUseCase,
    controller: IAssociationsController,
) : AbstractModelRouter<Association, UUID, CreateAssociationPayload, UpdateAssociationPayload>(
    typeInfo<Association>(),
    typeInfo<CreateAssociationPayload>(),
    typeInfo<UpdateAssociationPayload>(),
    controller,
    IAssociationsController::class,
    "",
    "",
    ""
), IAssociationForCallRouter {

    override suspend fun get(call: ApplicationCall): Association = requireAssociationForCallUseCase(call)

    override suspend fun <Payload : Any> decodePayload(call: ApplicationCall, type: KClass<Payload>): Payload =
        throw UnsupportedOperationException()


    override fun createControllerRoute(root: Route, controllerRoute: ControllerRoute, openAPI: OpenAPI?) = Unit
    override fun getOpenAPIParameters(self: Boolean): List<Parameter> = emptyList()

}
