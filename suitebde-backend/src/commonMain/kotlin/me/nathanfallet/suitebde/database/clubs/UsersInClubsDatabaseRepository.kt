package me.nathanfallet.suitebde.database.clubs

import me.nathanfallet.suitebde.database.users.Users
import me.nathanfallet.suitebde.models.clubs.CreateUserInClubPayload
import me.nathanfallet.suitebde.models.clubs.OptionalRoleInClubContext
import me.nathanfallet.suitebde.models.clubs.UserInClub
import me.nathanfallet.suitebde.repositories.clubs.IRolesInClubsRepository
import me.nathanfallet.suitebde.repositories.clubs.IUsersInClubsRepository
import me.nathanfallet.surexposed.database.IDatabase
import me.nathanfallet.usecases.context.IContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

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

    override suspend fun list(parentId: String, context: IContext?): List<UserInClub> =
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

    override suspend fun listForUser(userId: String): List<UserInClub> =
        database.suspendedTransaction {
            UsersInClubs
                .join(RolesInClubs, JoinType.INNER, UsersInClubs.roleId, RolesInClubs.id)
                .join(Clubs, JoinType.INNER, UsersInClubs.clubId, Clubs.id)
                .selectAll()
                .where { UsersInClubs.userId eq userId }
                .map {
                    UsersInClubs.toUserInClub(it, club = Clubs.toClub(it), role = RolesInClubs.toRoleInClub(it))
                }
        }

    override suspend fun get(id: String, parentId: String, context: IContext?): UserInClub? =
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

    override suspend fun create(payload: CreateUserInClubPayload, parentId: String, context: IContext?): UserInClub? {
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

    override suspend fun delete(id: String, parentId: String, context: IContext?): Boolean =
        database.suspendedTransaction {
            UsersInClubs.deleteWhere { clubId eq parentId and (userId eq id) }
        } == 1

}
