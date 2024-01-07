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

class AssociationsDatabaseRepositoryTest {

    private val now = Clock.System.now()
    private val tomorrow = now.plus(1, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
    private val yesterday = now.minus(1, DateTimeUnit.DAY, TimeZone.currentSystemDefault())

    @Test
    fun createAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "createAssociation")
        val repository = AssociationsDatabaseRepository(database)
        val association = repository.create(
            CreateAssociationPayload(
                "name", "school", "city",
                "email", "password", "firstname", "lastname"
            )
        )
        val associationFromDatabase = database.suspendedTransaction {
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
        val repository = AssociationsDatabaseRepository(database)
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
        val associationFromDatabase = database.suspendedTransaction {
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
        val repository = AssociationsDatabaseRepository(database)
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
        val repository = AssociationsDatabaseRepository(database)
        val association = repository.create(
            CreateAssociationPayload(
                "name", "school", "city",
                "email", "password", "firstname", "lastname"
            )
        )?.copy(
            expiresAt = tomorrow
        ) ?: fail("Unable to create association")
        assertEquals(true, repository.updateExpiresAt(association.id, tomorrow))
        val associationFromDatabase = database.suspendedTransaction {
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
        val repository = AssociationsDatabaseRepository(database)
        assertEquals(false, repository.updateExpiresAt("associationId", tomorrow))
    }

    @Test
    fun deleteAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteAssociation")
        val repository = AssociationsDatabaseRepository(database)
        val association = repository.create(
            CreateAssociationPayload(
                "name", "school", "city",
                "email", "password", "firstname", "lastname"
            )
        ) ?: fail("Unable to create association")
        assertEquals(true, repository.delete(association.id))
        val count = database.suspendedTransaction {
            Associations
                .selectAll()
                .count()
        }
        assertEquals(0, count)
    }

    @Test
    fun deleteAssociationNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteAssociationNotExists")
        val repository = AssociationsDatabaseRepository(database)
        assertEquals(false, repository.delete("associationId"))
    }

    @Test
    fun getAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getAssociation")
        val repository = AssociationsDatabaseRepository(database)
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
        val repository = AssociationsDatabaseRepository(database)
        val association = repository.create(
            CreateAssociationPayload(
                "name", "school", "city",
                "email", "password", "firstname", "lastname"
            )
        ) ?: fail("Unable to create association")
        val associations = repository.list()
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
    fun getAssociationsLimitOffset() = runBlocking {
        val database = Database(protocol = "h2", name = "getAssociationsLimitOffset")
        val repository = AssociationsDatabaseRepository(database)
        for (i in 1..4) repository.create(
            CreateAssociationPayload(
                "name $i", "school", "city",
                "email", "password", "firstname", "lastname"
            )
        )
        val associations = repository.list(1, 2)
        assertEquals(associations.size, 1)
    }

    @Test
    fun getValidatedAssociations() = runBlocking {
        val database = Database(protocol = "h2", name = "getValidatedAssociations")
        val repository = AssociationsDatabaseRepository(database)
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
        val repository = AssociationsDatabaseRepository(database)
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
        val repository = AssociationsDatabaseRepository(database)
        val domainsRepository = DomainsInAssociationsDatabaseRepository(database)
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

}
