package me.nathanfallet.suitebde.database.clubs

import kotlinx.datetime.Clock
import me.nathanfallet.ktorx.database.IDatabase
import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.clubs.CreateClubPayload
import me.nathanfallet.suitebde.models.clubs.UpdateClubPayload
import me.nathanfallet.usecases.context.IContext
import me.nathanfallet.usecases.models.repositories.IChildModelSuspendRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ClubsDatabaseRepository(
    private val database: IDatabase,
) : IChildModelSuspendRepository<Club, String, CreateClubPayload, UpdateClubPayload, String> {

    init {
        database.transaction {
            SchemaUtils.create(Clubs)
        }
    }

    override suspend fun list(parentId: String, context: IContext?): List<Club> {
        return database.suspendedTransaction {
            Clubs
                .selectAll()
                .where { Clubs.associationId eq parentId }
                .map(Clubs::toClub)
        }
    }

    override suspend fun list(limit: Long, offset: Long, parentId: String, context: IContext?): List<Club> {
        return database.suspendedTransaction {
            Clubs
                .selectAll()
                .where { Clubs.associationId eq parentId }
                .limit(limit.toInt(), offset)
                .map(Clubs::toClub)
        }
    }

    override suspend fun create(payload: CreateClubPayload, parentId: String, context: IContext?): Club? {
        return database.suspendedTransaction {
            Clubs.insert {
                it[id] = generateId()
                it[associationId] = parentId
                it[name] = payload.name
                it[description] = payload.description
                it[icon] = payload.icon
                it[createdAt] = Clock.System.now().toString()
                it[validated] = payload.validated ?: false
            }.resultedValues?.map(Clubs::toClub)?.singleOrNull()
        }
    }

    override suspend fun get(id: String, parentId: String, context: IContext?): Club? {
        return database.suspendedTransaction {
            Clubs
                .selectAll()
                .where { Clubs.id eq id and (Clubs.associationId eq parentId) }
                .map(Clubs::toClub)
                .singleOrNull()
        }
    }

    override suspend fun update(id: String, payload: UpdateClubPayload, parentId: String, context: IContext?): Boolean {
        return database.suspendedTransaction {
            Clubs.update({ Clubs.id eq id and (Clubs.associationId eq parentId) }) {
                payload.name?.let { name -> it[Clubs.name] = name }
                payload.description?.let { description -> it[Clubs.description] = description }
                payload.icon?.let { icon -> it[Clubs.icon] = icon }
                payload.validated?.let { validated -> it[Clubs.validated] = validated }
            }
        } == 1
    }

    override suspend fun delete(id: String, parentId: String, context: IContext?): Boolean {
        return database.suspendedTransaction {
            Clubs.deleteWhere {
                Clubs.id eq id and (associationId eq parentId)
            }
        } == 1
    }

}
