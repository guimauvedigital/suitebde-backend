package me.nathanfallet.suitebde.database.users

import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.database.Database
import org.jetbrains.exposed.sql.selectAll
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class DatabaseUsersRepositoryTest {

    @Test
    fun createUser() = runBlocking {
        val database = Database(protocol = "h2", name = "createUser")
        val repository = DatabaseUsersRepository(database)
        val user = repository.createUser("associationId", "email", "password", "firstName", "lastName", false)
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
        val user = repository.createUser("associationId", "email", "password", "firstName", "lastName", false)
            ?: fail("Unable to create user")
        val result = repository.getUser(user.id)
        assertEquals(user.id, result?.id)
        assertEquals(user.associationId, result?.associationId)
        assertEquals(user.email, result?.email)
        assertEquals(null, result?.password)
        assertEquals(user.firstName, result?.firstName)
        assertEquals(user.lastName, result?.lastName)
        assertEquals(user.superuser, result?.superuser)
    }

    @Test
    fun getUserForEmailInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getUserForEmailInAssociation")
        val repository = DatabaseUsersRepository(database)
        val user = repository.createUser("associationId", "email", "password", "firstName", "lastName", false)
            ?: fail("Unable to create user")
        val result = repository.getUserForEmailInAssociation(user.email, user.associationId, false)
        assertEquals(user.id, result?.id)
        assertEquals(user.associationId, result?.associationId)
        assertEquals(user.email, result?.email)
        assertEquals(null, result?.password)
        assertEquals(user.firstName, result?.firstName)
        assertEquals(user.lastName, result?.lastName)
        assertEquals(user.superuser, result?.superuser)
    }

    @Test
    fun getUserForEmailInAssociationWithPassword() = runBlocking {
        val database = Database(protocol = "h2", name = "getUserForEmailInAssociationWithPassword")
        val repository = DatabaseUsersRepository(database)
        val user = repository.createUser("associationId", "email", "password", "firstName", "lastName", false)
            ?: fail("Unable to create user")
        val result = repository.getUserForEmailInAssociation(user.email, user.associationId, true)
        assertEquals(user.id, result?.id)
        assertEquals(user.associationId, result?.associationId)
        assertEquals(user.email, result?.email)
        assertEquals("password", result?.password)
        assertEquals(user.firstName, result?.firstName)
        assertEquals(user.lastName, result?.lastName)
        assertEquals(user.superuser, result?.superuser)
    }

}