package me.nathanfallet.suitebde.database.associations

import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.suitebde.repositories.associations.ICodesInEmailsRepository
import org.jetbrains.exposed.sql.*

class DatabaseCodesInEmailsRepository(
    private val database: Database,
) : ICodesInEmailsRepository {

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
        expiresAt: Instant,
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
        expiresAt: Instant,
    ): Boolean {
        return database.dbQuery {
            CodesInEmails.update({ CodesInEmails.email eq email }) {
                it[this.code] = code
                it[this.associationId] = associationId
                it[this.expiresAt] = expiresAt.toString()
            }
        } == 1
    }

    override suspend fun deleteCodeInEmail(code: String) {
        database.dbQuery {
            CodesInEmails.deleteWhere {
                Op.build { CodesInEmails.code eq code }
            }
        }
    }

}
