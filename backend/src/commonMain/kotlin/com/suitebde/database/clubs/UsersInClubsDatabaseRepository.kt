package com.suitebde.database.clubs

import com.suitebde.database.users.Users
import com.suitebde.models.clubs.CreateUserInClubPayload
import com.suitebde.models.clubs.OptionalRoleInClubContext
import com.suitebde.models.clubs.UserInClub
import com.suitebde.repositories.clubs.IRolesInClubsRepository
import com.suitebde.repositories.clubs.IUsersInClubsRepository
import dev.kaccelero.database.IDatabase
import dev.kaccelero.database.eq
import dev.kaccelero.database.set
import dev.kaccelero.models.IContext
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination
import org.jetbrains.exposed.sql.*

class UsersInClubsDatabaseRepository(
    private val database: IDatabase,
    private val rolesInClubsRepository: IRolesInClubsRepository,
) : IUsersInClubsRepository {

    init {
        database.transaction {
            SchemaUtils.create(UsersInClubs)
            SchemaUtils.create(Users)
            SchemaUtils.create(Clubs)
            SchemaUtils.create(RolesInClubs)
        }
    }

    override suspend fun list(parentId: UUID, context: IContext?): List<UserInClub> =
        database.suspendedTransaction {
            UsersInClubs
                .join(RolesInClubs, JoinType.INNER, UsersInClubs.roleId, RolesInClubs.id)
                .join(Users, JoinType.INNER, UsersInClubs.userId, Users.id)
                .selectAll()
                .where { UsersInClubs.clubId eq parentId }
                .map {
                    UsersInClubs.toUserInClub(it, user = Users.toUser(it), role = RolesInClubs.toRoleInClub(it))
                }
        }

    override suspend fun list(pagination: Pagination, parentId: UUID, context: IContext?): List<UserInClub> =
        database.suspendedTransaction {
            UsersInClubs
                .join(RolesInClubs, JoinType.INNER, UsersInClubs.roleId, RolesInClubs.id)
                .join(Users, JoinType.INNER, UsersInClubs.userId, Users.id)
                .selectAll()
                .where { UsersInClubs.clubId eq parentId }
                .limit(pagination.limit.toInt(), pagination.offset)
                .map {
                    UsersInClubs.toUserInClub(it, user = Users.toUser(it), role = RolesInClubs.toRoleInClub(it))
                }
        }

    override suspend fun listForUser(userId: UUID, associationId: UUID): List<UserInClub> =
        database.suspendedTransaction {
            UsersInClubs
                .join(RolesInClubs, JoinType.INNER, UsersInClubs.roleId, RolesInClubs.id)
                .join(Clubs, JoinType.INNER, UsersInClubs.clubId, Clubs.id)
                .selectAll()
                .where { UsersInClubs.userId eq userId and (Clubs.associationId eq associationId) }
                .map {
                    UsersInClubs.toUserInClub(it, club = Clubs.toClub(it), role = RolesInClubs.toRoleInClub(it))
                }
        }

    override suspend fun get(id: UUID, parentId: UUID, context: IContext?): UserInClub? =
        database.suspendedTransaction {
            UsersInClubs
                .join(RolesInClubs, JoinType.INNER, UsersInClubs.roleId, RolesInClubs.id)
                .join(Users, JoinType.INNER, UsersInClubs.userId, Users.id)
                .selectAll()
                .where { UsersInClubs.clubId eq parentId and (UsersInClubs.userId eq id) }
                .map {
                    UsersInClubs.toUserInClub(it, user = Users.toUser(it), role = RolesInClubs.toRoleInClub(it))
                }
                .singleOrNull()
        }

    override suspend fun create(payload: CreateUserInClubPayload, parentId: UUID, context: IContext?): UserInClub? {
        val defaultRoleId = (context as? OptionalRoleInClubContext)?.roleId
            ?: rolesInClubsRepository.getDefault(parentId)?.id
            ?: return null
        database.suspendedTransaction {
            UsersInClubs.insert {
                it[clubId] = parentId
                it[userId] = payload.userId
                it[roleId] = defaultRoleId
            }
        }
        return get(payload.userId, parentId, context)
    }

    override suspend fun delete(id: UUID, parentId: UUID, context: IContext?): Boolean =
        database.suspendedTransaction {
            UsersInClubs.deleteWhere { clubId eq parentId and (userId eq id) }
        } == 1

}
