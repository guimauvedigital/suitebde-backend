package me.nathanfallet.suitebde.database.associations

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.*
import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.associations.CreateDomainInAssociationPayload
import me.nathanfallet.suitebde.models.associations.UpdateAssociationPayload
import org.jetbrains.exposed.sql.selectAll
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
        val association = repository.create(
            CreateAssociationPayload(
                "name", "school", "city",
                "email", "password", "firstname", "lastname"
            )
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
        assertEquals(associationFromDatabase?.name, "name")
        assertEquals(associationFromDatabase?.school, "school")
        assertEquals(associationFromDatabase?.city, "city")
        assertEquals(associationFromDatabase?.validated, false)
    }

    @Test
    fun updateAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "updateAssociation")
        val repository = DatabaseAssociationRepository(database)
        val association = repository.create(
            CreateAssociationPayload(
                "name", "school", "city",
                "email", "password", "firstname", "lastname"
            )
        )?.copy(
            name = "newName",
            school = "newSchool",
            city = "newCity",
            validated = true
        ) ?: fail("Unable to create association")
        assertEquals(
            true, repository.update(
                association.id, UpdateAssociationPayload(
                    name = "newName",
                    school = "newSchool",
                    city = "newCity",
                    validated = true
                )
            )
        )
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
    fun updateAssociationNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "updateAssociationNotExists")
        val repository = DatabaseAssociationRepository(database)
        assertEquals(
            false, repository.update(
                "associationId", UpdateAssociationPayload(
                    name = "newName",
                    school = "newSchool",
                    city = "newCity",
                    validated = true
                )
            )
        )
    }

    @Test
    fun updateAssociationExpiresAt() = runBlocking {
        val database = Database(protocol = "h2", name = "updateAssociationExpiresAt")
        val repository = DatabaseAssociationRepository(database)
        val association = repository.create(
            CreateAssociationPayload(
                "name", "school", "city",
                "email", "password", "firstname", "lastname"
            )
        )?.copy(
            expiresAt = tomorrow
        ) ?: fail("Unable to create association")
        assertEquals(true, repository.updateExpiresAt(association.id, tomorrow))
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
    fun updateAssociationExpiresAtNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "updateAssociationExpiresAtNotExists")
        val repository = DatabaseAssociationRepository(database)
        assertEquals(false, repository.updateExpiresAt("associationId", tomorrow))
    }

    @Test
    fun deleteAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteAssociation")
        val repository = DatabaseAssociationRepository(database)
        val association = repository.create(
            CreateAssociationPayload(
                "name", "school", "city",
                "email", "password", "firstname", "lastname"
            )
        ) ?: fail("Unable to create association")
        assertEquals(true, repository.delete(association.id))
        val count = database.dbQuery {
            Associations
                .selectAll()
                .count()
        }
        assertEquals(0, count)
    }

    @Test
    fun deleteAssociationNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteAssociationNotExists")
        val repository = DatabaseAssociationRepository(database)
        assertEquals(false, repository.delete("associationId"))
    }

    @Test
    fun getAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getAssociation")
        val repository = DatabaseAssociationRepository(database)
        val association = repository.create(
            CreateAssociationPayload(
                "name", "school", "city",
                "email", "password", "firstname", "lastname"
            )
        ) ?: fail("Unable to create association")
        val associationFromDatabase = repository.get(association.id)
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
        val association = repository.create(
            CreateAssociationPayload(
                "name", "school", "city",
                "email", "password", "firstname", "lastname"
            )
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
    fun getValidatedAssociations() = runBlocking {
        val database = Database(protocol = "h2", name = "getValidatedAssociations")
        val repository = DatabaseAssociationRepository(database)
        val association = repository.create(
            CreateAssociationPayload(
                "name", "school", "city",
                "email", "password", "firstname", "lastname"
            )
        )?.copy(validated = true) ?: fail("Unable to create association")
        repository.update(association.id, UpdateAssociationPayload("name", "school", "city", true))
        repository.create(
            CreateAssociationPayload(
                "name 2", "school", "city",
                "email", "password", "firstname", "lastname"
            )
        ) ?: fail("Unable to create association")
        val associations = repository.getValidatedAssociations()
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
    fun getAssociationsExpiringBefore() = runBlocking {
        val database = Database(protocol = "h2", name = "getAssociationsExpiringBefore")
        val repository = DatabaseAssociationRepository(database)
        val association = repository.create(
            CreateAssociationPayload(
                "name", "school", "city",
                "email", "password", "firstname", "lastname"
            )
        )?.copy(expiresAt = yesterday) ?: fail("Unable to create association")
        repository.create(
            CreateAssociationPayload(
                "name 2", "school", "city",
                "email", "password", "firstname", "lastname"
            )
        ) ?: fail("Unable to create association")
        repository.updateExpiresAt(association.id, yesterday)
        val associations = repository.getAssociationsExpiringBefore(now)
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
        val domainsRepository = DatabaseDomainsInAssociationsRepository(database)
        val association = repository.create(
            CreateAssociationPayload(
                "name", "school", "city",
                "email", "password", "firstname", "lastname"
            )
        ) ?: fail("Unable to create association")
        val domain = domainsRepository.create(CreateDomainInAssociationPayload("domain"), association.id)
            ?: fail("Unable to create domain")
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
    fun getCodeInEmail() = runBlocking {
        val database = Database(protocol = "h2", name = "getCodeInEmail")
        val repository = DatabaseAssociationRepository(database)
        val codeInEmail = repository.createCodeInEmail("email", "code", "associationId", tomorrow)
            ?: fail("Unable to create code in email")
        val codeInEmailFromDatabase = repository.getCodeInEmail(codeInEmail.code)
        assertEquals(codeInEmailFromDatabase?.email, codeInEmail.email)
        assertEquals(codeInEmailFromDatabase?.code, codeInEmail.code)
        assertEquals(codeInEmailFromDatabase?.associationId, codeInEmail.associationId)
        assertEquals(codeInEmailFromDatabase?.expiresAt, codeInEmail.expiresAt)
    }

    @Test
    fun getCodesInEmailsExpiringBefore() = runBlocking {
        val database = Database(protocol = "h2", name = "getCodesInEmailsExpiringBefore")
        val repository = DatabaseAssociationRepository(database)
        val codeInEmail = repository.createCodeInEmail("email", "code", "associationId", yesterday)
            ?: fail("Unable to create code in email")
        repository.createCodeInEmail("email2", "code", "associationId", tomorrow)
            ?: fail("Unable to create code in email")
        val codesInEmails = repository.getCodesInEmailsExpiringBefore(now)
        assertEquals(codesInEmails.size, 1)
        assertEquals(codesInEmails.first().email, codeInEmail.email)
        assertEquals(codesInEmails.first().code, codeInEmail.code)
        assertEquals(codesInEmails.first().associationId, codeInEmail.associationId)
        assertEquals(codesInEmails.first().expiresAt, codeInEmail.expiresAt)
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
        assertEquals(
            1,
            repository.updateCodeInEmail("email", "newCode", "newAssociationId", tomorrow)
        )
        val codeInEmailFromDatabase = database.dbQuery {
            CodesInEmails
                .selectAll()
                .map(CodesInEmails::toCodeInEmail)
                .singleOrNull()
        }
        assertEquals(codeInEmailFromDatabase?.email, codeInEmail.email)
        assertEquals(codeInEmailFromDatabase?.code, "newCode")
        assertEquals(codeInEmailFromDatabase?.associationId, "newAssociationId")
        assertEquals(codeInEmailFromDatabase?.expiresAt, codeInEmail.expiresAt)
    }

    @Test
    fun deleteCodeInEmail() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteCodeInEmail")
        val repository = DatabaseAssociationRepository(database)
        val codeInEmail = repository.createCodeInEmail("email", "code", "associationId", tomorrow)
            ?: fail("Unable to create code in email")
        repository.deleteCodeInEmail(codeInEmail.code)
        val count = database.dbQuery {
            CodesInEmails
                .selectAll()
                .count()
        }
        assertEquals(0, count)
    }

}
