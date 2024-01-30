package me.nathanfallet.suitebde.database.associations

import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.suitebde.repositories.associations.ICodesInEmailsRepository
import me.nathanfallet.surexposed.database.IDatabase
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class CodesInEmailsDatabaseRepository(
    private val database: IDatabase,
) : ICodesInEmailsRepository {

    init {
        database.transaction {
            SchemaUtils.create(CodesInEmails)
        }
    }

    override suspend fun getCodeInEmail(code: String): CodeInEmail? {
        return database.suspendedTransaction {
            CodesInEmails
                .selectAll()
                .where { CodesInEmails.code eq code }
                .map(CodesInEmails::toCodeInEmail)
                .singleOrNull()
        }
    }

    override suspend fun getCodesInEmailsExpiringBefore(date: Instant): List<CodeInEmail> {
        return database.suspendedTransaction {
            CodesInEmails
                .selectAll()
                .where { CodesInEmails.expiresAt less date.toString() }
                .map(CodesInEmails::toCodeInEmail)
        }
    }

    override suspend fun createCodeInEmail(
        email: String,
        code: String,
        associationId: String?,
        expiresAt: Instant,
    ): CodeInEmail? {
        return database.suspendedTransaction {
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
        return database.suspendedTransaction {
            CodesInEmails.update({ CodesInEmails.email eq email }) {
                it[this.code] = code
                it[this.associationId] = associationId
                it[this.expiresAt] = expiresAt.toString()
            }
        } == 1
    }

    override suspend fun deleteCodeInEmail(code: String) {
        database.suspendedTransaction {
            CodesInEmails.deleteWhere {
                CodesInEmails.code eq code
            }
        }
    }

}
