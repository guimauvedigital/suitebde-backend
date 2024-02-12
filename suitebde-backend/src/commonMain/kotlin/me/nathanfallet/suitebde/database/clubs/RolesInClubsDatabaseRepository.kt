package me.nathanfallet.suitebde.database.clubs

import me.nathanfallet.suitebde.models.clubs.CreateRoleInClubPayload
import me.nathanfallet.suitebde.models.clubs.RoleInClub
import me.nathanfallet.suitebde.models.clubs.UpdateRoleInClubPayload
import me.nathanfallet.suitebde.repositories.clubs.IRolesInClubsRepository
import me.nathanfallet.surexposed.database.IDatabase
import me.nathanfallet.usecases.context.IContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class RolesInClubsDatabaseRepository(
    private val database: IDatabase,
) : IRolesInClubsRepository {

    init {
        database.transaction {
            SchemaUtils.create(RolesInClubs)
        }
    }

    override suspend fun list(parentId: String, context: IContext?): List<RoleInClub> =
        database.suspendedTransaction {
            RolesInClubs
                .selectAll()
                .where { RolesInClubs.clubId eq parentId }
                .map(RolesInClubs::toRoleInClub)
        }

    override suspend fun get(id: String, parentId: String, context: IContext?): RoleInClub? =
        database.suspendedTransaction {
            RolesInClubs
                .selectAll()
                .where { RolesInClubs.clubId eq parentId and (RolesInClubs.id eq id) }
                .map(RolesInClubs::toRoleInClub)
                .singleOrNull()
        }

    override suspend fun create(payload: CreateRoleInClubPayload, parentId: String, context: IContext?): RoleInClub? =
        database.suspendedTransaction {
            RolesInClubs.insert {
                it[this.id] = generateId()
                it[this.clubId] = parentId
                it[this.name] = payload.name
                it[this.admin] = payload.admin
                it[this.default] = payload.default
            }.resultedValues?.map(RolesInClubs::toRoleInClub)?.singleOrNull()
        }

    override suspend fun update(
        id: String,
        payload: UpdateRoleInClubPayload,
        parentId: String,
        context: IContext?,
    ): Boolean =
        database.suspendedTransaction {
            RolesInClubs.update({ (RolesInClubs.clubId eq parentId) and (RolesInClubs.id eq id) }) {
                payload.name?.let { name -> it[RolesInClubs.name] = name }
                payload.admin?.let { admin -> it[RolesInClubs.admin] = admin }
            }
        } == 1

    override suspend fun delete(id: String, parentId: String, context: IContext?): Boolean =
        database.suspendedTransaction {
            RolesInClubs.deleteWhere { (clubId eq parentId) and (RolesInClubs.id eq id) }
        } == 1

}
