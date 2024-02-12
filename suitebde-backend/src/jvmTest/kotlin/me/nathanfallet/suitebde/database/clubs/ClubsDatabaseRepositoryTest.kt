package me.nathanfallet.suitebde.database.clubs

import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.models.clubs.CreateClubPayload
import me.nathanfallet.suitebde.models.clubs.UpdateClubPayload
import me.nathanfallet.suitebde.models.users.OptionalUserContext
import org.jetbrains.exposed.sql.JoinType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class ClubsDatabaseRepositoryTest {

    @Test
    fun getClubsInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getClubsInAssociation")
        val repository = ClubsDatabaseRepository(database)
        val club = repository.create(
            CreateClubPayload(
                "name",
                "description",
                "logo"
            ),
            "associationId",
        ) ?: fail("Unable to create club")
        val clubFromDatabase = repository.list("associationId")
        assertEquals(1, clubFromDatabase.size)
        assertEquals(clubFromDatabase.first().id, club.id)
        assertEquals(clubFromDatabase.first().associationId, club.associationId)
        assertEquals(clubFromDatabase.first().name, club.name)
        assertEquals(clubFromDatabase.first().description, club.description)
        assertEquals(clubFromDatabase.first().logo, club.logo)
        assertEquals(clubFromDatabase.first().validated, club.validated)
        assertEquals(clubFromDatabase.first().usersCount, club.usersCount)
    }

    @Test
    fun getClubsInAssociationNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getClubsInAssociationNotInAssociation")
        val repository = ClubsDatabaseRepository(database)
        repository.create(
            CreateClubPayload(
                "name",
                "description",
                "logo"
            ),
            "associationId",
        ) ?: fail("Unable to create club")
        val clubFromDatabase = repository.list("otherAssociationId")
        assertEquals(0, clubFromDatabase.size)
    }

    @Test
    fun createClub() = runBlocking {
        val database = Database(protocol = "h2", name = "createClub")
        val repository = ClubsDatabaseRepository(database)
        val club = repository.create(
            CreateClubPayload(
                "name",
                "description",
                "logo"
            ),
            "associationId",
        )
        val clubsFromDatabase = database.transaction {
            Clubs
                .join(UsersInClubs, JoinType.LEFT, Clubs.id, UsersInClubs.clubId)
                .select(Clubs.columns + Clubs.usersCount)
                .groupBy(Clubs.id)
                .map(Clubs::toClub)
                .singleOrNull()
        }
        assertEquals(clubsFromDatabase?.id, club?.id)
        assertEquals(clubsFromDatabase?.associationId, club?.associationId)
        assertEquals(clubsFromDatabase?.name, club?.name)
        assertEquals(clubsFromDatabase?.description, club?.description)
        assertEquals(clubsFromDatabase?.logo, club?.logo)
        assertEquals(clubsFromDatabase?.usersCount, club?.usersCount)
        assertEquals(clubsFromDatabase?.validated, club?.validated)
        assertEquals("associationId", club?.associationId)
        assertEquals("name", club?.name)
        assertEquals("description", club?.description)
        assertEquals("logo", club?.logo)
        assertEquals(0, club?.usersCount)
        assertEquals(false, club?.validated)
    }

    @Test
    fun getClub() = runBlocking {
        val database = Database(protocol = "h2", name = "getClub")
        val repository = ClubsDatabaseRepository(database)
        val club = repository.create(
            CreateClubPayload(
                "name",
                "description",
                "logo"
            ),
            "associationId",
        ) ?: fail("Unable to create club")
        val clubFromDatabase = repository.get(club.id, "associationId")
        assertEquals(clubFromDatabase?.id, club.id)
        assertEquals(clubFromDatabase?.associationId, club.associationId)
        assertEquals(clubFromDatabase?.name, club.name)
        assertEquals(clubFromDatabase?.description, club.description)
        assertEquals(clubFromDatabase?.logo, club.logo)
        assertEquals(clubFromDatabase?.validated, club.validated)
        assertEquals(clubFromDatabase?.usersCount, club.usersCount)
    }

    @Test
    fun getClubWithUserContext() = runBlocking {
        val database = Database(protocol = "h2", name = "getClubWithUserContext")
        val repository = ClubsDatabaseRepository(database)
        val club = repository.create(
            CreateClubPayload(
                "name",
                "description",
                "logo"
            ),
            "associationId",
            OptionalUserContext("userId")
        ) ?: fail("Unable to create club")
        val clubFromDatabase = repository.get(club.id, "associationId", OptionalUserContext("userId"))
        assertEquals(clubFromDatabase?.isMember, club.isMember)
        assertEquals(false, club.isMember)
    }

    @Test
    fun getClubNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getClubNotInAssociation")
        val repository = ClubsDatabaseRepository(database)
        val club = repository.create(
            CreateClubPayload(
                "name",
                "description",
                "logo"
            ),
            "associationId",
        ) ?: fail("Unable to create club")
        val clubFromDatabase = repository.get(club.id, "otherAssociationId")
        assertEquals(null, clubFromDatabase)
    }

    @Test
    fun getClubNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "getClubNotExists")
        val repository = ClubsDatabaseRepository(database)
        val clubFromDatabase = repository.get("id", "associationId")
        assertEquals(null, clubFromDatabase)
    }

    @Test
    fun updateClub() = runBlocking {
        val database = Database(protocol = "h2", name = "updateClub")
        val repository = ClubsDatabaseRepository(database)
        val club = repository.create(
            CreateClubPayload(
                "name",
                "description",
                "logo"
            ),
            "associationId",
        ) ?: fail("Unable to create club")
        val payload = UpdateClubPayload("newName", "newDescription", "newLogo")
        assertEquals(true, repository.update(club.id, payload, "associationId"))
        val clubFromDatabase = repository.get(club.id, "associationId")
        assertEquals(clubFromDatabase?.name, "newName")
        assertEquals(clubFromDatabase?.description, "newDescription")
        assertEquals(clubFromDatabase?.logo, "newLogo")
    }

    @Test
    fun updateClubNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "updateClubNotInAssociation")
        val repository = ClubsDatabaseRepository(database)
        val club = repository.create(
            CreateClubPayload(
                "name",
                "description",
                "logo"
            ),
            "associationId",
        ) ?: fail("Unable to create club")
        val payload = UpdateClubPayload("newName", "newDescription", "newLogo")
        assertEquals(false, repository.update(club.id, payload, "otherAssociationId"))
    }

    @Test
    fun updateClubNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "updateClubNotExists")
        val repository = ClubsDatabaseRepository(database)
        val payload = UpdateClubPayload("newName", "newDescription", "newLogo")
        assertEquals(false, repository.update("id", payload, "associationId"))
    }

    @Test
    fun deleteClub() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteClub")
        val repository = ClubsDatabaseRepository(database)
        val club = repository.create(
            CreateClubPayload(
                "name",
                "description",
                "logo"
            ),
            "associationId",
        ) ?: fail("Unable to create club")
        repository.delete(club.id, "associationId")
        val clubFromDatabase = repository.get(club.id, "associationId")
        assertEquals(null, clubFromDatabase)
    }

    @Test
    fun deleteClubNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteClubNotInAssociation")
        val repository = ClubsDatabaseRepository(database)
        val club = repository.create(
            CreateClubPayload(
                "name",
                "description",
                "logo"
            ),
            "associationId",
        ) ?: fail("Unable to create club")
        assertEquals(false, repository.delete(club.id, "otherAssociationId"))
    }

    @Test
    fun deleteClubNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteClubNotExists")
        val repository = ClubsDatabaseRepository(database)
        assertEquals(false, repository.delete("id", "associationId"))
    }

}
