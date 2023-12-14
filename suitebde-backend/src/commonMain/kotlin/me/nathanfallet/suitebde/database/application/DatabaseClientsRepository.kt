package me.nathanfallet.suitebde.database.application

import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.models.application.Client
import me.nathanfallet.usecases.context.IContext
import me.nathanfallet.usecases.models.repositories.IModelSuspendRepository
import org.jetbrains.exposed.sql.select

class DatabaseClientsRepository(
    private val database: Database,
) : IModelSuspendRepository<Client, String, Unit, Unit> {

    override suspend fun get(id: String, context: IContext?): Client? {
        return database.dbQuery {
            Clients
                .select { Clients.id eq id }
                .map(Clients::toClient)
                .singleOrNull()
        }
    }

}
