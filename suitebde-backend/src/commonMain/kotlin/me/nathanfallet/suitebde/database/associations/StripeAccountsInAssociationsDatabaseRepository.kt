package me.nathanfallet.suitebde.database.associations

import me.nathanfallet.suitebde.models.associations.CreateStripeAccountInAssociationPayload
import me.nathanfallet.suitebde.models.associations.StripeAccountInAssociation
import me.nathanfallet.suitebde.models.associations.UpdateStripeAccountInAssociationPayload
import me.nathanfallet.suitebde.repositories.associations.IStripeAccountsInAssociationsRepository
import me.nathanfallet.surexposed.database.IDatabase
import me.nathanfallet.usecases.context.IContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class StripeAccountsInAssociationsDatabaseRepository(
    private val database: IDatabase,
) : IStripeAccountsInAssociationsRepository {

    init {
        database.transaction {
            SchemaUtils.create(StripeAccountsInAssociations)
        }
    }

    override suspend fun list(parentId: String, context: IContext?): List<StripeAccountInAssociation> =
        database.suspendedTransaction {
            StripeAccountsInAssociations
                .selectAll()
                .where { StripeAccountsInAssociations.associationId eq parentId }
                .map(StripeAccountsInAssociations::toStripeAccountInAssociation)
        }

    override suspend fun create(
        payload: CreateStripeAccountInAssociationPayload,
        parentId: String,
        context: IContext?,
    ): StripeAccountInAssociation? =
        database.suspendedTransaction {
            StripeAccountsInAssociations.insert {
                it[associationId] = parentId
                it[accountId] = payload.accountId
                it[chargesEnabled] = payload.chargesEnabled
            }.resultedValues?.map(StripeAccountsInAssociations::toStripeAccountInAssociation)?.singleOrNull()
        }

    override suspend fun get(id: String, parentId: String, context: IContext?): StripeAccountInAssociation? =
        database.suspendedTransaction {
            StripeAccountsInAssociations
                .selectAll()
                .where {
                    StripeAccountsInAssociations.accountId eq id and (StripeAccountsInAssociations.associationId eq parentId)
                }
                .map(StripeAccountsInAssociations::toStripeAccountInAssociation)
                .singleOrNull()
        }

    override suspend fun update(
        id: String,
        payload: UpdateStripeAccountInAssociationPayload,
        parentId: String,
        context: IContext?,
    ): Boolean =
        database.suspendedTransaction {
            StripeAccountsInAssociations.update({ StripeAccountsInAssociations.accountId eq id and (StripeAccountsInAssociations.associationId eq parentId) }) {
                it[chargesEnabled] = payload.chargesEnabled
            } == 1
        }

    override suspend fun delete(id: String, parentId: String, context: IContext?): Boolean =
        database.suspendedTransaction {
            StripeAccountsInAssociations.deleteWhere {
                StripeAccountsInAssociations.accountId eq id and (StripeAccountsInAssociations.associationId eq parentId)
            } == 1
        }

}
