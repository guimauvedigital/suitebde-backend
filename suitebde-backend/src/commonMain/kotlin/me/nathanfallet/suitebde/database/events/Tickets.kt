package me.nathanfallet.suitebde.database.events

import org.jetbrains.exposed.sql.Table

object Tickets : Table() {

    val id = varchar("id", 32)
    val ticketConfigurationId = varchar("ticket_configuration_id", 32).index()
    val email = varchar("email", 255)
    val firstName = varchar("first_name", 255)
    val lastName = varchar("last_name", 255)
    val userId = varchar("user_id", 32).nullable()
    val paid = varchar("paid", 255)

}
