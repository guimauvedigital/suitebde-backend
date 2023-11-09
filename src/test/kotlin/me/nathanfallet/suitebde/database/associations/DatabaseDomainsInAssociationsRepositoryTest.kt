package me.nathanfallet.suitebde.database.associations

import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.database.Database
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.selectAll
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class DatabaseDomainsInAssociationsRepositoryTest {

    @Test
    fun createDomain() = runBlocking {
        val database = Database(protocol = "h2", name = "createDomain")
        val repository = DatabaseDomainsInAssociationsRepository(database)
        val domain = repository.create("domain", "associationId")
        val domainFromDatabase = database.dbQuery {
            DomainsInAssociations
                .selectAll()
                .map(DomainsInAssociations::toDomainInAssociation)
                .singleOrNull()
        }
        assertEquals(domainFromDatabase?.domain, domain?.domain)
        assertEquals(domainFromDatabase?.associationId, domain?.associationId)
    }

    @Test
    fun createDomainAlreadyExists(): Unit = runBlocking {
        val database = Database(protocol = "h2", name = "createDomainAlreadyExists")
        val repository = DatabaseDomainsInAssociationsRepository(database)
        repository.create("domain", "associationId")
        assertThrows<ExposedSQLException> {
            repository.create("domain", "associationId")
        }
    }

    @Test
    fun deleteDomain() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteDomain")
        val repository = DatabaseDomainsInAssociationsRepository(database)
        val domain = repository.create("domain", "associationId") ?: fail("Unable to create domain")
        repository.delete(domain.domain, domain.associationId)
        val count = database.dbQuery {
            DomainsInAssociations
                .selectAll()
                .count()
        }
        assertEquals(0, count)
    }

    @Test
    fun getDomains() = runBlocking {
        val database = Database(protocol = "h2", name = "getDomains")
        val repository = DatabaseDomainsInAssociationsRepository(database)
        val domain = repository.create("domain", "associationId") ?: fail("Unable to create domain")
        val domains = repository.getDomains("associationId")
        assertEquals(domains.size, 1)
        assertEquals(domains.first().domain, domain.domain)
    }

}