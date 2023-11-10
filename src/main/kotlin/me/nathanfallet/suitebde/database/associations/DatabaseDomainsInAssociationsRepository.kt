package me.nathanfallet.suitebde.database.associations

import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.models.associations.CreateDomainInAssociationPayload
import me.nathanfallet.suitebde.models.associations.DomainInAssociation
import me.nathanfallet.suitebde.repositories.associations.IDomainsInAssociationsRepository
import org.jetbrains.exposed.sql.*

class DatabaseDomainsInAssociationsRepository(
    private val database: Database
) : IDomainsInAssociationsRepository {

    override suspend fun getDomains(associationId: String): List<DomainInAssociation> {
        return database.dbQuery {
            DomainsInAssociations
                .select { DomainsInAssociations.associationId eq associationId }
                .map(DomainsInAssociations::toDomainInAssociation)
        }
    }

    override suspend fun create(payload: CreateDomainInAssociationPayload, parentId: String): DomainInAssociation? {
        return database.dbQuery {
            DomainsInAssociations.insert {
                it[domain] = payload.domain
                it[associationId] = parentId
            }.resultedValues?.map(DomainsInAssociations::toDomainInAssociation)?.singleOrNull()
        }
    }

    override suspend fun delete(id: String, parentId: String): Boolean {
        return database.dbQuery {
            DomainsInAssociations.deleteWhere {
                Op.build { domain eq id and (associationId eq parentId) }
            }
        } == 1
    }

    override suspend fun get(id: String, parentId: String): DomainInAssociation? {
        return database.dbQuery {
            DomainsInAssociations
                .select { DomainsInAssociations.domain eq id and (DomainsInAssociations.associationId eq parentId) }
                .map(DomainsInAssociations::toDomainInAssociation)
                .firstOrNull()
        }
    }

    override suspend fun update(id: String, payload: Unit, parentId: String): Boolean {
        TODO("Not yet implemented")
    }

}
