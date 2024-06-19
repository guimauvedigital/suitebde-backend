package com.suitebde.database.events

import org.jetbrains.exposed.dao.id.UUIDTable

object Tickets : UUIDTable() {

    val ticketConfigurationId = uuid("ticket_configuration_id").index()
    val email = varchar("email", 255)
    val firstName = varchar("first_name", 255)
    val lastName = varchar("last_name", 255)
    val userId = uuid("user_id").nullable()
    val paid = varchar("paid", 255)

}
