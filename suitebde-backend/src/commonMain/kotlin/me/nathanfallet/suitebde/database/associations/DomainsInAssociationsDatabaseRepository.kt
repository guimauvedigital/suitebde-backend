package me.nathanfallet.suitebde.database.associations

import me.nathanfallet.suitebde.models.associations.CreateDomainInAssociationPayload
import me.nathanfallet.suitebde.models.associations.DomainInAssociation
import me.nathanfallet.suitebde.repositories.associations.IDomainsInAssociationsRepository
import me.nathanfallet.surexposed.database.IDatabase
import me.nathanfallet.usecases.context.IContext
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

    override suspend fun list(parentId: String, context: IContext?): List<DomainInAssociation> =
        database.suspendedTransaction {
            DomainsInAssociations
                .selectAll()
                .where { DomainsInAssociations.associationId eq parentId }
                .map(DomainsInAssociations::toDomainInAssociation)
        }

    override suspend fun list(
        limit: Long,
        offset: Long,
        parentId: String,
        context: IContext?,
    ): List<DomainInAssociation> =
        database.suspendedTransaction {
            DomainsInAssociations
                .selectAll()
                .where { DomainsInAssociations.associationId eq parentId }
                .limit(limit.toInt(), offset)
                .map(DomainsInAssociations::toDomainInAssociation)
        }

    override suspend fun create(
        payload: CreateDomainInAssociationPayload,
        parentId: String,
        context: IContext?,
    ): DomainInAssociation? =
        database.suspendedTransaction {
            DomainsInAssociations.insert {
                it[domain] = payload.domain
                it[associationId] = parentId
            }.resultedValues?.map(DomainsInAssociations::toDomainInAssociation)?.singleOrNull()
        }

    override suspend fun delete(id: String, parentId: String, context: IContext?): Boolean =
        database.suspendedTransaction {
            DomainsInAssociations.deleteWhere {
                domain eq id and (associationId eq parentId)
            }
        } == 1

    override suspend fun get(id: String, parentId: String, context: IContext?): DomainInAssociation? =
        database.suspendedTransaction {
            DomainsInAssociations
                .selectAll()
                .where { DomainsInAssociations.domain eq id and (DomainsInAssociations.associationId eq parentId) }
                .map(DomainsInAssociations::toDomainInAssociation)
                .firstOrNull()
        }

}
