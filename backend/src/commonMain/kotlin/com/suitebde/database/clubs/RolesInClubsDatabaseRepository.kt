package com.suitebde.database.clubs

import com.suitebde.models.clubs.CreateRoleInClubPayload
import com.suitebde.models.clubs.RoleInClub
import com.suitebde.models.clubs.UpdateRoleInClubPayload
import com.suitebde.repositories.clubs.IRolesInClubsRepository
import dev.kaccelero.database.IDatabase
import dev.kaccelero.database.eq
import dev.kaccelero.database.set
import dev.kaccelero.models.IContext
import dev.kaccelero.models.UUID
import org.jetbrains.exposed.sql.*

class RolesInClubsDatabaseRepository(
    private val database: IDatabase,
) : IRolesInClubsRepository {

    init {
        database.transaction {
            SchemaUtils.create(RolesInClubs)
        }
    }

    override suspend fun list(parentId: UUID, context: IContext?): List<RoleInClub> =
        database.suspendedTransaction {
            RolesInClubs
                .selectAll()
                .where { RolesInClubs.clubId eq parentId }
                .map(RolesInClubs::toRoleInClub)
        }

    override suspend fun get(id: UUID, parentId: UUID, context: IContext?): RoleInClub? =
        database.suspendedTransaction {
            RolesInClubs
                .selectAll()
                .where { RolesInClubs.clubId eq parentId and (RolesInClubs.id eq id) }
                .map(RolesInClubs::toRoleInClub)
                .singleOrNull()
        }

    override suspend fun getDefault(parentId: UUID): RoleInClub? =
        database.suspendedTransaction {
            RolesInClubs
                .selectAll()
                .where { (RolesInClubs.clubId eq parentId) and (RolesInClubs.default eq true) }
                .map(RolesInClubs::toRoleInClub)
                .singleOrNull()
        }

    override suspend fun create(payload: CreateRoleInClubPayload, parentId: UUID, context: IContext?): RoleInClub? =
        database.suspendedTransaction {
            RolesInClubs.insert {
                it[this.clubId] = parentId
                it[this.name] = payload.name
                it[this.admin] = payload.admin
                it[this.default] = payload.default
            }.resultedValues?.map(RolesInClubs::toRoleInClub)?.singleOrNull()
        }

    override suspend fun update(
        id: UUID,
        payload: UpdateRoleInClubPayload,
        parentId: UUID,
        context: IContext?,
    ): Boolean =
        database.suspendedTransaction {
            RolesInClubs.update({ (RolesInClubs.clubId eq parentId) and (RolesInClubs.id eq id) }) {
                payload.name?.let { name -> it[RolesInClubs.name] = name }
                payload.admin?.let { admin -> it[RolesInClubs.admin] = admin }
            }
        } == 1

    override suspend fun delete(id: UUID, parentId: UUID, context: IContext?): Boolean =
        database.suspendedTransaction {
            RolesInClubs.deleteWhere { (clubId eq parentId) and (RolesInClubs.id eq id) }
        } == 1

}
