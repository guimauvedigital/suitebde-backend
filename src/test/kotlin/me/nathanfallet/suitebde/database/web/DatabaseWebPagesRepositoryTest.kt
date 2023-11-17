package me.nathanfallet.suitebde.database.web

import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.models.web.CreateWebPagePayload
import me.nathanfallet.suitebde.models.web.UpdateWebPagePayload
import org.jetbrains.exposed.sql.selectAll
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class DatabaseWebPagesRepositoryTest {

    @Test
    fun getWebPagesInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getWebPagesInAssociation")
        val repository = DatabaseWebPagesRepository(database)
        val page = repository.create(
            CreateWebPagePayload(
                "url", "title", "content", false
            ), "associationId"
        ) ?: fail("Unable to create page")
        val pageFromDatabase = repository.getWebPages("associationId")
        assertEquals(1, pageFromDatabase.size)
        assertEquals(pageFromDatabase.first().id, page.id)
        assertEquals(pageFromDatabase.first().associationId, page.associationId)
        assertEquals(pageFromDatabase.first().url, page.url)
        assertEquals(pageFromDatabase.first().title, page.title)
        assertEquals(pageFromDatabase.first().content, page.content)
        assertEquals(pageFromDatabase.first().home, page.home)
    }

    @Test
    fun createWebPage() = runBlocking {
        val database = Database(protocol = "h2", name = "createWebPage")
        val repository = DatabaseWebPagesRepository(database)
        val page = repository.create(
            CreateWebPagePayload(
                "url", "title", "content", false
            ), "associationId"
        )
        val pageFromDatabase = database.dbQuery {
            WebPages
                .selectAll()
                .map(WebPages::toWebPage)
                .singleOrNull()
        }
        assertEquals(pageFromDatabase?.id, page?.id)
        assertEquals(pageFromDatabase?.associationId, page?.associationId)
        assertEquals(pageFromDatabase?.url, page?.url)
        assertEquals(pageFromDatabase?.title, page?.title)
        assertEquals(pageFromDatabase?.content, page?.content)
        assertEquals(pageFromDatabase?.home, page?.home)
        assertEquals(pageFromDatabase?.associationId, "associationId")
        assertEquals(pageFromDatabase?.url, "url")
        assertEquals(pageFromDatabase?.title, "title")
        assertEquals(pageFromDatabase?.content, "content")
        assertEquals(pageFromDatabase?.home, false)
    }

    @Test
    fun getWebPage() = runBlocking {
        val database = Database(protocol = "h2", name = "getWebPage")
        val repository = DatabaseWebPagesRepository(database)
        val page = repository.create(
            CreateWebPagePayload(
                "url", "title", "content", false
            ), "associationId"
        ) ?: fail("Unable to create page")
        val pageFromDatabase = repository.get(page.id, "associationId")
        assertEquals(pageFromDatabase?.id, page.id)
        assertEquals(pageFromDatabase?.associationId, page.associationId)
        assertEquals(pageFromDatabase?.url, page.url)
        assertEquals(pageFromDatabase?.title, page.title)
        assertEquals(pageFromDatabase?.content, page.content)
        assertEquals(pageFromDatabase?.home, page.home)
    }

    @Test
    fun getWebPageNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getWebPageNotInAssociation")
        val repository = DatabaseWebPagesRepository(database)
        val page = repository.create(
            CreateWebPagePayload(
                "url", "title", "content", false
            ), "associationId"
        ) ?: fail("Unable to create page")
        assertEquals(null, repository.get(page.id, "otherAssociationId"))
    }

    @Test
    fun getWebPageNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "getWebPageNotExists")
        val repository = DatabaseWebPagesRepository(database)
        assertEquals(null, repository.get("pageId", "associationId"))
    }

    @Test
    fun getWebPageByUrl() = runBlocking {
        val database = Database(protocol = "h2", name = "getWebPageByUrl")
        val repository = DatabaseWebPagesRepository(database)
        val page = repository.create(
            CreateWebPagePayload(
                "url", "title", "content", false
            ), "associationId"
        ) ?: fail("Unable to create page")
        val pageFromDatabase = repository.getByUrl(page.url, "associationId")
        assertEquals(pageFromDatabase?.id, page.id)
        assertEquals(pageFromDatabase?.associationId, page.associationId)
        assertEquals(pageFromDatabase?.url, page.url)
        assertEquals(pageFromDatabase?.title, page.title)
        assertEquals(pageFromDatabase?.content, page.content)
        assertEquals(pageFromDatabase?.home, page.home)
    }

    @Test
    fun getWebPageByUrlNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getWebPageByUrlNotInAssociation")
        val repository = DatabaseWebPagesRepository(database)
        val page = repository.create(
            CreateWebPagePayload(
                "url", "title", "content", false
            ), "associationId"
        ) ?: fail("Unable to create page")
        assertEquals(null, repository.getByUrl(page.url, "otherAssociationId"))
    }

    @Test
    fun getWebPageByUrlNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "getWebPageByUrlNotExists")
        val repository = DatabaseWebPagesRepository(database)
        assertEquals(null, repository.getByUrl("url", "associationId"))
    }

    @Test
    fun getWebPageHome() = runBlocking {
        val database = Database(protocol = "h2", name = "getWebPageHome")
        val repository = DatabaseWebPagesRepository(database)
        val page = repository.create(
            CreateWebPagePayload(
                "url", "title", "content", true
            ), "associationId"
        ) ?: fail("Unable to create page")
        val pageFromDatabase = repository.getHome("associationId")
        assertEquals(pageFromDatabase?.id, page.id)
        assertEquals(pageFromDatabase?.associationId, page.associationId)
        assertEquals(pageFromDatabase?.url, page.url)
        assertEquals(pageFromDatabase?.title, page.title)
        assertEquals(pageFromDatabase?.content, page.content)
        assertEquals(pageFromDatabase?.home, page.home)
    }

    @Test
    fun getWebPageHomeNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getWebPageHomeNotInAssociation")
        val repository = DatabaseWebPagesRepository(database)
        repository.create(
            CreateWebPagePayload(
                "url", "title", "content", true
            ), "associationId"
        ) ?: fail("Unable to create page")
        assertEquals(null, repository.getHome("otherAssociationId"))
    }

    @Test
    fun getWebPageHomeNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "getWebPageHomeNotExists")
        val repository = DatabaseWebPagesRepository(database)
        assertEquals(null, repository.getHome("associationId"))
    }

    @Test
    fun updateWebPage() = runBlocking {
        val database = Database(protocol = "h2", name = "updateWebPage")
        val repository = DatabaseWebPagesRepository(database)
        val page = repository.create(
            CreateWebPagePayload(
                "url", "title", "content", false
            ), "associationId"
        ) ?: fail("Unable to create page")
        val updatedPage = page.copy(
            title = "title2",
            content = "content2",
            home = true
        )
        val payload = UpdateWebPagePayload("url", "title2", "content2", true)
        assertEquals(true, repository.update(page.id, payload, "associationId"))
        val pageFromDatabase = database.dbQuery {
            WebPages
                .selectAll()
                .map(WebPages::toWebPage)
                .singleOrNull()
        }
        assertEquals(pageFromDatabase?.id, updatedPage.id)
        assertEquals(pageFromDatabase?.associationId, updatedPage.associationId)
        assertEquals(pageFromDatabase?.url, updatedPage.url)
        assertEquals(pageFromDatabase?.title, updatedPage.title)
        assertEquals(pageFromDatabase?.content, updatedPage.content)
        assertEquals(pageFromDatabase?.home, updatedPage.home)
    }

    @Test
    fun updateWebPageNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "updateWebPageNotInAssociation")
        val repository = DatabaseWebPagesRepository(database)
        val page = repository.create(
            CreateWebPagePayload(
                "url", "title", "content", false
            ), "associationId"
        ) ?: fail("Unable to create page")
        val payload = UpdateWebPagePayload("url", "title2", "content2", true)
        assertEquals(false, repository.update(page.id, payload, "otherAssociationId"))
    }

    @Test
    fun updateWebPageNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "updateWebPageNotExists")
        val repository = DatabaseWebPagesRepository(database)
        val payload = UpdateWebPagePayload("url", "title2", "content2", true)
        assertEquals(false, repository.update("pageId", payload, "associationId"))
    }

    @Test
    fun deleteWebPage() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteWebPage")
        val repository = DatabaseWebPagesRepository(database)
        val page = repository.create(
            CreateWebPagePayload(
                "url", "title", "content", false
            ), "associationId"
        ) ?: fail("Unable to create page")
        assertEquals(true, repository.delete(page.id, "associationId"))
        val count = database.dbQuery {
            WebPages
                .selectAll()
                .count()
        }
        assertEquals(0, count)
    }

    @Test
    fun deleteWebPageNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteWebPageNotInAssociation")
        val repository = DatabaseWebPagesRepository(database)
        val page = repository.create(
            CreateWebPagePayload(
                "url", "title", "content", false
            ), "associationId"
        ) ?: fail("Unable to create page")
        assertEquals(false, repository.delete(page.id, "otherAssociationId"))
        val count = database.dbQuery {
            WebPages
                .selectAll()
                .count()
        }
        assertEquals(1, count)
    }

    @Test
    fun deleteWebPageNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteWebPageNotExists")
        val repository = DatabaseWebPagesRepository(database)
        assertEquals(false, repository.delete("pageId", "associationId"))
    }

}
