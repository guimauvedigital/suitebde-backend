package me.nathanfallet.suitebde.database.clubs

import me.nathanfallet.ktorx.database.IDatabase
import me.nathanfallet.suitebde.database.users.Users
import me.nathanfallet.suitebde.models.clubs.CreateUserInClub
import me.nathanfallet.suitebde.models.clubs.UserInClub
import me.nathanfallet.suitebde.repositories.clubs.IUsersInClubsRepository
import me.nathanfallet.usecases.context.IContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UsersInClubsDatabaseRepository(
    private val database: IDatabase,
) : IUsersInClubsRepository {

    init {
        database.transaction {
            SchemaUtils.create(UsersInClubs)
        }
    }

    override suspend fun list(parentId: String, context: IContext?): List<UserInClub> {
        return database.suspendedTransaction {
            UsersInClubs
                .join(Users, JoinType.INNER, UsersInClubs.userId, Users.id)
                .selectAll()
                .where { UsersInClubs.clubId eq parentId }
                .map {
                    UsersInClubs.toUserInClub(it, user = Users.toUser(it))
                }
        }
    }

    override suspend fun listForUser(userId: String): List<UserInClub> {
        return database.suspendedTransaction {
            UsersInClubs
                .join(Clubs, JoinType.INNER, UsersInClubs.clubId, Clubs.id)
                .selectAll()
                .where { UsersInClubs.userId eq userId }
                .map {
                    UsersInClubs.toUserInClub(it, club = Clubs.toClub(it))
                }
        }
    }

    override suspend fun get(id: String, parentId: String, context: IContext?): UserInClub? {
        return database.suspendedTransaction {
            UsersInClubs
                .join(Users, JoinType.INNER, UsersInClubs.userId, Users.id)
                .selectAll()
                .where { UsersInClubs.clubId eq parentId and (UsersInClubs.userId eq id) }
                .map {
                    UsersInClubs.toUserInClub(it, user = Users.toUser(it))
                }
                .singleOrNull()
        }
    }

    override suspend fun create(payload: CreateUserInClub, parentId: String, context: IContext?): UserInClub? {
        return database.suspendedTransaction {
            UsersInClubs.insert {
                it[clubId] = parentId
                it[userId] = payload.userId
            }.resultedValues?.map(UsersInClubs::toUserInClub)?.singleOrNull()
        }
    }

    override suspend fun delete(id: String, parentId: String, context: IContext?): Boolean {
        return database.suspendedTransaction {
            UsersInClubs.deleteWhere { clubId eq parentId and (userId eq id) }
        } == 1
    }

}
