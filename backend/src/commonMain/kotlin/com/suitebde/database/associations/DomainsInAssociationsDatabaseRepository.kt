package com.suitebde.database.associations

import com.suitebde.models.associations.CreateDomainInAssociationPayload
import com.suitebde.models.associations.DomainInAssociation
import com.suitebde.repositories.associations.IDomainsInAssociationsRepository
import dev.kaccelero.database.IDatabase
import dev.kaccelero.database.eq
import dev.kaccelero.database.set
import dev.kaccelero.models.IContext
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DomainsInAssociationsDatabaseRepository(
    private val database: IDatabase,
) : IDomainsInAssociationsRepository {

    init {
        database.transaction {
            SchemaUtils.create(DomainsInAssociations)
        }
    }

    override suspend fun list(parentId: UUID, context: IContext?): List<DomainInAssociation> =
        database.suspendedTransaction {
            DomainsInAssociations
                .selectAll()
                .where { DomainsInAssociations.associationId eq parentId }
                .map(DomainsInAssociations::toDomainInAssociation)
        }

    override suspend fun list(
        pagination: Pagination,
        parentId: UUID,
        context: IContext?,
    ): List<DomainInAssociation> =
        database.suspendedTransaction {
            DomainsInAssociations
                .selectAll()
                .where { DomainsInAssociations.associationId eq parentId }
                .limit(pagination.limit.toInt(), pagination.offset)
                .map(DomainsInAssociations::toDomainInAssociation)
        }

    override suspend fun create(
        payload: CreateDomainInAssociationPayload,
        parentId: UUID,
        context: IContext?,
    ): DomainInAssociation? =
        database.suspendedTransaction {
            DomainsInAssociations.insert {
                it[domain] = payload.domain
                it[associationId] = parentId
            }.resultedValues?.map(DomainsInAssociations::toDomainInAssociation)?.singleOrNull()
        }

    override suspend fun delete(id: String, parentId: UUID, context: IContext?): Boolean =
        database.suspendedTransaction {
            DomainsInAssociations.deleteWhere {
                domain eq id and (associationId eq parentId)
            }
        } == 1

    override suspend fun get(id: String, parentId: UUID, context: IContext?): DomainInAssociation? =
        database.suspendedTransaction {
            DomainsInAssociations
                .selectAll()
                .where { DomainsInAssociations.domain eq id and (DomainsInAssociations.associationId eq parentId) }
                .map(DomainsInAssociations::toDomainInAssociation)
                .firstOrNull()
        }

}
