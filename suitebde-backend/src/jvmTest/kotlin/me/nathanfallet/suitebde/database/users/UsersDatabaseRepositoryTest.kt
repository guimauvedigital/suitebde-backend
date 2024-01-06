package me.nathanfallet.suitebde.database.users

import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import org.jetbrains.exposed.sql.selectAll
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class UsersDatabaseRepositoryTest {

    @Test
    fun createUser() = runBlocking {
        val database = Database(protocol = "h2", name = "createUser")
        val repository = UsersDatabaseRepository(database)
        val user = repository.create(
            CreateUserPayload(
                "email", "password",
                "firstName", "lastName", false
            ),
            "associationId"
        )
        val userFromDatabase = database.dbQuery {
            Users
                .selectAll()
                .map {
                    Users.toUser(it, true)
                }
                .singleOrNull()
        }
        assertEquals(userFromDatabase?.id, user?.id)
        assertEquals(userFromDatabase?.associationId, user?.associationId)
        assertEquals(userFromDatabase?.email, user?.email)
        assertEquals(null, user?.password)
        assertEquals(userFromDatabase?.firstName, user?.firstName)
        assertEquals(userFromDatabase?.lastName, user?.lastName)
        assertEquals(userFromDatabase?.superuser, user?.superuser)
        assertEquals(userFromDatabase?.associationId, "associationId")
        assertEquals(userFromDatabase?.email, "email")
        assertEquals(userFromDatabase?.password, "password")
        assertEquals(userFromDatabase?.firstName, "firstName")
        assertEquals(userFromDatabase?.lastName, "lastName")
        assertEquals(userFromDatabase?.superuser, false)
    }

    @Test
    fun getUser() = runBlocking {
        val database = Database(protocol = "h2", name = "getUser")
        val repository = UsersDatabaseRepository(database)
        val user = repository.create(
            CreateUserPayload(
                "email", "password",
                "firstName", "lastName", false
            ),
            "associationId"
        ) ?: fail("Unable to create user")
        val result = repository.get(user.id, "associationId")
        assertEquals(user.id, result?.id)
        assertEquals(user.associationId, result?.associationId)
        assertEquals(user.email, result?.email)
        assertEquals(null, result?.password)
        assertEquals(user.firstName, result?.firstName)
        assertEquals(user.lastName, result?.lastName)
        assertEquals(user.superuser, result?.superuser)
    }

    @Test
    fun getUserNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getUserNotInAssociation")
        val repository = UsersDatabaseRepository(database)
        val user = repository.create(
            CreateUserPayload(
                "email", "password",
                "firstName", "lastName", false
            ),
            "associationId"
        ) ?: fail("Unable to create user")
        assertEquals(null, repository.get(user.id, "otherAssociationId"))
    }

    @Test
    fun getUserWithoutAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getUserWithoutAssociation")
        val repository = UsersDatabaseRepository(database)
        val user = repository.create(
            CreateUserPayload(
                "email", "password",
                "firstName", "lastName", false
            ),
            "associationId"
        ) ?: fail("Unable to create user")
        val result = repository.get(user.id)
        assertEquals(user.id, result?.id)
        assertEquals(user.associationId, result?.associationId)
        assertEquals(user.email, result?.email)
        assertEquals(null, result?.password)
        assertEquals(user.firstName, result?.firstName)
        assertEquals(user.lastName, result?.lastName)
        assertEquals(user.superuser, result?.superuser)
    }

    @Test
    fun getUserForEmail() = runBlocking {
        val database = Database(protocol = "h2", name = "getUserForEmail")
        val repository = UsersDatabaseRepository(database)
        val user = repository.create(
            CreateUserPayload(
                "email", "password",
                "firstName", "lastName", false
            ),
            "associationId"
        ) ?: fail("Unable to create user")
        val result = repository.getForEmail(user.email, false)
        assertEquals(user.id, result?.id)
        assertEquals(user.associationId, result?.associationId)
        assertEquals(user.email, result?.email)
        assertEquals(null, result?.password)
        assertEquals(user.firstName, result?.firstName)
        assertEquals(user.lastName, result?.lastName)
        assertEquals(user.superuser, result?.superuser)
    }

    @Test
    fun getUserForEmailWithPassword() = runBlocking {
        val database = Database(protocol = "h2", name = "getUserForEmailWithPassword")
        val repository = UsersDatabaseRepository(database)
        val user = repository.create(
            CreateUserPayload(
                "email", "password",
                "firstName", "lastName", false
            ),
            "associationId"
        ) ?: fail("Unable to create user")
        val result = repository.getForEmail(user.email, true)
        assertEquals(user.id, result?.id)
        assertEquals(user.associationId, result?.associationId)
        assertEquals(user.email, result?.email)
        assertEquals("password", result?.password)
        assertEquals(user.firstName, result?.firstName)
        assertEquals(user.lastName, result?.lastName)
        assertEquals(user.superuser, result?.superuser)
    }

    @Test
    fun getUsersInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getUsersInAssociation")
        val repository = UsersDatabaseRepository(database)
        val user = repository.create(
            CreateUserPayload(
                "email", "password",
                "firstName", "lastName", false
            ),
            "associationId"
        ) ?: fail("Unable to create user")
        val result = repository.list(user.associationId)
        assertEquals(1, result.size)
        assertEquals(user.id, result.first().id)
        assertEquals(user.associationId, result.first().associationId)
        assertEquals(user.email, result.first().email)
        assertEquals(null, result.first().password)
        assertEquals(user.firstName, result.first().firstName)
        assertEquals(user.lastName, result.first().lastName)
        assertEquals(user.superuser, result.first().superuser)
    }

    @Test
    fun getUsersInAssociationLimitOffset() = runBlocking {
        val database = Database(protocol = "h2", name = "getUsersInAssociationLimitOffset")
        val repository = UsersDatabaseRepository(database)
        for (i in 1..4) repository.create(
            CreateUserPayload(
                "email $i", "password",
                "firstName", "lastName", false
            ),
            "associationId"
        ) ?: fail("Unable to create user")
        val result = repository.list(1, 2, "associationId")
        assertEquals(1, result.size)
    }

    @Test
    fun getUsersInAssociationNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getUsersInAssociationNotInAssociation")
        val repository = UsersDatabaseRepository(database)
        repository.create(
            CreateUserPayload(
                "email", "password",
                "firstName", "lastName", false
            ),
            "associationId"
        ) ?: fail("Unable to create user")
        assertEquals(listOf(), repository.list("otherAssociationId"))
    }

    @Test
    fun updateUser() = runBlocking {
        val database = Database(protocol = "h2", name = "updateUser")
        val repository = UsersDatabaseRepository(database)
        val user = repository.create(
            CreateUserPayload(
                "email", "password",
                "firstName", "lastName", false
            ),
            "associationId"
        ) ?: fail("Unable to create user")
        val updatedUser = user.copy(
            password = "password2",
            firstName = "firstName2",
            lastName = "lastName2"
        )
        val payload = UpdateUserPayload("firstName2", "lastName2", "password2")
        assertEquals(true, repository.update(user.id, payload, "associationId"))
        val userFromDatabase = database.dbQuery {
            Users
                .selectAll()
                .map {
                    Users.toUser(it, true)
                }
                .singleOrNull()
        }
        assertEquals(userFromDatabase?.id, updatedUser.id)
        assertEquals(userFromDatabase?.associationId, updatedUser.associationId)
        assertEquals(userFromDatabase?.email, updatedUser.email)
        assertEquals(userFromDatabase?.password, updatedUser.password)
        assertEquals(userFromDatabase?.firstName, updatedUser.firstName)
        assertEquals(userFromDatabase?.lastName, updatedUser.lastName)
        assertEquals(userFromDatabase?.superuser, updatedUser.superuser)
    }

    @Test
    fun updateUserNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "updateUserNotInAssociation")
        val repository = UsersDatabaseRepository(database)
        val user = repository.create(
            CreateUserPayload(
                "email", "password",
                "firstName", "lastName", false
            ),
            "associationId"
        ) ?: fail("Unable to create user")
        val payload = UpdateUserPayload("firstName2", "lastName2", "password2")
        assertEquals(false, repository.update(user.id, payload, "otherAssociationId"))
    }

    @Test
    fun updateUserNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "updateUserNotExists")
        val repository = UsersDatabaseRepository(database)
        val payload = UpdateUserPayload("firstName2", "lastName2", "password2")
        assertEquals(false, repository.update("userId", payload, "associationId"))
    }

    @Test
    fun deleteUser() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteUser")
        val repository = UsersDatabaseRepository(database)
        val user = repository.create(
            CreateUserPayload(
                "email", "password",
                "firstName", "lastName", false
            ),
            "associationId"
        ) ?: fail("Unable to create user")
        assertEquals(true, repository.delete(user.id, "associationId"))
        val count = database.dbQuery {
            Users
                .selectAll()
                .count()
        }
        assertEquals(0, count)
    }

    @Test
    fun deleteUserNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteUserNotInAssociation")
        val repository = UsersDatabaseRepository(database)
        val user = repository.create(
            CreateUserPayload(
                "email", "password",
                "firstName", "lastName", false
            ),
            "associationId"
        ) ?: fail("Unable to create user")
        assertEquals(false, repository.delete(user.id, "otherAssociationId"))
        val count = database.dbQuery {
            Users
                .selectAll()
                .count()
        }
        assertEquals(1, count)
    }

    @Test
    fun deleteUserNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteUserNotExists")
        val repository = UsersDatabaseRepository(database)
        assertEquals(false, repository.delete("userId", "associationId"))
    }

}
