package com.suitebde.database.associations

import com.suitebde.models.associations.DomainInAssociation
import dev.kaccelero.models.UUID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object DomainsInAssociations : Table() {

    val domain = varchar("domain", 255)
    val associationId = uuid("association_id")

    override val primaryKey = PrimaryKey(domain)

    fun toDomainInAssociation(
        row: ResultRow,
    ) = DomainInAssociation(
        row[domain],
        UUID(row[associationId])
    )

}
