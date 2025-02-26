package com.suitebde.controllers.web

import com.suitebde.models.associations.Association
import com.suitebde.models.roles.Permission
import com.suitebde.models.users.User
import com.suitebde.models.web.WebPage
import com.suitebde.models.web.WebPagePayload
import com.suitebde.usecases.web.IGetWebPageByUrlUseCase
import com.suitebde.usecases.web.IRenderMarkdownUseCase
import dev.kaccelero.commons.exceptions.ControllerException
import dev.kaccelero.commons.permissions.ICheckPermissionSuspendUseCase
import dev.kaccelero.commons.repositories.*
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class WebPagesControllerTest {

    private val association = Association(
        UUID(), "name", "school", "city",
        true, Clock.System.now(), Clock.System.now()
    )
    private val user = User(
        UUID(), association.id, "email", null,
        "firstname", "lastname", false, Clock.System.now()
    )
    private val page = WebPage(
        UUID(), association.id, "url", "title", "content", false
    )

    @Test
    fun testList() = runBlocking {
        val getWebPagesUseCase = mockk<IListSliceChildModelSuspendUseCase<WebPage, UUID>>()
        val controller = WebPagesController(
            mockk(), mockk(), mockk(), getWebPagesUseCase, mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk()
        )
        val call = mockk<ApplicationCall>()
        coEvery { getWebPagesUseCase(Pagination(10, 5), page.associationId) } returns listOf(page)
        every { call.request.path() } returns "/api/v1/associations/id/webpages"
        assertEquals(listOf(page), controller.list(call, association, 10, 5))
    }

    @Test
    fun testListDefaultLimitOffset() = runBlocking {
        val getWebPagesUseCase = mockk<IListSliceChildModelSuspendUseCase<WebPage, UUID>>()
        val controller = WebPagesController(
            mockk(), mockk(), mockk(), getWebPagesUseCase, mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk()
        )
        val call = mockk<ApplicationCall>()
        coEvery { getWebPagesUseCase(Pagination(25, 0), page.associationId) } returns listOf(page)
        every { call.request.path() } returns "/api/v1/associations/id/webpages"
        assertEquals(listOf(page), controller.list(call, association, null, null))
    }

    @Test
    fun testListAdmin() = runBlocking {
        val getWebPagesUseCase = mockk<IListChildModelSuspendUseCase<WebPage, UUID>>()
        val controller = WebPagesController(
            mockk(), mockk(), getWebPagesUseCase, mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk()
        )
        val call = mockk<ApplicationCall>()
        coEvery { getWebPagesUseCase(page.associationId) } returns listOf(page)
        every { call.request.path() } returns "/admin/webpages"
        assertEquals(listOf(page), controller.list(call, association, null, null))
    }

    @Test
    fun testGet() = runBlocking {
        val getWebPageUseCase = mockk<IGetChildModelSuspendUseCase<WebPage, UUID, UUID>>()
        val controller = WebPagesController(
            mockk(), mockk(), mockk(), mockk(), mockk(),
            getWebPageUseCase, mockk(), mockk(), mockk(), mockk()
        )
        coEvery { getWebPageUseCase(page.id, page.associationId) } returns page
        assertEquals(page, controller.get(mockk(), association, page.id))
    }

    @Test
    fun testGetNotFound() = runBlocking {
        val getWebPageUseCase = mockk<IGetChildModelSuspendUseCase<WebPage, UUID, UUID>>()
        val controller = WebPagesController(
            mockk(), mockk(), mockk(), mockk(), mockk(),
            getWebPageUseCase, mockk(), mockk(), mockk(), mockk()
        )
        coEvery { getWebPageUseCase(page.id, page.associationId) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.get(mockk(), association, page.id)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("webpages_not_found", exception.key)
    }

    @Test
    fun testGetByUrl() = runBlocking {
        val getWebPageByUrlUseCase = mockk<IGetWebPageByUrlUseCase>()
        val renderMarkdownUseCase = mockk<IRenderMarkdownUseCase>()
        val controller = WebPagesController(
            mockk(), mockk(), mockk(), mockk(), getWebPageByUrlUseCase,
            mockk(), mockk(), mockk(), mockk(), renderMarkdownUseCase
        )
        coEvery { getWebPageByUrlUseCase(page.url, page.associationId) } returns page
        every { renderMarkdownUseCase(page.content) } returns "markdown"
        assertEquals(
            mapOf("item" to page, "content" to "markdown"),
            controller.getByUrl(mockk(), association, page.url)
        )
    }

    @Test
    fun testGetByUrlNotFound() = runBlocking {
        val getWebPageByUrlUseCase = mockk<IGetWebPageByUrlUseCase>()
        val controller = WebPagesController(
            mockk(), mockk(), mockk(), mockk(), getWebPageByUrlUseCase,
            mockk(), mockk(), mockk(), mockk(), mockk()
        )
        coEvery { getWebPageByUrlUseCase(page.url, page.associationId) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.getByUrl(mockk(), association, page.url)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("webpages_not_found", exception.key)
    }

    @Test
    fun testCreate() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val createWebPageUseCase =
            mockk<ICreateChildModelSuspendUseCase<WebPage, WebPagePayload, UUID>>()
        val controller = WebPagesController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(), mockk(), mockk(), mockk(),
            createWebPageUseCase, mockk(), mockk(), mockk()
        )
        val payload = WebPagePayload("url", "title", "content", true)
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBPAGES_CREATE inAssociation association.id) } returns true
        coEvery { createWebPageUseCase(payload, page.associationId) } returns page
        assertEquals(page, controller.create(mockk(), association, payload))
    }

    @Test
    fun testCreateForbidden() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val controller = WebPagesController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk()
        )
        val payload = WebPagePayload("url", "title", "content", true)
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBPAGES_CREATE inAssociation association.id) } returns false
        val exception = assertFailsWith(ControllerException::class) {
            controller.create(mockk(), association, payload)
        }
        assertEquals(HttpStatusCode.Forbidden, exception.code)
        assertEquals("webpages_create_not_allowed", exception.key)
    }

    @Test
    fun testCreateInternalError() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val createWebPageUseCase =
            mockk<ICreateChildModelSuspendUseCase<WebPage, WebPagePayload, UUID>>()
        val controller = WebPagesController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(), mockk(), mockk(), mockk(),
            createWebPageUseCase, mockk(), mockk(), mockk()
        )
        val payload = WebPagePayload("url", "title", "content", true)
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBPAGES_CREATE inAssociation association.id) } returns true
        coEvery { createWebPageUseCase(payload, page.associationId) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.create(mockk(), association, payload)
        }
        assertEquals(HttpStatusCode.InternalServerError, exception.code)
        assertEquals("error_internal", exception.key)
    }

    @Test
    fun testUpdate() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getWebPageUseCase = mockk<IGetChildModelSuspendUseCase<WebPage, UUID, UUID>>()
        val updateWebPageUseCase = mockk<IUpdateChildModelSuspendUseCase<WebPage, UUID, WebPagePayload, UUID>>()
        val controller = WebPagesController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(), mockk(), mockk(),
            getWebPageUseCase, mockk(), updateWebPageUseCase, mockk(), mockk()
        )
        val payload = WebPagePayload("url", "title2", "content2", true)
        val updatedPage = page.copy(
            title = payload.title,
            content = payload.content,
            home = payload.home
        )
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBPAGES_UPDATE inAssociation association.id) } returns true
        coEvery { getWebPageUseCase(page.id, page.associationId) } returns page
        coEvery { updateWebPageUseCase(page.id, payload, page.associationId) } returns updatedPage
        assertEquals(updatedPage, controller.update(mockk(), association, page.id, payload))
    }

    @Test
    fun testUpdateInternalError() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getWebPageUseCase = mockk<IGetChildModelSuspendUseCase<WebPage, UUID, UUID>>()
        val updateWebPageUseCase = mockk<IUpdateChildModelSuspendUseCase<WebPage, UUID, WebPagePayload, UUID>>()
        val controller = WebPagesController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(), mockk(), mockk(),
            getWebPageUseCase, mockk(), updateWebPageUseCase, mockk(), mockk()
        )
        val payload = WebPagePayload("url", "title2", "content2", true)
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBPAGES_UPDATE inAssociation association.id) } returns true
        coEvery { getWebPageUseCase(page.id, page.associationId) } returns page
        coEvery { updateWebPageUseCase(page.id, payload, page.associationId) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.update(mockk(), association, page.id, payload)
        }
        assertEquals(HttpStatusCode.InternalServerError, exception.code)
        assertEquals("error_internal", exception.key)
    }

    @Test
    fun testUpdateNotFound() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getWebPageUseCase = mockk<IGetChildModelSuspendUseCase<WebPage, UUID, UUID>>()
        val controller = WebPagesController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(), mockk(), mockk(),
            getWebPageUseCase, mockk(), mockk(), mockk(), mockk()
        )
        val payload = WebPagePayload("url", "title2", "content2", true)
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBPAGES_UPDATE inAssociation association.id) } returns true
        coEvery { getWebPageUseCase(page.id, page.associationId) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.update(mockk(), association, page.id, payload)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("webpages_not_found", exception.key)
    }

    @Test
    fun testUpdateForbidden() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val controller = WebPagesController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk()
        )
        val payload = WebPagePayload("url", "title2", "content2", true)
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBPAGES_UPDATE inAssociation association.id) } returns false
        val exception = assertFailsWith(ControllerException::class) {
            controller.update(mockk(), association, page.id, payload)
        }
        assertEquals(HttpStatusCode.Forbidden, exception.code)
        assertEquals("webpages_update_not_allowed", exception.key)
    }

    @Test
    fun testDelete() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getWebPageUseCase = mockk<IGetChildModelSuspendUseCase<WebPage, UUID, UUID>>()
        val deleteWebPageUseCase = mockk<IDeleteChildModelSuspendUseCase<WebPage, UUID, UUID>>()
        val controller = WebPagesController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(), mockk(), mockk(),
            getWebPageUseCase, mockk(), mockk(), deleteWebPageUseCase, mockk()
        )
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBPAGES_DELETE inAssociation association.id) } returns true
        coEvery { getWebPageUseCase(page.id, page.associationId) } returns page
        coEvery { deleteWebPageUseCase(page.id, page.associationId) } returns true
        controller.delete(mockk(), association, page.id)
        coVerify {
            deleteWebPageUseCase(page.id, page.associationId)
        }
    }

    @Test
    fun testDeleteInternalError() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getWebPageUseCase = mockk<IGetChildModelSuspendUseCase<WebPage, UUID, UUID>>()
        val deleteWebPageUseCase = mockk<IDeleteChildModelSuspendUseCase<WebPage, UUID, UUID>>()
        val controller = WebPagesController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(), mockk(), mockk(),
            getWebPageUseCase, mockk(), mockk(), deleteWebPageUseCase, mockk()
        )
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBPAGES_DELETE inAssociation association.id) } returns true
        coEvery { getWebPageUseCase(page.id, page.associationId) } returns page
        coEvery { deleteWebPageUseCase(page.id, page.associationId) } returns false
        val exception = assertFailsWith(ControllerException::class) {
            controller.delete(mockk(), association, page.id)
        }
        assertEquals(HttpStatusCode.InternalServerError, exception.code)
        assertEquals("error_internal", exception.key)
    }

    @Test
    fun testDeleteNotFound() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getWebPageUseCase = mockk<IGetChildModelSuspendUseCase<WebPage, UUID, UUID>>()
        val controller = WebPagesController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(), mockk(), mockk(),
            getWebPageUseCase, mockk(), mockk(), mockk(), mockk()
        )
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBPAGES_DELETE inAssociation association.id) } returns true
        coEvery { getWebPageUseCase(page.id, page.associationId) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.delete(mockk(), association, page.id)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("webpages_not_found", exception.key)
    }

    @Test
    fun testDeleteForbidden() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val controller = WebPagesController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk()
        )
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBPAGES_DELETE inAssociation association.id) } returns false
        val exception = assertFailsWith(ControllerException::class) {
            controller.delete(mockk(), association, page.id)
        }
        assertEquals(HttpStatusCode.Forbidden, exception.code)
        assertEquals("webpages_delete_not_allowed", exception.key)
    }

}
