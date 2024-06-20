package com.suitebde.database.associations

import com.suitebde.models.associations.Association
import com.suitebde.models.associations.CreateAssociationPayload
import com.suitebde.models.associations.UpdateAssociationPayload
import com.suitebde.repositories.associations.IAssociationsRepository
import dev.kaccelero.database.IDatabase
import dev.kaccelero.database.eq
import dev.kaccelero.models.IContext
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination
import kotlinx.datetime.*
import org.jetbrains.exposed.sql.*

class AssociationsDatabaseRepository(
    private val database: IDatabase,
) : IAssociationsRepository {

    init {
        database.transaction {
            SchemaUtils.create(Associations)
            SchemaUtils.create(DomainsInAssociations)
        }
    }

    override suspend fun create(payload: CreateAssociationPayload, context: IContext?): Association? {
        val createdAt = Clock.System.now()
        val expiresAt = createdAt.plus(1, DateTimeUnit.MONTH, TimeZone.currentSystemDefault())
        return database.suspendedTransaction {
            Associations.insert {
                it[name] = payload.name
                it[school] = payload.school
                it[city] = payload.city
                it[validated] = false
                it[Associations.createdAt] = createdAt
                it[Associations.expiresAt] = expiresAt
            }.resultedValues?.map(Associations::toAssociation)?.singleOrNull()
        }
    }

    override suspend fun update(id: UUID, payload: UpdateAssociationPayload, context: IContext?): Boolean =
        database.suspendedTransaction {
            Associations.update({ Associations.id eq id }) {
                it[name] = payload.name
                it[school] = payload.school
                it[city] = payload.city
                it[validated] = payload.validated
            }
        } == 1

    override suspend fun updateExpiresAt(id: UUID, expiresAt: Instant): Boolean =
        database.suspendedTransaction {
            Associations.update({ Associations.id eq id }) {
                it[Associations.expiresAt] = expiresAt
            }
        } == 1

    override suspend fun delete(id: UUID, context: IContext?): Boolean =
        database.suspendedTransaction {
            Associations.deleteWhere {
                Associations.id eq id
            }
        } == 1

    override suspend fun get(id: UUID, context: IContext?): Association? =
        database.suspendedTransaction {
            Associations
                .selectAll()
                .where { Associations.id eq id }
                .map(Associations::toAssociation)
                .singleOrNull()
        }

    override suspend fun list(context: IContext?): List<Association> =
        database.suspendedTransaction {
            Associations
                .selectAll()
                .map(Associations::toAssociation)
        }

    override suspend fun list(pagination: Pagination, context: IContext?): List<Association> =
        database.suspendedTransaction {
            Associations
                .selectAll()
                .limit(pagination.limit.toInt(), pagination.offset)
                .map(Associations::toAssociation)
        }

    override suspend fun getValidatedAssociations(): List<Association> =
        database.suspendedTransaction {
            Associations
                .selectAll()
                .where { Associations.validated eq true }
                .map(Associations::toAssociation)
        }

    override suspend fun getAssociationsExpiringBefore(date: Instant): List<Association> =
        database.suspendedTransaction {
            Associations
                .selectAll()
                .where { Associations.expiresAt less date }
                .map(Associations::toAssociation)
        }

    override suspend fun getAssociationForDomain(domain: String): Association? =
        database.suspendedTransaction {
            DomainsInAssociations
                .join(Associations, JoinType.INNER, DomainsInAssociations.associationId, Associations.id)
                .selectAll()
                .where { DomainsInAssociations.domain eq domain }
                .map(Associations::toAssociation)
                .singleOrNull()
        }

}
