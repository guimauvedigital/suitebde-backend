package me.nathanfallet.suitebde.database.clubs

import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.database.users.UsersDatabaseRepository
import me.nathanfallet.suitebde.models.clubs.CreateRoleInClubPayload
import me.nathanfallet.suitebde.models.clubs.CreateUserInClubPayload
import me.nathanfallet.suitebde.models.clubs.OptionalRoleInClubContext
import me.nathanfallet.suitebde.models.users.CreateUserPayload
import org.jetbrains.exposed.sql.selectAll
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class UsersInClubsDatabaseRepositoryTest {

    @Test
    fun getUsersInClub() = runBlocking {
        val database = Database(protocol = "h2", name = "getUsersInClub")
        val usersRepository = UsersDatabaseRepository(database)
        val rolesRepository = RolesInClubsDatabaseRepository(database)
        val repository = UsersInClubsDatabaseRepository(database, rolesRepository)
        val user = usersRepository.create(
            CreateUserPayload(
                "email", "password",
                "firstName", "lastName", false
            ),
            "associationId"
        ) ?: fail("Unable to create user")
        rolesRepository.create(
            CreateRoleInClubPayload(
                "name", admin = false, default = true
            ), "clubId"
        ) ?: fail("Unable to create role in club")
        val userInClub = repository.create(
            CreateUserInClubPayload(user.id),
            "clubId"
        ) ?: fail("Unable to create user in club")
        val userInClubFromDatabase = repository.list("clubId")
        assertEquals(1, userInClubFromDatabase.size)
        assertEquals(userInClubFromDatabase.first().userId, userInClub.userId)
        assertEquals(userInClubFromDatabase.first().clubId, userInClub.clubId)
        assertEquals(userInClubFromDatabase.first().roleId, userInClub.roleId)
    }

    @Test
    fun getUsersInClubNotInClub() = runBlocking {
        val database = Database(protocol = "h2", name = "getUsersInClubNotInClub")
        val usersRepository = UsersDatabaseRepository(database)
        val rolesRepository = RolesInClubsDatabaseRepository(database)
        val repository = UsersInClubsDatabaseRepository(database, rolesRepository)
        val user = usersRepository.create(
            CreateUserPayload(
                "email", "password",
                "firstName", "lastName", false
            ),
            "associationId"
        ) ?: fail("Unable to create user")
        rolesRepository.create(
            CreateRoleInClubPayload(
                "name", admin = false, default = true
            ), "clubId"
        ) ?: fail("Unable to create role in club")
        repository.create(
            CreateUserInClubPayload(user.id),
            "clubId"
        ) ?: fail("Unable to create user in club")
        val userInClubFromDatabase = repository.list("otherClubId")
        assertEquals(0, userInClubFromDatabase.size)
    }

    @Test
    fun createUserInClub() = runBlocking {
        val database = Database(protocol = "h2", name = "createUserInClub")
        val usersRepository = UsersDatabaseRepository(database)
        val rolesRepository = RolesInClubsDatabaseRepository(database)
        val repository = UsersInClubsDatabaseRepository(database, rolesRepository)
        val user = usersRepository.create(
            CreateUserPayload(
                "email", "password",
                "firstName", "lastName", false
            ),
            "associationId"
        ) ?: fail("Unable to create user")
        val roleInClub = rolesRepository.create(
            CreateRoleInClubPayload(
                "name", admin = false, default = true
            ), "clubId"
        ) ?: fail("Unable to create role in club")
        val userInClub = repository.create(
            CreateUserInClubPayload(user.id),
            "clubId"
        )
        val usersInClubFromDatabase = database.suspendedTransaction {
            UsersInClubs.selectAll().map {
                UsersInClubs.toUserInClub(it, null, null, roleInClub)
            }.singleOrNull()
        }
        assertEquals(usersInClubFromDatabase?.userId, userInClub?.userId)
        assertEquals(usersInClubFromDatabase?.clubId, userInClub?.clubId)
        assertEquals(usersInClubFromDatabase?.roleId, userInClub?.roleId)
        assertEquals(user.id, userInClub?.userId)
        assertEquals("clubId", userInClub?.clubId)
        assertEquals(roleInClub.id, userInClub?.roleId)
    }

    @Test
    fun createUserInClubForSpecifiedRole() = runBlocking {
        val database = Database(protocol = "h2", name = "createUserInClubForSpecifiedRole")
        val usersRepository = UsersDatabaseRepository(database)
        val rolesRepository = RolesInClubsDatabaseRepository(database)
        val repository = UsersInClubsDatabaseRepository(database, rolesRepository)
        val user = usersRepository.create(
            CreateUserPayload(
                "email", "password",
                "firstName", "lastName", false
            ),
            "associationId"
        ) ?: fail("Unable to create user")
        val roleInClub = rolesRepository.create(
            CreateRoleInClubPayload(
                "name", admin = false, default = true
            ), "clubId"
        ) ?: fail("Unable to create role in club")
        val userInClub = repository.create(
            CreateUserInClubPayload(user.id),
            "clubId",
            OptionalRoleInClubContext(roleInClub.id)
        )
        val usersInClubFromDatabase = database.suspendedTransaction {
            UsersInClubs.selectAll().map {
                UsersInClubs.toUserInClub(it, null, null, roleInClub)
            }.singleOrNull()
        }
        assertEquals(usersInClubFromDatabase?.userId, userInClub?.userId)
        assertEquals(usersInClubFromDatabase?.clubId, userInClub?.clubId)
        assertEquals(usersInClubFromDatabase?.roleId, userInClub?.roleId)
        assertEquals(user.id, userInClub?.userId)
        assertEquals("clubId", userInClub?.clubId)
        assertEquals(roleInClub.id, userInClub?.roleId)
    }

    @Test
    fun getUserInClub() = runBlocking {
        val database = Database(protocol = "h2", name = "getUserInClub")
        val usersRepository = UsersDatabaseRepository(database)
        val rolesRepository = RolesInClubsDatabaseRepository(database)
        val repository = UsersInClubsDatabaseRepository(database, rolesRepository)
        val user = usersRepository.create(
            CreateUserPayload(
                "email", "password",
                "firstName", "lastName", false
            ),
            "associationId"
        ) ?: fail("Unable to create user")
        rolesRepository.create(
            CreateRoleInClubPayload(
                "name", admin = false, default = true
            ), "clubId"
        ) ?: fail("Unable to create role in club")
        val userInClub = repository.create(
            CreateUserInClubPayload(user.id),
            "clubId"
        ) ?: fail("Unable to create user in club")
        val usersInClubFromDatabase = repository.get(user.id, "clubId")
        assertEquals(usersInClubFromDatabase?.userId, userInClub.userId)
        assertEquals(usersInClubFromDatabase?.clubId, userInClub.clubId)
        assertEquals(usersInClubFromDatabase?.roleId, userInClub.roleId)
    }

}
