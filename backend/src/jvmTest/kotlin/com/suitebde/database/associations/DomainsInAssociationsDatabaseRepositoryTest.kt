package com.suitebde.database.associations

import com.suitebde.database.Database
import com.suitebde.models.associations.CreateDomainInAssociationPayload
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination
import kotlinx.coroutines.runBlocking
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
        val domain = repository.create(CreateDomainInAssociationPayload("domain"), UUID())
        val domainFromDatabase = database.suspendedTransaction {
            DomainsInAssociations
                .selectAll()
                .map(DomainsInAssociations::toDomainInAssociation)
                .singleOrNull()
        }
        assertEquals(domainFromDatabase?.domain, domain?.domain)
        assertEquals(domainFromDatabase?.associationId, domain?.associationId)
        assertEquals(domainFromDatabase?.domain, "domain")
        assertEquals(domainFromDatabase?.associationId, UUID())
    }

    @Test
    fun createDomainAlreadyExists(): Unit = runBlocking {
        val database = Database(protocol = "h2", name = "createDomainAlreadyExists")
        val repository = DomainsInAssociationsDatabaseRepository(database)
        repository.create(CreateDomainInAssociationPayload("domain"), UUID())
        assertFailsWith(ExposedSQLException::class) {
            repository.create(CreateDomainInAssociationPayload("domain"), UUID())
        }
    }

    @Test
    fun deleteDomain() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteDomain")
        val repository = DomainsInAssociationsDatabaseRepository(database)
        repository.create(CreateDomainInAssociationPayload("domain"), UUID())
            ?: fail("Unable to create domain")
        assertEquals(true, repository.delete("domain", UUID()))
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
        assertEquals(false, repository.delete("domain", UUID()))
    }

    @Test
    fun deleteDomainNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteDomainNotInAssociation")
        val repository = DomainsInAssociationsDatabaseRepository(database)
        repository.create(CreateDomainInAssociationPayload("domain"), UUID())
            ?: fail("Unable to create domain")
        assertEquals(false, repository.delete("domain", UUID()))
    }

    @Test
    fun getDomains() = runBlocking {
        val database = Database(protocol = "h2", name = "getDomains")
        val repository = DomainsInAssociationsDatabaseRepository(database)
        val domain = repository.create(CreateDomainInAssociationPayload("domain"), UUID())
            ?: fail("Unable to create domain")
        val domains = repository.list(UUID())
        assertEquals(domains.size, 1)
        assertEquals(domains.first().domain, domain.domain)
    }

    @Test
    fun getDomainsLimitOffset() = runBlocking {
        val database = Database(protocol = "h2", name = "getDomainsLimitOffset")
        val repository = DomainsInAssociationsDatabaseRepository(database)
        for (i in 1..4) repository.create(CreateDomainInAssociationPayload("domain $i"), UUID())
        val domains = repository.list(Pagination(1, 2), UUID())
        assertEquals(domains.size, 1)
        assertEquals(domains.first().domain, "domain 3")
    }

    @Test
    fun getDomainsNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getDomainsNotInAssociation")
        val repository = DomainsInAssociationsDatabaseRepository(database)
        repository.create(CreateDomainInAssociationPayload("domain"), UUID())
            ?: fail("Unable to create domain")
        val domains = repository.list(UUID())
        assertEquals(domains.size, 0)
    }

    @Test
    fun getDomain() = runBlocking {
        val database = Database(protocol = "h2", name = "getDomain")
        val repository = DomainsInAssociationsDatabaseRepository(database)
        val domain = repository.create(CreateDomainInAssociationPayload("domain"), UUID())
            ?: fail("Unable to create domain")
        val domainFromDatabase = repository.get("domain", UUID())
        assertEquals(domain.domain, domainFromDatabase?.domain)
        assertEquals(domain.associationId, domainFromDatabase?.associationId)
    }

    @Test
    fun getDomainNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getDomainNotInAssociation")
        val repository = DomainsInAssociationsDatabaseRepository(database)
        repository.create(CreateDomainInAssociationPayload("domain"), UUID())
            ?: fail("Unable to create domain")
        val domainFromDatabase = repository.get("domain", UUID())
        assertEquals(null, domainFromDatabase)
    }

}
