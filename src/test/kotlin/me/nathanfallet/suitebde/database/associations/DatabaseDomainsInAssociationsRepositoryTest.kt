package me.nathanfallet.suitebde.database.associations

import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.models.associations.CreateDomainInAssociationPayload
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
        val domain = repository.create(CreateDomainInAssociationPayload("domain"), "associationId")
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
        repository.create(CreateDomainInAssociationPayload("domain"), "associationId")
        assertThrows<ExposedSQLException> {
            repository.create(CreateDomainInAssociationPayload("domain"), "associationId")
        }
    }

    @Test
    fun deleteDomain() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteDomain")
        val repository = DatabaseDomainsInAssociationsRepository(database)
        repository.create(CreateDomainInAssociationPayload("domain"), "associationId")
            ?: fail("Unable to create domain")
        assertEquals(true, repository.delete("domain", "associationId"))
        val count = database.dbQuery {
            DomainsInAssociations
                .selectAll()
                .count()
        }
        assertEquals(0, count)
    }

    @Test
    fun deleteDomainNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteDomainNotExists")
        val repository = DatabaseDomainsInAssociationsRepository(database)
        assertEquals(false, repository.delete("domain", "associationId"))
    }

    @Test
    fun deleteDomainNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteDomainNotInAssociation")
        val repository = DatabaseDomainsInAssociationsRepository(database)
        repository.create(CreateDomainInAssociationPayload("domain"), "associationId")
            ?: fail("Unable to create domain")
        assertEquals(false, repository.delete("domain", "otherAssociationId"))
    }

    @Test
    fun getDomains() = runBlocking {
        val database = Database(protocol = "h2", name = "getDomains")
        val repository = DatabaseDomainsInAssociationsRepository(database)
        val domain = repository.create(CreateDomainInAssociationPayload("domain"), "associationId")
            ?: fail("Unable to create domain")
        val domains = repository.getDomains("associationId")
        assertEquals(domains.size, 1)
        assertEquals(domains.first().domain, domain.domain)
    }

    @Test
    fun getDomainsNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getDomainsNotInAssociation")
        val repository = DatabaseDomainsInAssociationsRepository(database)
        repository.create(CreateDomainInAssociationPayload("domain"), "associationId")
            ?: fail("Unable to create domain")
        val domains = repository.getDomains("otherAssociationId")
        assertEquals(domains.size, 0)
    }

    @Test
    fun getDomain() = runBlocking {
        val database = Database(protocol = "h2", name = "getDomain")
        val repository = DatabaseDomainsInAssociationsRepository(database)
        val domain = repository.create(CreateDomainInAssociationPayload("domain"), "associationId")
            ?: fail("Unable to create domain")
        val domainFromDatabase = repository.get("domain", "associationId")
        assertEquals(domain.domain, domainFromDatabase?.domain)
        assertEquals(domain.associationId, domainFromDatabase?.associationId)
    }

    @Test
    fun getDomainNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getDomainNotInAssociation")
        val repository = DatabaseDomainsInAssociationsRepository(database)
        repository.create(CreateDomainInAssociationPayload("domain"), "associationId")
            ?: fail("Unable to create domain")
        val domainFromDatabase = repository.get("domain", "otherAssociationId")
        assertEquals(null, domainFromDatabase)
    }

}
