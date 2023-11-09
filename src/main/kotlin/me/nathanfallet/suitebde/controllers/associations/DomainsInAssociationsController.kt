package me.nathanfallet.suitebde.controllers.associations

import io.ktor.server.application.*
import me.nathanfallet.ktor.routers.controllers.base.IChildModelController
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.DomainInAssociation

class DomainsInAssociationsController(

) : IChildModelController<DomainInAssociation, String, String, Unit, Association, String> {

    override suspend fun create(call: ApplicationCall, parent: Association, payload: String): DomainInAssociation {
        TODO("Not yet implemented")
    }

    override suspend fun delete(call: ApplicationCall, parent: Association, id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun get(call: ApplicationCall, parent: Association, id: String): DomainInAssociation {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(call: ApplicationCall, parent: Association): List<DomainInAssociation> {
        TODO("Not yet implemented")
    }

    override suspend fun update(
        call: ApplicationCall,
        parent: Association,
        id: String,
        payload: Unit
    ): DomainInAssociation {
        TODO("Not yet implemented")
    }

}
