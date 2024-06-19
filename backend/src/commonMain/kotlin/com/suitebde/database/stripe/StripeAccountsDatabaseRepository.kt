package com.suitebde.database.stripe

import com.suitebde.models.stripe.CreateStripeAccountPayload
import com.suitebde.models.stripe.StripeAccount
import com.suitebde.models.stripe.UpdateStripeAccountPayload
import com.suitebde.repositories.stripe.IStripeAccountsRepository
import dev.kaccelero.database.IDatabase
import dev.kaccelero.database.eq
import dev.kaccelero.database.set
import dev.kaccelero.models.IContext
import dev.kaccelero.models.UUID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class StripeAccountsDatabaseRepository(
    private val database: IDatabase,
) : IStripeAccountsRepository {

    init {
        database.transaction {
            SchemaUtils.create(StripeAccounts)
        }
    }

    override suspend fun list(parentId: UUID, context: IContext?): List<StripeAccount> =
        database.suspendedTransaction {
            StripeAccounts
                .selectAll()
                .where { StripeAccounts.associationId eq parentId }
                .map(StripeAccounts::toStripeAccount)
        }

    override suspend fun create(
        payload: CreateStripeAccountPayload,
        parentId: UUID,
        context: IContext?,
    ): StripeAccount? =
        database.suspendedTransaction {
            StripeAccounts.insert {
                it[associationId] = parentId
                it[accountId] = payload.accountId
                it[chargesEnabled] = payload.chargesEnabled
            }.resultedValues?.map(StripeAccounts::toStripeAccount)?.singleOrNull()
        }

    override suspend fun get(id: String, parentId: UUID, context: IContext?): StripeAccount? =
        database.suspendedTransaction {
            StripeAccounts
                .selectAll()
                .where {
                    StripeAccounts.accountId eq id and (StripeAccounts.associationId eq parentId)
                }
                .map(StripeAccounts::toStripeAccount)
                .singleOrNull()
        }

    override suspend fun update(
        id: String,
        payload: UpdateStripeAccountPayload,
        parentId: UUID,
        context: IContext?,
    ): Boolean =
        database.suspendedTransaction {
            StripeAccounts.update({ StripeAccounts.accountId eq id and (StripeAccounts.associationId eq parentId) }) {
                it[chargesEnabled] = payload.chargesEnabled
            } == 1
        }

    override suspend fun delete(id: String, parentId: UUID, context: IContext?): Boolean =
        database.suspendedTransaction {
            StripeAccounts.deleteWhere {
                accountId eq id and (associationId eq parentId)
            } == 1
        }

    override suspend fun count(parentId: UUID, context: IContext?): Long =
        database.suspendedTransaction {
            StripeAccounts
                .selectAll()
                .where { StripeAccounts.associationId eq parentId }
                .count()
        }

}
