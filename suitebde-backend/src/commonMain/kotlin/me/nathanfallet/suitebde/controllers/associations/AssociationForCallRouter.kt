package me.nathanfallet.suitebde.controllers.associations

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.util.reflect.*
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.parameters.Parameter
import me.nathanfallet.ktorx.controllers.IModelController
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.associations.UpdateAssociationPayload
import me.nathanfallet.suitebde.usecases.associations.IRequireAssociationForCallUseCase

class AssociationForCallRouter(
    private val requireAssociationForCallUseCase: IRequireAssociationForCallUseCase,
    override val controller: IModelController<Association, String, CreateAssociationPayload, UpdateAssociationPayload>,
) : IAssociationForCallRouter {

    override val modelTypeInfo = typeInfo<Association>()
    override val createPayloadTypeInfo = typeInfo<CreateAssociationPayload>()
    override val updatePayloadTypeInfo = typeInfo<UpdateAssociationPayload>()
    override val listTypeInfo = typeInfo<List<Association>>()
    override val parentRouter = null

    override val route = ""
    override val id = ""
    override val prefix = ""
    override val routeIncludingParent = ""

    override fun createRoutes(root: Route, openAPI: OpenAPI?) {
        // This does not create routes because it only resolves the association from the call
    }

    override suspend fun get(call: ApplicationCall): Association {
        return requireAssociationForCallUseCase(call)
    }

    override fun getOpenAPIParameters(self: Boolean): List<Parameter> {
        return emptyList()
    }

}
