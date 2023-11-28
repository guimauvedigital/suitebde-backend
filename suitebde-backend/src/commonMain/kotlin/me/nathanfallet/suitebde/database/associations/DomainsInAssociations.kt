package me.nathanfallet.suitebde.database.associations

import me.nathanfallet.suitebde.models.associations.DomainInAssociation
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object DomainsInAssociations : Table() {

    val domain = varchar("domain", 255)
    val associationId = varchar("association_id", 32).index()

    override val primaryKey = PrimaryKey(domain)

    fun toDomainInAssociation(
        row: ResultRow
    ) = DomainInAssociation(
        row[domain],
        row[associationId]
    )

}