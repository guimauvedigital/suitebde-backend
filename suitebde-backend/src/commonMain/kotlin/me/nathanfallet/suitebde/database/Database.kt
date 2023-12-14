package me.nathanfallet.suitebde.database

import kotlinx.coroutines.Dispatchers
import me.nathanfallet.suitebde.database.application.Clients
import me.nathanfallet.suitebde.database.associations.Associations
import me.nathanfallet.suitebde.database.associations.CodesInEmails
import me.nathanfallet.suitebde.database.associations.DomainsInAssociations
import me.nathanfallet.suitebde.database.users.ClientsInUsers
import me.nathanfallet.suitebde.database.users.Users
import me.nathanfallet.suitebde.database.web.WebMenus
import me.nathanfallet.suitebde.database.web.WebPages
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class Database(
    protocol: String,
    host: String = "",
    name: String = "",
    user: String = "",
    password: String = "",
) {

    // Connect to database
    private val database: org.jetbrains.exposed.sql.Database = when (protocol) {
        "mysql" -> org.jetbrains.exposed.sql.Database.connect(
            "jdbc:mysql://$host:3306/$name", "com.mysql.cj.jdbc.Driver",
            user, password
        )

        "h2" -> org.jetbrains.exposed.sql.Database.connect(
            "jdbc:h2:mem:$name;DB_CLOSE_DELAY=-1;", "org.h2.Driver"
        )

        else -> throw Exception("Unsupported database protocol: $protocol")
    }

    init {
        transaction(database) {
            SchemaUtils.create(Clients)
            SchemaUtils.create(Associations)
            SchemaUtils.create(DomainsInAssociations)
            SchemaUtils.create(CodesInEmails)
            SchemaUtils.create(Users)
            SchemaUtils.create(ClientsInUsers)
            SchemaUtils.create(WebPages)
            SchemaUtils.create(WebMenus)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO, database) { block() }

}
