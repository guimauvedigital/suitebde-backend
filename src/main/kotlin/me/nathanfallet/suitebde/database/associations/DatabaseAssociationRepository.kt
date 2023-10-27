package me.nathanfallet.suitebde.database.associations

import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.suitebde.models.associations.DomainInAssociation
import me.nathanfallet.suitebde.repositories.IAssociationsRepository
import org.jetbrains.exposed.sql.*

class DatabaseAssociationRepository(
    private val database: Database
) : IAssociationsRepository {

    override suspend fun createAssociation(
        name: String,
        school: String,
        city: String,
        validated: Boolean,
        createdAt: Instant,
        expiresAt: Instant
    ): Association? {
        return database.dbQuery {
            Associations.insert {
                it[id] = generateId()
                it[this.name] = name
                it[this.school] = school
                it[this.city] = city
                it[this.validated] = validated
                it[this.createdAt] = createdAt.toString()
                it[this.expiresAt] = expiresAt.toString()
            }.resultedValues?.map(Associations::toAssociation)?.singleOrNull()
        }
    }

    override suspend fun updateAssociation(association: Association) {
        database.dbQuery {
            Associations.update({ Associations.id eq association.id }) {
                it[name] = association.name
                it[school] = association.school
                it[city] = association.city
                it[validated] = association.validated
                it[expiresAt] = association.expiresAt.toString()
            }
        }
    }

    override suspend fun deleteAssociation(association: Association) {
        database.dbQuery {
            Associations.deleteWhere {
                Op.build { id eq association.id }
            }
        }
    }

    override suspend fun getAssociation(id: String): Association? {
        return database.dbQuery {
            Associations
                .select { Associations.id eq id }
                .map(Associations::toAssociation)
                .singleOrNull()
        }
    }

    override suspend fun getAssociations(): List<Association> {
        return database.dbQuery {
            Associations
                .selectAll()
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

    override suspend fun getAssociationForDomain(domain: String): Association? {
        return database.dbQuery {
            DomainsInAssociations
                .join(Associations, JoinType.INNER, DomainsInAssociations.associationId, Associations.id)
                .select { DomainsInAssociations.domain eq domain }
                .map(Associations::toAssociation)
                .singleOrNull()
        }
    }

    override suspend fun createDomain(domain: String, associationId: String): DomainInAssociation? {
        return database.dbQuery {
            DomainsInAssociations.insert {
                it[this.domain] = domain
                it[this.associationId] = associationId
            }.resultedValues?.map(DomainsInAssociations::toDomainInAssociation)?.singleOrNull()
        }
    }

    override suspend fun deleteDomain(domain: String) {
        database.dbQuery {
            DomainsInAssociations.deleteWhere {
                Op.build { DomainsInAssociations.domain eq domain }
            }
        }
    }

    override suspend fun getDomains(associationId: String): List<DomainInAssociation> {
        return database.dbQuery {
            DomainsInAssociations
                .select { DomainsInAssociations.associationId eq associationId }
                .map(DomainsInAssociations::toDomainInAssociation)
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

    override suspend fun updateCodeInEmail(email: String, code: String, expiresAt: Instant) {
        database.dbQuery {
            CodesInEmails.update({ CodesInEmails.email eq email }) {
                it[this.code] = code
                it[this.expiresAt] = expiresAt.toString()
            }
        }
    }

    override suspend fun deleteCodeInEmailBefore(date: Instant) {
        database.dbQuery {
            CodesInEmails.deleteWhere {
                Op.build { expiresAt less date.toString() }
            }
        }
    }

}