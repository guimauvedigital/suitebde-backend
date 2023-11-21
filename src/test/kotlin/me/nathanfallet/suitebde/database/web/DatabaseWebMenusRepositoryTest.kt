package me.nathanfallet.suitebde.database.web

import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.models.web.CreateWebMenuPayload
import me.nathanfallet.suitebde.models.web.UpdateWebMenuPayload
import org.jetbrains.exposed.sql.selectAll
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class DatabaseWebMenusRepositoryTest {

    @Test
    fun getWebMenusInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getWebMenusInAssociation")
        val repository = DatabaseWebMenusRepository(database)
        val menu = repository.create(
            CreateWebMenuPayload(
                "title", "url", 42
            ), "associationId"
        ) ?: fail("Unable to create menu")
        val menuFromDatabase = repository.list("associationId")
        assertEquals(1, menuFromDatabase.size)
        assertEquals(menuFromDatabase.first().id, menu.id)
        assertEquals(menuFromDatabase.first().associationId, menu.associationId)
        assertEquals(menuFromDatabase.first().title, menu.title)
        assertEquals(menuFromDatabase.first().url, menu.url)
        assertEquals(menuFromDatabase.first().position, menu.position)
    }

    @Test
    fun getWebMenusInAssociationLimitOffset() = runBlocking {
        val database = Database(protocol = "h2", name = "getWebMenusInAssociationLimitOffset")
        val repository = DatabaseWebMenusRepository(database)
        for (i in 1..4) repository.create(
            CreateWebMenuPayload(
                "title $i", "url", 42
            ), "associationId"
        )
        val menuFromDatabase = repository.list(1, 2, "associationId")
        assertEquals(1, menuFromDatabase.size)
    }

    @Test
    fun getWebMenusInAssociationNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getWebMenusInAssociationNotInAssociation")
        val repository = DatabaseWebMenusRepository(database)
        repository.create(
            CreateWebMenuPayload(
                "title", "url", 42
            ), "associationId"
        ) ?: fail("Unable to create menu")
        val menuFromDatabase = repository.list("otherAssociationId")
        assertEquals(0, menuFromDatabase.size)
    }

    @Test
    fun createWebMenu() = runBlocking {
        val database = Database(protocol = "h2", name = "createWebMenu")
        val repository = DatabaseWebMenusRepository(database)
        val menu = repository.create(
            CreateWebMenuPayload(
                "title", "url", 42
            ), "associationId"
        )
        val menuFromDatabase = database.dbQuery {
            WebMenus
                .selectAll()
                .map(WebMenus::toWebMenu)
                .singleOrNull()
        }
        assertEquals(menuFromDatabase?.id, menu?.id)
        assertEquals(menuFromDatabase?.associationId, menu?.associationId)
        assertEquals(menuFromDatabase?.title, menu?.title)
        assertEquals(menuFromDatabase?.url, menu?.url)
        assertEquals(menuFromDatabase?.position, menu?.position)
        assertEquals(menuFromDatabase?.associationId, "associationId")
        assertEquals(menuFromDatabase?.title, "title")
        assertEquals(menuFromDatabase?.url, "url")
        assertEquals(menuFromDatabase?.position, 42)
    }

    @Test
    fun createWebMenuDefaultPosition() = runBlocking {
        val database = Database(protocol = "h2", name = "createWebMenuDefaultPosition")
        val repository = DatabaseWebMenusRepository(database)
        val menu = repository.create(
            CreateWebMenuPayload(
                "title", "url"
            ), "associationId"
        )
        val menuFromDatabase = database.dbQuery {
            WebMenus
                .selectAll()
                .map(WebMenus::toWebMenu)
                .singleOrNull()
        }
        assertEquals(menuFromDatabase?.position, menu?.position)
        assertEquals(menuFromDatabase?.position, 1)
    }

    @Test
    fun getWebMenu() = runBlocking {
        val database = Database(protocol = "h2", name = "getWebMenu")
        val repository = DatabaseWebMenusRepository(database)
        val menu = repository.create(
            CreateWebMenuPayload(
                "title", "url", 42
            ), "associationId"
        ) ?: fail("Unable to create menu")
        val menuFromDatabase = repository.get(menu.id, menu.associationId)
        assertEquals(menuFromDatabase?.id, menu.id)
        assertEquals(menuFromDatabase?.associationId, menu.associationId)
        assertEquals(menuFromDatabase?.title, menu.title)
        assertEquals(menuFromDatabase?.url, menu.url)
        assertEquals(menuFromDatabase?.position, menu.position)
    }

    @Test
    fun getWebMenuNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getWebMenuNotInAssociation")
        val repository = DatabaseWebMenusRepository(database)
        val menu = repository.create(
            CreateWebMenuPayload(
                "title", "url", 42
            ), "associationId"
        ) ?: fail("Unable to create menu")
        val menuFromDatabase = repository.get(menu.id, "otherAssociationId")
        assertEquals(menuFromDatabase, null)
    }

    @Test
    fun getWebMenuNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "getWebMenuNotExists")
        val repository = DatabaseWebMenusRepository(database)
        val menuFromDatabase = repository.get("id", "associationId")
        assertEquals(menuFromDatabase, null)
    }

    @Test
    fun updateWebMenu() = runBlocking {
        val database = Database(protocol = "h2", name = "updateWebMenu")
        val repository = DatabaseWebMenusRepository(database)
        val menu = repository.create(
            CreateWebMenuPayload(
                "title", "url", 42
            ), "associationId"
        ) ?: fail("Unable to create menu")
        val payload = UpdateWebMenuPayload("newTitle", "newUrl", 43)
        val updatedMenu = menu.copy(
            title = payload.title,
            url = payload.url,
            position = payload.position
        )
        assertEquals(true, repository.update(menu.id, payload, menu.associationId))
        val menuFromDatabase = repository.get(menu.id, menu.associationId)
        assertEquals(menuFromDatabase?.id, menu.id)
        assertEquals(menuFromDatabase?.associationId, menu.associationId)
        assertEquals(menuFromDatabase?.title, updatedMenu.title)
        assertEquals(menuFromDatabase?.url, updatedMenu.url)
        assertEquals(menuFromDatabase?.position, updatedMenu.position)
    }

    @Test
    fun updateWebMenuNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "updateWebMenuNotInAssociation")
        val repository = DatabaseWebMenusRepository(database)
        val menu = repository.create(
            CreateWebMenuPayload(
                "title", "url", 42
            ), "associationId"
        ) ?: fail("Unable to create menu")
        val payload = UpdateWebMenuPayload("newTitle", "newUrl", 43)
        assertEquals(false, repository.update(menu.id, payload, "otherAssociationId"))
    }

    @Test
    fun updateWebMenuNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "updateWebMenuNotExists")
        val repository = DatabaseWebMenusRepository(database)
        val payload = UpdateWebMenuPayload("newTitle", "newUrl", 43)
        assertEquals(false, repository.update("id", payload, "associationId"))
    }

    @Test
    fun deleteWebMenu() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteWebMenu")
        val repository = DatabaseWebMenusRepository(database)
        val menu = repository.create(
            CreateWebMenuPayload(
                "title", "url", 42
            ), "associationId"
        ) ?: fail("Unable to create menu")
        assertEquals(true, repository.delete(menu.id, menu.associationId))
        val count = database.dbQuery {
            WebMenus
                .selectAll()
                .count()
        }
        assertEquals(0, count)
    }

    @Test
    fun deleteWebMenuNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteWebMenuNotInAssociation")
        val repository = DatabaseWebMenusRepository(database)
        val menu = repository.create(
            CreateWebMenuPayload(
                "title", "url", 42
            ), "associationId"
        ) ?: fail("Unable to create menu")
        assertEquals(false, repository.delete(menu.id, "otherAssociationId"))
        val count = database.dbQuery {
            WebMenus
                .selectAll()
                .count()
        }
        assertEquals(1, count)
    }

    @Test
    fun deleteWebMenuNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteWebMenuNotExists")
        val repository = DatabaseWebMenusRepository(database)
        assertEquals(false, repository.delete("id", "associationId"))
    }

}
