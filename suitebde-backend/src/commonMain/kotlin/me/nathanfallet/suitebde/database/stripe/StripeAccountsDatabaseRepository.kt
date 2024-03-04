package me.nathanfallet.suitebde.database.stripe

import me.nathanfallet.suitebde.models.stripe.CreateStripeAccountPayload
import me.nathanfallet.suitebde.models.stripe.StripeAccount
import me.nathanfallet.suitebde.models.stripe.UpdateStripeAccountPayload
import me.nathanfallet.suitebde.repositories.stripe.IStripeAccountsRepository
import me.nathanfallet.surexposed.database.IDatabase
import me.nathanfallet.usecases.context.IContext
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

    override suspend fun list(parentId: String, context: IContext?): List<StripeAccount> =
        database.suspendedTransaction {
            StripeAccounts
                .selectAll()
                .where { StripeAccounts.associationId eq parentId }
                .map(StripeAccounts::toStripeAccount)
        }

    override suspend fun create(
        payload: CreateStripeAccountPayload,
        parentId: String,
        context: IContext?,
    ): StripeAccount? =
        database.suspendedTransaction {
            StripeAccounts.insert {
                it[associationId] = parentId
                it[accountId] = payload.accountId
                it[chargesEnabled] = payload.chargesEnabled
            }.resultedValues?.map(StripeAccounts::toStripeAccount)?.singleOrNull()
        }

    override suspend fun get(id: String, parentId: String, context: IContext?): StripeAccount? =
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
        parentId: String,
        context: IContext?,
    ): Boolean =
        database.suspendedTransaction {
            StripeAccounts.update({ StripeAccounts.accountId eq id and (StripeAccounts.associationId eq parentId) }) {
                it[chargesEnabled] = payload.chargesEnabled
            } == 1
        }

    override suspend fun delete(id: String, parentId: String, context: IContext?): Boolean =
        database.suspendedTransaction {
            StripeAccounts.deleteWhere {
                accountId eq id and (associationId eq parentId)
            } == 1
        }

    override suspend fun count(parentId: String): Long =
        database.suspendedTransaction {
            StripeAccounts
                .selectAll()
                .where { StripeAccounts.associationId eq parentId }
                .count()
        }

}
