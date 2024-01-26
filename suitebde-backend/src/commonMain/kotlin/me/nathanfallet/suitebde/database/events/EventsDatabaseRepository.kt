package me.nathanfallet.suitebde.database.events

import me.nathanfallet.suitebde.models.events.CreateEventPayload
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.models.events.UpdateEventPayload
import me.nathanfallet.surexposed.database.IDatabase
import me.nathanfallet.usecases.context.IContext
import me.nathanfallet.usecases.models.repositories.IChildModelSuspendRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class EventsDatabaseRepository(
    private val database: IDatabase,
) : IChildModelSuspendRepository<Event, String, CreateEventPayload, UpdateEventPayload, String> {

    init {
        database.transaction {
            SchemaUtils.create(Events)
        }
    }

    override suspend fun list(parentId: String, context: IContext?): List<Event> {
        return database.suspendedTransaction {
            Events
                .selectAll()
                .where { Events.associationId eq parentId }
                .map(Events::toEvent)
        }
    }

    override suspend fun list(limit: Long, offset: Long, parentId: String, context: IContext?): List<Event> {
        return database.suspendedTransaction {
            Events
                .selectAll()
                .where { Events.associationId eq parentId }
                .limit(limit.toInt(), offset)
                .map(Events::toEvent)
        }
    }

    override suspend fun get(id: String, parentId: String, context: IContext?): Event? {
        return database.suspendedTransaction {
            Events
                .selectAll()
                .where { Events.id eq id and (Events.associationId eq parentId) }
                .map(Events::toEvent)
                .singleOrNull()
        }
    }

    override suspend fun create(payload: CreateEventPayload, parentId: String, context: IContext?): Event? {
        return database.suspendedTransaction {
            Events.insert {
                it[id] = generateId()
                it[associationId] = parentId
                it[name] = payload.name
                it[description] = payload.description
                it[icon] = payload.icon
                it[startsAt] = payload.startsAt.toString()
                it[endsAt] = payload.endsAt.toString()
                it[validated] = payload.validated ?: false
            }.resultedValues?.map(Events::toEvent)?.singleOrNull()
        }
    }

    override suspend fun update(
        id: String,
        payload: UpdateEventPayload,
        parentId: String,
        context: IContext?,
    ): Boolean {
        return database.suspendedTransaction {
            Events.update({ Events.id eq id and (Events.associationId eq parentId) }) {
                payload.name?.let { name -> it[Events.name] = name }
                payload.description?.let { description -> it[Events.description] = description }
                payload.icon?.let { icon -> it[Events.icon] = icon }
                payload.startsAt?.let { startsAt -> it[Events.startsAt] = startsAt.toString() }
                payload.endsAt?.let { endsAt -> it[Events.endsAt] = endsAt.toString() }
                payload.validated?.let { validated -> it[Events.validated] = validated }
            }
        } == 1
    }

    override suspend fun delete(id: String, parentId: String, context: IContext?): Boolean {
        return database.suspendedTransaction {
            Events.deleteWhere {
                Events.id eq id and (associationId eq parentId)
            }
        } == 1
    }

}
