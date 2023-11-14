package me.nathanfallet.suitebde.database.web

import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.models.web.CreateWebPagePayload
import org.jetbrains.exposed.sql.selectAll
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class DatabaseWebPagesRepositoryTest {

    @Test
    fun createWebPage() = runBlocking {
        val database = Database(protocol = "h2", name = "createWebPage")
        val repository = DatabaseWebPagesRepository(database)
        val page = repository.create(
            CreateWebPagePayload(
                "title", "content", false
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
        assertEquals(pageFromDatabase?.title, page?.title)
        assertEquals(pageFromDatabase?.content, page?.content)
        assertEquals(pageFromDatabase?.isHome, page?.isHome)
    }

    @Test
    fun getWebPage() = runBlocking {
        val database = Database(protocol = "h2", name = "getWebPage")
        val repository = DatabaseWebPagesRepository(database)
        val page = repository.create(
            CreateWebPagePayload(
                "title", "content", false
            ), "associationId"
        ) ?: fail("Unable to create page")
        val pageFromDatabase = repository.get(page.id, "associationId")
        assertEquals(pageFromDatabase?.id, page.id)
        assertEquals(pageFromDatabase?.associationId, page.associationId)
        assertEquals(pageFromDatabase?.title, page.title)
        assertEquals(pageFromDatabase?.content, page.content)
        assertEquals(pageFromDatabase?.isHome, page.isHome)
    }

}
