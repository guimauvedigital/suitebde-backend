package com.suitebde.database.users

import com.suitebde.models.users.ResetInUser
import com.suitebde.repositories.users.IResetsInUsersRepository
import dev.kaccelero.database.IDatabase
import dev.kaccelero.database.set
import dev.kaccelero.models.UUID
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

class ResetsInUsersDatabaseRepository(
    private val database: IDatabase,
) : IResetsInUsersRepository {

    init {
        database.transaction {
            SchemaUtils.create(ResetsInUsers)
        }
    }

    override suspend fun create(userId: UUID, expiration: Instant): ResetInUser? =
        database.suspendedTransaction {
            ResetsInUsers.insert {
                it[code] = generateCode()
                it[ResetsInUsers.userId] = userId
                it[ResetsInUsers.expiration] = expiration
            }.resultedValues?.map(ResetsInUsers::toResetInUser)?.singleOrNull()
        }

    override suspend fun get(code: String): ResetInUser? =
        database.suspendedTransaction {
            ResetsInUsers
                .selectAll()
                .where { ResetsInUsers.code eq code }
                .map(ResetsInUsers::toResetInUser)
                .singleOrNull()
        }

    override suspend fun delete(code: String): Boolean =
        database.suspendedTransaction {
            ResetsInUsers.deleteWhere {
                ResetsInUsers.code eq code
            }
        } == 1

    override suspend fun getExpiringBefore(date: Instant): List<ResetInUser> =
        database.suspendedTransaction {
            ResetsInUsers
                .selectAll()
                .where { ResetsInUsers.expiration less date }
                .map(ResetsInUsers::toResetInUser)
        }

}
