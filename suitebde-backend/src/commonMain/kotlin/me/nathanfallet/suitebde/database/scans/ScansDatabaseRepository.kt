package me.nathanfallet.suitebde.database.scans

import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.scans.CreateScanPayload
import me.nathanfallet.suitebde.models.scans.Scan
import me.nathanfallet.suitebde.models.users.UserContext
import me.nathanfallet.suitebde.repositories.scans.IScansRepository
import me.nathanfallet.surexposed.database.IDatabase
import me.nathanfallet.usecases.context.IContext
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert

class ScansDatabaseRepository(
    private val database: IDatabase,
) : IScansRepository {

    init {
        database.transaction {
            SchemaUtils.create(Scans)
        }
    }

    override suspend fun create(payload: CreateScanPayload, parentId: String, context: IContext?): Scan? {
        if (context !is UserContext) return null
        return database.suspendedTransaction {
            Scans.insert {
                it[id] = generateId()
                it[associationId] = parentId
                it[userId] = payload.userId
                it[scannerId] = context.userId
                it[scannedAt] = Clock.System.now().toString()
            }.resultedValues?.map(Scans::toScan)?.singleOrNull()
        }
    }

}
