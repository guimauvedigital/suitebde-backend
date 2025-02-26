package com.suitebde.database.web

import com.suitebde.database.Database
import com.suitebde.models.web.WebPagePayload
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.selectAll
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class WebPagesDatabaseRepositoryTest {

    @Test
    fun getWebPagesInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getWebPagesInAssociation")
        val repository = WebPagesDatabaseRepository(database)
        val associationId = UUID()
        val page = repository.create(
            WebPagePayload(
                "url", "title", "content", false
            ), associationId
        ) ?: fail("Unable to create page")
        val pageFromDatabase = repository.list(associationId)
        assertEquals(1, pageFromDatabase.size)
        assertEquals(pageFromDatabase.first().id, page.id)
        assertEquals(pageFromDatabase.first().associationId, page.associationId)
        assertEquals(pageFromDatabase.first().url, page.url)
        assertEquals(pageFromDatabase.first().title, page.title)
        assertEquals(pageFromDatabase.first().content, page.content)
        assertEquals(pageFromDatabase.first().home, page.home)
    }

    @Test
    fun getWebPagesInAssociationLimitOffset() = runBlocking {
        val database = Database(protocol = "h2", name = "getWebPagesInAssociationLimitOffset")
        val repository = WebPagesDatabaseRepository(database)
        val associationId = UUID()
        for (i in 1..4) repository.create(
            WebPagePayload(
                "url", "title $i", "content", false
            ), associationId
        )
        val pageFromDatabase = repository.list(Pagination(1, 2), associationId)
        assertEquals(1, pageFromDatabase.size)
    }

    @Test
    fun getWebPagesInAssociationNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "getWebPagesInAssociationNotInAssociation")
        val repository = WebPagesDatabaseRepository(database)
        repository.create(
            WebPagePayload(
                "url", "title", "content", false
            ), UUID()
        ) ?: fail("Unable to create page")
        val pageFromDatabase = repository.list(UUID())
        assertEquals(0, pageFromDatabase.size)
    }

    @Test
    fun createWebPage() = runBlocking {
        val database = Database(protocol = "h2", name = "createWebPage")
        val repository = WebPagesDatabaseRepository(database)
        val associationId = UUID()
        val page = repository.create(
            WebPagePayload(
                "url", "title", "content", false
            ), associationId
        )
        val pageFromDatabase = database.suspendedTransaction {
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
        assertEquals(pageFromDatabase?.associationId, associationId)
        assertEquals(pageFromDatabase?.url, "url")
        assertEquals(pageFromDatabase?.title, "title")
        assertEquals(pageFromDatabase?.content, "content")
        assertEquals(pageFromDatabase?.home, false)
    }

    @Test
    fun getWebPage() = runBlocking {
        val database = Database(protocol = "h2", name = "getWebPage")
        val repository = WebPagesDatabaseRepository(database)
        val associationId = UUID()
        val page = repository.create(
            WebPagePayload(
                "url", "title", "content", false
            ), associationId
        ) ?: fail("Unable to create page")
        val pageFromDatabase = repository.get(page.id, associationId)
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
        val repository = WebPagesDatabaseRepository(database)
        val page = repository.create(
            WebPagePayload(
                "url", "title", "content", false
            ), UUID()
        ) ?: fail("Unable to create page")
        assertEquals(null, repository.get(page.id, UUID()))
    }

    @Test
    fun getWebPageNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "getWebPageNotExists")
        val repository = WebPagesDatabaseRepository(database)
        assertEquals(null, repository.get(UUID(), UUID()))
    }

    @Test
    fun getWebPageByUrl() = runBlocking {
        val database = Database(protocol = "h2", name = "getWebPageByUrl")
        val repository = WebPagesDatabaseRepository(database)
        val associationId = UUID()
        val page = repository.create(
            WebPagePayload(
                "url", "title", "content", false
            ), associationId
        ) ?: fail("Unable to create page")
        val pageFromDatabase = repository.getByUrl(page.url, associationId)
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
        val repository = WebPagesDatabaseRepository(database)
        val page = repository.create(
            WebPagePayload(
                "url", "title", "content", false
            ), UUID()
        ) ?: fail("Unable to create page")
        assertEquals(null, repository.getByUrl(page.url, UUID()))
    }

    @Test
    fun getWebPageByUrlNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "getWebPageByUrlNotExists")
        val repository = WebPagesDatabaseRepository(database)
        assertEquals(null, repository.getByUrl("url", UUID()))
    }

    @Test
    fun getWebPageHome() = runBlocking {
        val database = Database(protocol = "h2", name = "getWebPageHome")
        val repository = WebPagesDatabaseRepository(database)
        val associationId = UUID()
        val page = repository.create(
            WebPagePayload(
                "url", "title", "content", true
            ), associationId
        ) ?: fail("Unable to create page")
        val pageFromDatabase = repository.getHome(associationId)
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
        val repository = WebPagesDatabaseRepository(database)
        repository.create(
            WebPagePayload(
                "url", "title", "content", true
            ), UUID()
        ) ?: fail("Unable to create page")
        assertEquals(null, repository.getHome(UUID()))
    }

    @Test
    fun getWebPageHomeNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "getWebPageHomeNotExists")
        val repository = WebPagesDatabaseRepository(database)
        assertEquals(null, repository.getHome(UUID()))
    }

    @Test
    fun updateWebPage() = runBlocking {
        val database = Database(protocol = "h2", name = "updateWebPage")
        val repository = WebPagesDatabaseRepository(database)
        val associationId = UUID()
        val page = repository.create(
            WebPagePayload(
                "url", "title", "content", false
            ), associationId
        ) ?: fail("Unable to create page")
        val updatedPage = page.copy(
            title = "title2",
            content = "content2",
            home = true
        )
        val payload = WebPagePayload("url", "title2", "content2", true)
        assertEquals(true, repository.update(page.id, payload, associationId))
        val pageFromDatabase = database.suspendedTransaction {
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
        val repository = WebPagesDatabaseRepository(database)
        val page = repository.create(
            WebPagePayload(
                "url", "title", "content", false
            ), UUID()
        ) ?: fail("Unable to create page")
        val payload = WebPagePayload("url", "title2", "content2", true)
        assertEquals(false, repository.update(page.id, payload, UUID()))
    }

    @Test
    fun updateWebPageNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "updateWebPageNotExists")
        val repository = WebPagesDatabaseRepository(database)
        val payload = WebPagePayload("url", "title2", "content2", true)
        assertEquals(false, repository.update(UUID(), payload, UUID()))
    }

    @Test
    fun deleteWebPage() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteWebPage")
        val repository = WebPagesDatabaseRepository(database)
        val associationId = UUID()
        val page = repository.create(
            WebPagePayload(
                "url", "title", "content", false
            ), associationId
        ) ?: fail("Unable to create page")
        assertEquals(true, repository.delete(page.id, associationId))
        val count = database.suspendedTransaction {
            WebPages
                .selectAll()
                .count()
        }
        assertEquals(0, count)
    }

    @Test
    fun deleteWebPageNotInAssociation() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteWebPageNotInAssociation")
        val repository = WebPagesDatabaseRepository(database)
        val page = repository.create(
            WebPagePayload(
                "url", "title", "content", false
            ), UUID()
        ) ?: fail("Unable to create page")
        assertEquals(false, repository.delete(page.id, UUID()))
        val count = database.suspendedTransaction {
            WebPages
                .selectAll()
                .count()
        }
        assertEquals(1, count)
    }

    @Test
    fun deleteWebPageNotExists() = runBlocking {
        val database = Database(protocol = "h2", name = "deleteWebPageNotExists")
        val repository = WebPagesDatabaseRepository(database)
        assertEquals(false, repository.delete(UUID(), UUID()))
    }

}
