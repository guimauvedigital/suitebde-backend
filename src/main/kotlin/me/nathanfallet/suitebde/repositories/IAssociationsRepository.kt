package me.nathanfallet.suitebde.repositories

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.DomainInAssociation

interface IAssociationsRepository {

    suspend fun createAssociation(name: String): Association?
    suspend fun updateAssociation(association: Association)
    suspend fun deleteAssociation(association: Association)
    suspend fun getAssociation(id: String): Association?
    suspend fun getAssociations(): List<Association>
    suspend fun getAssociationForDomain(domain: String): Association?

    suspend fun createDomain(domain: String, associationId: String): DomainInAssociation?
    suspend fun deleteDomain(domain: String)
    suspend fun getDomains(associationId: String): List<DomainInAssociation>

}