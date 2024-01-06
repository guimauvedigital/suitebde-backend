package me.nathanfallet.suitebde.database.associations

import kotlinx.datetime.*
import me.nathanfallet.ktorx.database.IDatabase
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.associations.UpdateAssociationPayload
import me.nathanfallet.suitebde.repositories.associations.IAssociationsRepository
import me.nathanfallet.usecases.context.IContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class AssociationsDatabaseRepository(
    private val database: IDatabase,
) : IAssociationsRepository {

    override suspend fun create(payload: CreateAssociationPayload, context: IContext?): Association? {
        val createdAt = Clock.System.now()
        val expiresAt = createdAt.plus(1, DateTimeUnit.MONTH, TimeZone.currentSystemDefault())
        return database.dbQuery {
            Associations.insert {
                it[id] = generateId()
                it[name] = payload.name
                it[school] = payload.school
                it[city] = payload.city
                it[validated] = false
                it[Associations.createdAt] = createdAt.toString()
                it[Associations.expiresAt] = expiresAt.toString()
            }.resultedValues?.map(Associations::toAssociation)?.singleOrNull()
        }
    }

    override suspend fun update(id: String, payload: UpdateAssociationPayload, context: IContext?): Boolean {
        return database.dbQuery {
            Associations.update({ Associations.id eq id }) {
                it[name] = payload.name
                it[school] = payload.school
                it[city] = payload.city
                it[validated] = payload.validated
            }
        } == 1
    }

    override suspend fun updateExpiresAt(id: String, expiresAt: Instant): Boolean {
        return database.dbQuery {
            Associations.update({ Associations.id eq id }) {
                it[Associations.expiresAt] = expiresAt.toString()
            }
        } == 1
    }

    override suspend fun delete(id: String, context: IContext?): Boolean {
        return database.dbQuery {
            Associations.deleteWhere {
                Associations.id eq id
            }
        } == 1
    }

    override suspend fun get(id: String, context: IContext?): Association? {
        return database.dbQuery {
            Associations
                .selectAll()
                .where { Associations.id eq id }
                .map(Associations::toAssociation)
                .singleOrNull()
        }
    }

    override suspend fun list(context: IContext?): List<Association> {
        return database.dbQuery {
            Associations
                .selectAll()
                .map(Associations::toAssociation)
        }
    }

    override suspend fun list(limit: Long, offset: Long, context: IContext?): List<Association> {
        return database.dbQuery {
            Associations
                .selectAll()
                .limit(limit.toInt(), offset)
                .map(Associations::toAssociation)
        }
    }

    override suspend fun getValidatedAssociations(): List<Association> {
        return database.dbQuery {
            Associations
                .selectAll()
                .where { Associations.validated eq true }
                .map(Associations::toAssociation)
        }
    }

    override suspend fun getAssociationsExpiringBefore(date: Instant): List<Association> {
        return database.dbQuery {
            Associations
                .selectAll()
                .where { Associations.expiresAt less date.toString() }
                .map(Associations::toAssociation)
        }
    }

    override suspend fun getAssociationForDomain(domain: String): Association? {
        return database.dbQuery {
            DomainsInAssociations
                .join(Associations, JoinType.INNER, DomainsInAssociations.associationId, Associations.id)
                .selectAll()
                .where { DomainsInAssociations.domain eq domain }
                .map(Associations::toAssociation)
                .singleOrNull()
        }
    }

}
