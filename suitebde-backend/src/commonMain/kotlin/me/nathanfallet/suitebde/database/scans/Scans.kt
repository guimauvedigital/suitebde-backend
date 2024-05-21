package me.nathanfallet.suitebde.database.scans

import kotlinx.datetime.toInstant
import me.nathanfallet.suitebde.extensions.generateId
import me.nathanfallet.suitebde.models.scans.Scan
import me.nathanfallet.suitebde.models.users.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll

object Scans : Table() {

    val id = varchar("id", 32)
    val associationId = varchar("association_id", 32).index()
    val userId = varchar("user_id", 32)
    val scannerId = varchar("scanner_id", 32)
    val scannedAt = varchar("scanned_at", 255)

    override val primaryKey = PrimaryKey(id)

    fun generateId(): String {
        val candidate = String.generateId()
        return if (selectAll().where { id eq candidate }.count() > 0) generateId() else candidate
    }

    fun toScan(
        row: ResultRow,
        user: User? = null,
        scanner: User? = null,
    ) = Scan(
        row[id],
        row[associationId],
        row[userId],
        row[scannerId],
        row[scannedAt].toInstant(),
        user,
        scanner
    )

}
