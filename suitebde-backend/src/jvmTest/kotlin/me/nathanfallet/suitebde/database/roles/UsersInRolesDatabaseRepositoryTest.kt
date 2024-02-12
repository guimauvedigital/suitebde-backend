package me.nathanfallet.suitebde.database.roles

import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.database.users.UsersDatabaseRepository
import me.nathanfallet.suitebde.models.roles.CreateUserInRolePayload
import me.nathanfallet.suitebde.models.users.CreateUserPayload
import org.jetbrains.exposed.sql.selectAll
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class UsersInRolesDatabaseRepositoryTest {

    @Test
    fun getUsersInRole() = runBlocking {
        val database = Database(protocol = "h2", name = "getUsersInRole")
        val repository = UsersInRolesDatabaseRepository(database)
        val usersRepository = UsersDatabaseRepository(database)
        val user = usersRepository.create(
            CreateUserPayload(
                "email", "password",
                "firstName", "lastName", false
            ),
            "associationId"
        ) ?: fail("Unable to create user")
        val userInRole = repository.create(
            CreateUserInRolePayload(
                user.id
            ), "roleId"
        ) ?: fail("Unable to create user in role")
        val userInRoleFromDatabase = repository.list("roleId")
        assertEquals(1, userInRoleFromDatabase.size)
        assertEquals(userInRoleFromDatabase.first().userId, userInRole.userId)
        assertEquals(userInRoleFromDatabase.first().roleId, userInRole.roleId)
    }

    @Test
    fun getUsersInRoleNotInRole() = runBlocking {
        val database = Database(protocol = "h2", name = "getUsersInRoleNotInRole")
        val repository = UsersInRolesDatabaseRepository(database)
        val usersRepository = UsersDatabaseRepository(database)
        val user = usersRepository.create(
            CreateUserPayload(
                "email", "password",
                "firstName", "lastName", false
            ),
            "associationId"
        ) ?: fail("Unable to create user")
        repository.create(
            CreateUserInRolePayload(
                user.id
            ), "roleId"
        ) ?: fail("Unable to create user in role")
        val userInRoleFromDatabase = repository.list("otherRoleId")
        assertEquals(0, userInRoleFromDatabase.size)
    }

    @Test
    fun createUserInRole() = runBlocking {
        val database = Database(protocol = "h2", name = "createUserInRole")
        val repository = UsersInRolesDatabaseRepository(database)
        val userInRole = repository.create(
            CreateUserInRolePayload(
                "userId"
            ), "roleId"
        )
        val usersInRolesFromDatabase = database.suspendedTransaction {
            UsersInRoles.selectAll().map(UsersInRoles::toUserInRole).singleOrNull()
        }
        assertEquals(usersInRolesFromDatabase?.userId, userInRole?.userId)
        assertEquals(usersInRolesFromDatabase?.roleId, userInRole?.roleId)
        assertEquals("userId", userInRole?.userId)
        assertEquals("roleId", userInRole?.roleId)
    }

    @Test
    fun getUserInRole() = runBlocking {
        val database = Database(protocol = "h2", name = "getUserInRole")
        val repository = UsersInRolesDatabaseRepository(database)
        val usersRepository = UsersDatabaseRepository(database)
        val user = usersRepository.create(
            CreateUserPayload(
                "email", "password",
                "firstName", "lastName", false
            ),
            "associationId"
        ) ?: fail("Unable to create user")
        val userInRole = repository.create(
            CreateUserInRolePayload(
                user.id
            ), "roleId"
        ) ?: fail("Unable to create user in role")
        val userInRoleFromDatabase = repository.get(user.id, "roleId")
        assertEquals(userInRoleFromDatabase?.userId, userInRole.userId)
        assertEquals(userInRoleFromDatabase?.roleId, userInRole.roleId)
    }

    @Test
    fun getUserInRoleNotInRole() = runBlocking {
        val database = Database(protocol = "h2", name = "getUserInRoleNotInRole")
        val repository = UsersInRolesDatabaseRepository(database)
        UsersDatabaseRepository(database)
        repository.create(
            CreateUserInRolePayload(
                "userId"
            ), "roleId"
        ) ?: fail("Unable to create user in role")
        val userInRoleFromDatabase = repository.get("userId", "otherRoleId")
        assertEquals(userInRoleFromDatabase, null)
    }

    @Test
    fun getUserInRoleNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "getUserInRoleNotExists")
        val repository = UsersInRolesDatabaseRepository(database)
        UsersDatabaseRepository(database)
        val userInRoleFromDatabase = repository.get("id", "otherRoleId")
        assertEquals(userInRoleFromDatabase, null)
    }

    @Test
    fun deleteUserInRole() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteUserInRole")
        val repository = UsersInRolesDatabaseRepository(database)
        val usersRepository = UsersDatabaseRepository(database)
        val user = usersRepository.create(
            CreateUserPayload(
                "email", "password",
                "firstName", "lastName", false
            ),
            "associationId"
        ) ?: fail("Unable to create user")
        repository.create(
            CreateUserInRolePayload(
                user.id
            ), "roleId"
        ) ?: fail("Unable to create role")
        assertEquals(true, repository.delete(user.id, "roleId"))
        val userInRoleFromDatabase = repository.get(user.id, "roleId")
        assertEquals(userInRoleFromDatabase, null)
    }

    @Test
    fun deleteUserInRoleNotInRole() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteUserInRoleNotInRole")
        val repository = UsersInRolesDatabaseRepository(database)
        repository.create(
            CreateUserInRolePayload(
                "userId"
            ), "roleId"
        ) ?: fail("Unable to create role")
        assertEquals(false, repository.delete("userId", "otherRoleId"))
    }

    @Test
    fun deleteUserInRoleNotExits() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteUserInRoleNotExits")
        val repository = UsersInRolesDatabaseRepository(database)
        assertEquals(false, repository.delete("userId", "roleId"))
    }

}
