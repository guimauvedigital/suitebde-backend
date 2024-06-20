package com.suitebde.database.associations

import com.suitebde.models.associations.CodeInEmail
import com.suitebde.repositories.associations.ICodesInEmailsRepository
import dev.kaccelero.database.IDatabase
import dev.kaccelero.models.UUID
import kotlinx.datetime.Instant
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

    override suspend fun getCodeInEmail(code: String): CodeInEmail? =
        database.suspendedTransaction {
            CodesInEmails
                .selectAll()
                .where { CodesInEmails.code eq code }
                .map(CodesInEmails::toCodeInEmail)
                .singleOrNull()
        }

    override suspend fun getCodesInEmailsExpiringBefore(date: Instant): List<CodeInEmail> =
        database.suspendedTransaction {
            CodesInEmails
                .selectAll()
                .where { CodesInEmails.expiresAt less date }
                .map(CodesInEmails::toCodeInEmail)
        }

    override suspend fun createCodeInEmail(
        email: String,
        code: String,
        associationId: UUID?,
        expiresAt: Instant,
    ): CodeInEmail? =
        database.suspendedTransaction {
            CodesInEmails.insert {
                it[this.email] = email
                it[this.code] = code
                it[this.associationId] = associationId?.javaUUID
                it[this.expiresAt] = expiresAt
            }.resultedValues?.map(CodesInEmails::toCodeInEmail)?.singleOrNull()
        }

    override suspend fun updateCodeInEmail(
        email: String,
        code: String,
        associationId: UUID?,
        expiresAt: Instant,
    ): Boolean =
        database.suspendedTransaction {
            CodesInEmails.update({ CodesInEmails.email eq email }) {
                it[this.code] = code
                it[this.associationId] = associationId?.javaUUID
                it[this.expiresAt] = expiresAt
            }
        } == 1

    override suspend fun deleteCodeInEmail(code: String) {
        database.suspendedTransaction {
            CodesInEmails.deleteWhere {
                CodesInEmails.code eq code
            }
        }
    }

}
