package me.nathanfallet.suitebde.database.users

import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import org.jetbrains.exposed.sql.selectAll
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class DatabaseUsersRepositoryTest {

    @Test
    fun createUser() = runBlocking {
        val database = Database(protocol = "h2", name = "createUser")
        val repository = DatabaseUsersRepository(database)
        val user = repository.create(
            CreateUserPayload(
                "associationId", "email", "password",
                "firstName", "lastName", false
            )
        )
        val userFromDatabase = database.dbQuery {
            Users
                .selectAll()
                .map(Users::toUser)
                .singleOrNull()
        }
        assertEquals(userFromDatabase?.id, user?.id)
        assertEquals(userFromDatabase?.associationId, user?.associationId)
        assertEquals(userFromDatabase?.email, user?.email)
        assertEquals(null, user?.password)
        assertEquals(userFromDatabase?.firstName, user?.firstName)
        assertEquals(userFromDatabase?.lastName, user?.lastName)
        assertEquals(userFromDatabase?.superuser, user?.superuser)
    }

    @Test
    fun getUser() = runBlocking {
        val database = Database(protocol = "h2", name = "getUser")
        val repository = DatabaseUsersRepository(database)
        val user = repository.create(
            CreateUserPayload(
                "associationId", "email", "password",
                "firstName", "lastName", false
            )
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
        val repository = DatabaseUsersRepository(database)
        val user = repository.create(
            CreateUserPayload(
                "associationId", "email", "password",
                "firstName", "lastName", false
            )
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
        val repository = DatabaseUsersRepository(database)
        val user = repository.create(
            CreateUserPayload(
                "associationId", "email", "password",
                "firstName", "lastName", false
            )
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
        val repository = DatabaseUsersRepository(database)
        val user = repository.create(
            CreateUserPayload(
                "associationId", "email", "password",
                "firstName", "lastName", false
            )
        ) ?: fail("Unable to create user")
        val result = repository.getInAssociation(user.associationId)
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
    fun updateUser() = runBlocking {
        val database = Database(protocol = "h2", name = "updateUser")
        val repository = DatabaseUsersRepository(database)
        val user = repository.create(
            CreateUserPayload(
                "associationId", "email", "password",
                "firstName", "lastName", false
            )
        ) ?: fail("Unable to create user")
        val updatedUser = user.copy(
            password = "password2",
            firstName = "firstName2",
            lastName = "lastName2"
        )
        val payload = UpdateUserPayload("firstName2", "lastName2", "password2")
        assertEquals(true, repository.update(user.id, payload))
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

}