package com.suitebde.database.web

import com.suitebde.database.Database
import com.suitebde.models.web.CreateWebMenuPayload
import com.suitebde.models.web.UpdateWebMenuPayload
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.selectAll
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class WebMenusDatabaseRepositoryTest {

    @Test
    fun getWebMenusInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getWebMenusInAssociation")
        val repository = WebMenusDatabaseRepository(database)
        val associationId = UUID()
        val menu = repository.create(
            CreateWebMenuPayload(
                "title", "url", 42
            ), associationId
        ) ?: fail("Unable to create menu")
        val menuFromDatabase = repository.list(associationId)
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
        val repository = WebMenusDatabaseRepository(database)
        val associationId = UUID()
        for (i in 1..4) repository.create(
            CreateWebMenuPayload(
                "title $i", "url", 42
            ), associationId
        )
        val menuFromDatabase = repository.list(Pagination(1, 2), associationId)
        assertEquals(1, menuFromDatabase.size)
    }

    @Test
    fun getWebMenusInAssociationNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getWebMenusInAssociationNotInAssociation")
        val repository = WebMenusDatabaseRepository(database)
        repository.create(
            CreateWebMenuPayload(
                "title", "url", 42
            ), UUID()
        ) ?: fail("Unable to create menu")
        val menuFromDatabase = repository.list(UUID())
        assertEquals(0, menuFromDatabase.size)
    }

    @Test
    fun createWebMenu() = runBlocking {
        val database = Database(protocol = "h2", name = "createWebMenu")
        val repository = WebMenusDatabaseRepository(database)
        val associationId = UUID()
        val menu = repository.create(
            CreateWebMenuPayload(
                "title", "url", 42
            ), associationId
        )
        val menuFromDatabase = database.suspendedTransaction {
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
        assertEquals(menuFromDatabase?.associationId, associationId)
        assertEquals(menuFromDatabase?.title, "title")
        assertEquals(menuFromDatabase?.url, "url")
        assertEquals(menuFromDatabase?.position, 42)
    }

    @Test
    fun createWebMenuDefaultPosition() = runBlocking {
        val database = Database(protocol = "h2", name = "createWebMenuDefaultPosition")
        val repository = WebMenusDatabaseRepository(database)
        val menu = repository.create(
            CreateWebMenuPayload(
                "title", "url"
            ), UUID()
        )
        val menuFromDatabase = database.suspendedTransaction {
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
        val repository = WebMenusDatabaseRepository(database)
        val menu = repository.create(
            CreateWebMenuPayload(
                "title", "url", 42
            ), UUID()
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
        val repository = WebMenusDatabaseRepository(database)
        val menu = repository.create(
            CreateWebMenuPayload(
                "title", "url", 42
            ), UUID()
        ) ?: fail("Unable to create menu")
        val menuFromDatabase = repository.get(menu.id, UUID())
        assertEquals(menuFromDatabase, null)
    }

    @Test
    fun getWebMenuNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "getWebMenuNotExists")
        val repository = WebMenusDatabaseRepository(database)
        val menuFromDatabase = repository.get(UUID(), UUID())
        assertEquals(menuFromDatabase, null)
    }

    @Test
    fun updateWebMenu() = runBlocking {
        val database = Database(protocol = "h2", name = "updateWebMenu")
        val repository = WebMenusDatabaseRepository(database)
        val menu = repository.create(
            CreateWebMenuPayload(
                "title", "url", 42
            ), UUID()
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
        val repository = WebMenusDatabaseRepository(database)
        val menu = repository.create(
            CreateWebMenuPayload(
                "title", "url", 42
            ), UUID()
        ) ?: fail("Unable to create menu")
        val payload = UpdateWebMenuPayload("newTitle", "newUrl", 43)
        assertEquals(false, repository.update(menu.id, payload, UUID()))
    }

    @Test
    fun updateWebMenuNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "updateWebMenuNotExists")
        val repository = WebMenusDatabaseRepository(database)
        val payload = UpdateWebMenuPayload("newTitle", "newUrl", 43)
        assertEquals(false, repository.update(UUID(), payload, UUID()))
    }

    @Test
    fun deleteWebMenu() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteWebMenu")
        val repository = WebMenusDatabaseRepository(database)
        val menu = repository.create(
            CreateWebMenuPayload(
                "title", "url", 42
            ), UUID()
        ) ?: fail("Unable to create menu")
        assertEquals(true, repository.delete(menu.id, menu.associationId))
        val count = database.suspendedTransaction {
            WebMenus
                .selectAll()
                .count()
        }
        assertEquals(0, count)
    }

    @Test
    fun deleteWebMenuNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteWebMenuNotInAssociation")
        val repository = WebMenusDatabaseRepository(database)
        val menu = repository.create(
            CreateWebMenuPayload(
                "title", "url", 42
            ), UUID()
        ) ?: fail("Unable to create menu")
        assertEquals(false, repository.delete(menu.id, UUID()))
        val count = database.suspendedTransaction {
            WebMenus
                .selectAll()
                .count()
        }
        assertEquals(1, count)
    }

    @Test
    fun deleteWebMenuNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteWebMenuNotExists")
        val repository = WebMenusDatabaseRepository(database)
        assertEquals(false, repository.delete(UUID(), UUID()))
    }

}
