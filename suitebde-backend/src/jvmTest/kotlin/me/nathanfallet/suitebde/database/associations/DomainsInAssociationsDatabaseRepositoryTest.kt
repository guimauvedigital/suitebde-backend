package me.nathanfallet.suitebde.database.associations

import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.models.associations.CreateDomainInAssociationPayload
import me.nathanfallet.usecases.pagination.Pagination
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.selectAll
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.fail

class DomainsInAssociationsDatabaseRepositoryTest {

    @Test
    fun createDomain() = runBlocking {
        val database = Database(protocol = "h2", name = "createDomain")
        val repository = DomainsInAssociationsDatabaseRepository(database)
        val domain = repository.create(CreateDomainInAssociationPayload("domain"), "associationId")
        val domainFromDatabase = database.suspendedTransaction {
            DomainsInAssociations
                .selectAll()
                .map(DomainsInAssociations::toDomainInAssociation)
                .singleOrNull()
        }
        assertEquals(domainFromDatabase?.domain, domain?.domain)
        assertEquals(domainFromDatabase?.associationId, domain?.associationId)
        assertEquals(domainFromDatabase?.domain, "domain")
        assertEquals(domainFromDatabase?.associationId, "associationId")
    }

    @Test
    fun createDomainAlreadyExists(): Unit = runBlocking {
        val database = Database(protocol = "h2", name = "createDomainAlreadyExists")
        val repository = DomainsInAssociationsDatabaseRepository(database)
        repository.create(CreateDomainInAssociationPayload("domain"), "associationId")
        assertFailsWith(ExposedSQLException::class) {
            repository.create(CreateDomainInAssociationPayload("domain"), "associationId")
        }
    }

    @Test
    fun deleteDomain() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteDomain")
        val repository = DomainsInAssociationsDatabaseRepository(database)
        repository.create(CreateDomainInAssociationPayload("domain"), "associationId")
            ?: fail("Unable to create domain")
        assertEquals(true, repository.delete("domain", "associationId"))
        val count = database.suspendedTransaction {
            DomainsInAssociations
                .selectAll()
                .count()
        }
        assertEquals(0, count)
    }

    @Test
    fun deleteDomainNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteDomainNotExists")
        val repository = DomainsInAssociationsDatabaseRepository(database)
        assertEquals(false, repository.delete("domain", "associationId"))
    }

    @Test
    fun deleteDomainNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteDomainNotInAssociation")
        val repository = DomainsInAssociationsDatabaseRepository(database)
        repository.create(CreateDomainInAssociationPayload("domain"), "associationId")
            ?: fail("Unable to create domain")
        assertEquals(false, repository.delete("domain", "otherAssociationId"))
    }

    @Test
    fun getDomains() = runBlocking {
        val database = Database(protocol = "h2", name = "getDomains")
        val repository = DomainsInAssociationsDatabaseRepository(database)
        val domain = repository.create(CreateDomainInAssociationPayload("domain"), "associationId")
            ?: fail("Unable to create domain")
        val domains = repository.list("associationId")
        assertEquals(domains.size, 1)
        assertEquals(domains.first().domain, domain.domain)
    }

    @Test
    fun getDomainsLimitOffset() = runBlocking {
        val database = Database(protocol = "h2", name = "getDomainsLimitOffset")
        val repository = DomainsInAssociationsDatabaseRepository(database)
        for (i in 1..4) repository.create(CreateDomainInAssociationPayload("domain $i"), "associationId")
        val domains = repository.list(Pagination(1, 2), "associationId")
        assertEquals(domains.size, 1)
        assertEquals(domains.first().domain, "domain 3")
    }

    @Test
    fun getDomainsNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getDomainsNotInAssociation")
        val repository = DomainsInAssociationsDatabaseRepository(database)
        repository.create(CreateDomainInAssociationPayload("domain"), "associationId")
            ?: fail("Unable to create domain")
        val domains = repository.list("otherAssociationId")
        assertEquals(domains.size, 0)
    }

    @Test
    fun getDomain() = runBlocking {
        val database = Database(protocol = "h2", name = "getDomain")
        val repository = DomainsInAssociationsDatabaseRepository(database)
        val domain = repository.create(CreateDomainInAssociationPayload("domain"), "associationId")
            ?: fail("Unable to create domain")
        val domainFromDatabase = repository.get("domain", "associationId")
        assertEquals(domain.domain, domainFromDatabase?.domain)
        assertEquals(domain.associationId, domainFromDatabase?.associationId)
    }

    @Test
    fun getDomainNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getDomainNotInAssociation")
        val repository = DomainsInAssociationsDatabaseRepository(database)
        repository.create(CreateDomainInAssociationPayload("domain"), "associationId")
            ?: fail("Unable to create domain")
        val domainFromDatabase = repository.get("domain", "otherAssociationId")
        assertEquals(null, domainFromDatabase)
    }

}
