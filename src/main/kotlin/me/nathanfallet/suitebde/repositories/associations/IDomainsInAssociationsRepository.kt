package me.nathanfallet.suitebde.repositories.associations

import me.nathanfallet.suitebde.models.associations.DomainInAssociation
import me.nathanfallet.usecases.models.repositories.IChildModelSuspendRepository

interface IDomainsInAssociationsRepository :
    IChildModelSuspendRepository<DomainInAssociation, String, String, Unit, String> {

    suspend fun getDomains(associationId: String): List<DomainInAssociation>

}
