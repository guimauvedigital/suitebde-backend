package me.nathanfallet.suitebde.database.users

import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.database.Database
import org.jetbrains.exposed.sql.selectAll
import kotlin.test.Test
import kotlin.test.assertEquals

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

}