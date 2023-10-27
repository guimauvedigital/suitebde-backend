package me.nathanfallet.suitebde.database.associations

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.*
import me.nathanfallet.suitebde.database.Database
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.selectAll
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class DatabaseAssociationRepositoryTest {

    private val now = Clock.System.now()
    private val tomorrow = now.plus(1, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
    private val yesterday = now.minus(1, DateTimeUnit.DAY, TimeZone.currentSystemDefault())

    @Test
    fun createAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "createAssociation")
        val repository = DatabaseAssociationRepository(database)
        val association = repository.createAssociation(
            "name", "school", "city",
            true, now, tomorrow
        )
        val associationFromDatabase = database.dbQuery {
            Associations
                .selectAll()
                .map(Associations::toAssociation)
                .singleOrNull()
        }
        assertEquals(associationFromDatabase?.id, association?.id)
        assertEquals(associationFromDatabase?.name, association?.name)
        assertEquals(associationFromDatabase?.school, association?.school)
        assertEquals(associationFromDatabase?.city, association?.city)
        assertEquals(associationFromDatabase?.validated, association?.validated)
        assertEquals(associationFromDatabase?.createdAt, association?.createdAt)
        assertEquals(associationFromDatabase?.expiresAt, association?.expiresAt)
    }

    @Test
    fun updateAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "updateAssociation")
        val repository = DatabaseAssociationRepository(database)
        val association = repository.createAssociation(
            "name", "school", "city",
            true, now, tomorrow
        )?.copy(
            name = "newName",
            school = "newSchool",
            city = "newCity",
            validated = true,
            expiresAt = tomorrow
        ) ?: fail("Unable to create association")
        repository.updateAssociation(association)
        val associationFromDatabase = database.dbQuery {
            Associations
                .selectAll()
                .map(Associations::toAssociation)
                .singleOrNull()
        }
        assertEquals(associationFromDatabase?.id, association.id)
        assertEquals(associationFromDatabase?.name, association.name)
        assertEquals(associationFromDatabase?.school, association.school)
        assertEquals(associationFromDatabase?.city, association.city)
        assertEquals(associationFromDatabase?.validated, association.validated)
        assertEquals(associationFromDatabase?.createdAt, association.createdAt)
        assertEquals(associationFromDatabase?.expiresAt, association.expiresAt)
    }

    @Test
    fun deleteAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteAssociation")
        val repository = DatabaseAssociationRepository(database)
        val association = repository.createAssociation(
            "name", "school", "city",
            true, now, tomorrow
        ) ?: fail("Unable to create association")
        repository.deleteAssociation(association)
        val count = database.dbQuery {
            Associations
                .selectAll()
                .count()
        }
        assertEquals(0, count)
    }

    @Test
    fun getAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getAssociation")
        val repository = DatabaseAssociationRepository(database)
        val association = repository.createAssociation(
            "name", "school", "city",
            true, now, tomorrow
        ) ?: fail("Unable to create association")
        val associationFromDatabase = repository.getAssociation(association.id)
        assertEquals(associationFromDatabase?.id, association.id)
        assertEquals(associationFromDatabase?.name, association.name)
        assertEquals(associationFromDatabase?.school, association.school)
        assertEquals(associationFromDatabase?.city, association.city)
        assertEquals(associationFromDatabase?.validated, association.validated)
        assertEquals(associationFromDatabase?.createdAt, association.createdAt)
        assertEquals(associationFromDatabase?.expiresAt, association.expiresAt)
    }

    @Test
    fun getAssociations() = runBlocking {
        val database = Database(protocol = "h2", name = "getAssociations")
        val repository = DatabaseAssociationRepository(database)
        val association = repository.createAssociation(
            "name", "school", "city",
            true, now, tomorrow
        ) ?: fail("Unable to create association")
        val associations = repository.getAssociations()
        assertEquals(associations.size, 1)
        assertEquals(associations.first().id, association.id)
        assertEquals(associations.first().name, association.name)
        assertEquals(associations.first().school, association.school)
        assertEquals(associations.first().city, association.city)
        assertEquals(associations.first().validated, association.validated)
        assertEquals(associations.first().createdAt, association.createdAt)
        assertEquals(associations.first().expiresAt, association.expiresAt)
    }

    @Test
    fun getAssociationForDomain() = runBlocking {
        val database = Database(protocol = "h2", name = "getAssociationForDomain")
        val repository = DatabaseAssociationRepository(database)
        val association = repository.createAssociation(
            "name", "school", "city",
            true, now, tomorrow
        ) ?: fail("Unable to create association")
        val domain = repository.createDomain("domain", association.id) ?: fail("Unable to create domain")
        val associationFromDatabase = repository.getAssociationForDomain(domain.domain)
        assertEquals(associationFromDatabase?.id, association.id)
        assertEquals(associationFromDatabase?.name, association.name)
        assertEquals(associationFromDatabase?.school, association.school)
        assertEquals(associationFromDatabase?.city, association.city)
        assertEquals(associationFromDatabase?.validated, association.validated)
        assertEquals(associationFromDatabase?.createdAt, association.createdAt)
        assertEquals(associationFromDatabase?.expiresAt, association.expiresAt)
    }

    @Test
    fun createDomain() = runBlocking {
        val database = Database(protocol = "h2", name = "createDomain")
        val repository = DatabaseAssociationRepository(database)
        val domain = repository.createDomain("domain", "associationId")
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
        val repository = DatabaseAssociationRepository(database)
        repository.createDomain("domain", "associationId")
        assertThrows<ExposedSQLException> {
            repository.createDomain("domain", "associationId")
        }
    }

    @Test
    fun deleteDomain() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteDomain")
        val repository = DatabaseAssociationRepository(database)
        val domain = repository.createDomain("domain", "associationId") ?: fail("Unable to create domain")
        repository.deleteDomain(domain.domain)
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
        val repository = DatabaseAssociationRepository(database)
        val domain = repository.createDomain("domain", "associationId") ?: fail("Unable to create domain")
        val domains = repository.getDomains("associationId")
        assertEquals(domains.size, 1)
        assertEquals(domains.first().domain, domain.domain)
    }

    @Test
    fun createCodeInEmail() = runBlocking {
        val database = Database(protocol = "h2", name = "createCodeInEmail")
        val repository = DatabaseAssociationRepository(database)
        val codeInEmail = repository.createCodeInEmail("email", "code", "associationId", tomorrow)
        val codeInEmailFromDatabase = database.dbQuery {
            CodesInEmails
                .selectAll()
                .map(CodesInEmails::toCodeInEmail)
                .singleOrNull()
        }
        assertEquals(codeInEmailFromDatabase?.email, codeInEmail?.email)
        assertEquals(codeInEmailFromDatabase?.code, codeInEmail?.code)
        assertEquals(codeInEmailFromDatabase?.associationId, codeInEmail?.associationId)
        assertEquals(codeInEmailFromDatabase?.expiresAt, codeInEmail?.expiresAt)
    }

    @Test
    fun updateCodeInEmail() = runBlocking {
        val database = Database(protocol = "h2", name = "updateCodeInEmail")
        val repository = DatabaseAssociationRepository(database)
        val codeInEmail = repository.createCodeInEmail("email", "code", "associationId", tomorrow)
            ?: fail("Unable to create code in email")
        repository.updateCodeInEmail("email", "newCode", tomorrow)
        val codeInEmailFromDatabase = database.dbQuery {
            CodesInEmails
                .selectAll()
                .map(CodesInEmails::toCodeInEmail)
                .singleOrNull()
        }
        assertEquals(codeInEmailFromDatabase?.email, codeInEmail.email)
        assertEquals(codeInEmailFromDatabase?.code, "newCode")
        assertEquals(codeInEmailFromDatabase?.associationId, codeInEmail.associationId)
        assertEquals(codeInEmailFromDatabase?.expiresAt, codeInEmail.expiresAt)
    }

    @Test
    fun deleteCodeInEmailBefore() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteCodeInEmailBefore")
        val repository = DatabaseAssociationRepository(database)
        repository.createCodeInEmail("email", "code", "associationId", yesterday)
            ?: fail("Unable to create code in email")
        repository.deleteCodeInEmailBefore(now)
        val count = database.dbQuery {
            CodesInEmails
                .selectAll()
                .count()
        }
        assertEquals(0, count)
    }

}