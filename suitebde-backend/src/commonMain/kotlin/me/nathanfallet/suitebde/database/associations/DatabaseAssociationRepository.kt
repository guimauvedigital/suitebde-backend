package me.nathanfallet.suitebde.database.associations

import kotlinx.datetime.*
import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.associations.UpdateAssociationPayload
import me.nathanfallet.suitebde.repositories.associations.IAssociationsRepository
import me.nathanfallet.usecases.users.IUser
import org.jetbrains.exposed.sql.*

class DatabaseAssociationRepository(
    private val database: Database
) : IAssociationsRepository {

    override suspend fun create(payload: CreateAssociationPayload, user: IUser?): Association? {
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

    override suspend fun update(id: String, payload: UpdateAssociationPayload, user: IUser?): Boolean {
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

    override suspend fun delete(id: String): Boolean {
        return database.dbQuery {
            Associations.deleteWhere {
                Op.build { Associations.id eq id }
            }
        } == 1
    }

    override suspend fun get(id: String): Association? {
        return database.dbQuery {
            Associations
                .select { Associations.id eq id }
                .map(Associations::toAssociation)
                .singleOrNull()
        }
    }

    override suspend fun list(): List<Association> {
        return database.dbQuery {
            Associations
                .selectAll()
                .map(Associations::toAssociation)
        }
    }

    override suspend fun list(limit: Long, offset: Long): List<Association> {
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
                .select { Associations.validated eq true }
                .map(Associations::toAssociation)
        }
    }

    override suspend fun getAssociationsExpiringBefore(date: Instant): List<Association> {
        return database.dbQuery {
            Associations
                .select { Associations.expiresAt less date.toString() }
                .map(Associations::toAssociation)
        }
    }

    override suspend fun getAssociationForDomain(domain: String): Association? {
        return database.dbQuery {
            DomainsInAssociations
                .join(Associations, JoinType.INNER, DomainsInAssociations.associationId, Associations.id)
                .select { DomainsInAssociations.domain eq domain }
                .map(Associations::toAssociation)
                .singleOrNull()
        }
    }

    override suspend fun getCodeInEmail(code: String): CodeInEmail? {
        return database.dbQuery {
            CodesInEmails
                .select { CodesInEmails.code eq code }
                .map(CodesInEmails::toCodeInEmail)
                .singleOrNull()
        }
    }

    override suspend fun getCodesInEmailsExpiringBefore(date: Instant): List<CodeInEmail> {
        return database.dbQuery {
            CodesInEmails
                .select { CodesInEmails.expiresAt less date.toString() }
                .map(CodesInEmails::toCodeInEmail)
        }
    }

    override suspend fun createCodeInEmail(
        email: String,
        code: String,
        associationId: String?,
        expiresAt: Instant
    ): CodeInEmail? {
        return database.dbQuery {
            CodesInEmails.insert {
                it[this.email] = email
                it[this.code] = code
                it[this.associationId] = associationId
                it[this.expiresAt] = expiresAt.toString()
            }.resultedValues?.map(CodesInEmails::toCodeInEmail)?.singleOrNull()
        }
    }

    override suspend fun updateCodeInEmail(
        email: String,
        code: String,
        associationId: String?,
        expiresAt: Instant
    ): Int {
        return database.dbQuery {
            CodesInEmails.update({ CodesInEmails.email eq email }) {
                it[this.code] = code
                it[this.associationId] = associationId
                it[this.expiresAt] = expiresAt.toString()
            }
        }
    }

    override suspend fun deleteCodeInEmail(code: String) {
        database.dbQuery {
            CodesInEmails.deleteWhere {
                Op.build { CodesInEmails.code eq code }
            }
        }
    }

}
