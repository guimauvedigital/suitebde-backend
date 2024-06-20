package com.suitebde.controllers.web

import com.suitebde.models.associations.Association
import com.suitebde.models.roles.Permission
import com.suitebde.models.users.User
import com.suitebde.models.web.CreateWebMenuPayload
import com.suitebde.models.web.UpdateWebMenuPayload
import com.suitebde.models.web.WebMenu
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

class WebMenusControllerTest {

    private val association = Association(
        UUID(), "name", "school", "city",
        true, Clock.System.now(), Clock.System.now()
    )
    private val user = User(
        UUID(), association.id, "email", null,
        "firstname", "lastname", false, Clock.System.now()
    )
    private val menu = WebMenu(
        UUID(), association.id, "title", "url",
        0, null
    )

    @Test
    fun testList() = runBlocking {
        val getWebMenusUseCase = mockk<IListSliceChildModelSuspendUseCase<WebMenu, UUID>>()
        val controller = WebMenusController(
            mockk(), mockk(), mockk(), getWebMenusUseCase, mockk(),
            mockk(), mockk(), mockk()
        )
        val call = mockk<ApplicationCall>()
        coEvery { getWebMenusUseCase(Pagination(10, 5), menu.associationId) } returns listOf(menu)
        every { call.request.path() } returns "/api/v1/associations/id/webmenus"
        assertEquals(listOf(menu), controller.list(call, association, 10, 5))
    }

    @Test
    fun testListDefaultLimitOffset() = runBlocking {
        val getWebMenusUseCase = mockk<IListSliceChildModelSuspendUseCase<WebMenu, UUID>>()
        val controller = WebMenusController(
            mockk(), mockk(), mockk(), getWebMenusUseCase, mockk(),
            mockk(), mockk(), mockk()
        )
        val call = mockk<ApplicationCall>()
        coEvery { getWebMenusUseCase(Pagination(25, 0), menu.associationId) } returns listOf(menu)
        every { call.request.path() } returns "/api/v1/associations/id/webmenus"
        assertEquals(listOf(menu), controller.list(call, association, null, null))
    }

    @Test
    fun testListAdmin() = runBlocking {
        val getWebMenusUseCase = mockk<IListChildModelSuspendUseCase<WebMenu, UUID>>()
        val controller = WebMenusController(
            mockk(), mockk(), getWebMenusUseCase, mockk(), mockk(),
            mockk(), mockk(), mockk()
        )
        val call = mockk<ApplicationCall>()
        coEvery { getWebMenusUseCase(menu.associationId) } returns listOf(menu)
        every { call.request.path() } returns "/admin/webmenus"
        assertEquals(listOf(menu), controller.list(call, association, null, null))
    }

    @Test
    fun testGet() = runBlocking {
        val getWebMenuUseCase = mockk<IGetChildModelSuspendUseCase<WebMenu, UUID, UUID>>()
        val controller = WebMenusController(
            mockk(), mockk(), mockk(), mockk(), getWebMenuUseCase,
            mockk(), mockk(), mockk()
        )
        coEvery { getWebMenuUseCase(menu.id, menu.associationId) } returns menu
        assertEquals(menu, controller.get(mockk(), association, menu.id))
    }

    @Test
    fun testGetNotFound() = runBlocking {
        val getWebMenuUseCase = mockk<IGetChildModelSuspendUseCase<WebMenu, UUID, UUID>>()
        val controller = WebMenusController(
            mockk(), mockk(), mockk(), mockk(), getWebMenuUseCase,
            mockk(), mockk(), mockk()
        )
        coEvery { getWebMenuUseCase(menu.id, menu.associationId) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.get(mockk(), association, menu.id)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("webmenus_not_found", exception.key)
    }

    @Test
    fun testCreate() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val createWebMenuUseCase = mockk<ICreateChildModelSuspendUseCase<WebMenu, CreateWebMenuPayload, UUID>>()
        val controller = WebMenusController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(),
            mockk(), mockk(), createWebMenuUseCase, mockk(), mockk()
        )
        val payload = CreateWebMenuPayload("url", "title", 0)
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBMENUS_CREATE inAssociation association.id) } returns true
        coEvery { createWebMenuUseCase(payload, menu.associationId) } returns menu
        assertEquals(menu, controller.create(mockk(), association, payload))
    }

    @Test
    fun testCreateForbidden() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val controller = WebMenusController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk()
        )
        val payload = CreateWebMenuPayload("url", "title", 0)
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBMENUS_CREATE inAssociation association.id) } returns false
        val exception = assertFailsWith(ControllerException::class) {
            controller.create(mockk(), association, payload)
        }
        assertEquals(HttpStatusCode.Forbidden, exception.code)
        assertEquals("webmenus_create_not_allowed", exception.key)
    }

    @Test
    fun testCreateInternalError() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val createWebMenuUseCase = mockk<ICreateChildModelSuspendUseCase<WebMenu, CreateWebMenuPayload, UUID>>()
        val controller = WebMenusController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(),
            mockk(), mockk(), createWebMenuUseCase, mockk(), mockk()
        )
        val payload = CreateWebMenuPayload("url", "title", 0)
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBMENUS_CREATE inAssociation association.id) } returns true
        coEvery { createWebMenuUseCase(payload, menu.associationId) } returns null
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
        val getWebMenuUseCase = mockk<IGetChildModelSuspendUseCase<WebMenu, UUID, UUID>>()
        val updateWebMenuUseCase = mockk<IUpdateChildModelSuspendUseCase<WebMenu, UUID, UpdateWebMenuPayload, UUID>>()
        val controller = WebMenusController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(),
            mockk(), getWebMenuUseCase, mockk(), updateWebMenuUseCase, mockk()
        )
        val payload = UpdateWebMenuPayload("url", "title", 0)
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBMENUS_UPDATE inAssociation association.id) } returns true
        coEvery { getWebMenuUseCase(menu.id, menu.associationId) } returns menu
        coEvery { updateWebMenuUseCase(menu.id, payload, menu.associationId) } returns menu
        assertEquals(menu, controller.update(mockk(), association, menu.id, payload))
    }

    @Test
    fun testUpdateInternalError() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getWebMenuUseCase = mockk<IGetChildModelSuspendUseCase<WebMenu, UUID, UUID>>()
        val updateWebMenuUseCase = mockk<IUpdateChildModelSuspendUseCase<WebMenu, UUID, UpdateWebMenuPayload, UUID>>()
        val controller = WebMenusController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(),
            mockk(), getWebMenuUseCase, mockk(), updateWebMenuUseCase, mockk()
        )
        val payload = UpdateWebMenuPayload("url", "title", 0)
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBMENUS_UPDATE inAssociation association.id) } returns true
        coEvery { getWebMenuUseCase(menu.id, menu.associationId) } returns menu
        coEvery { updateWebMenuUseCase(menu.id, payload, menu.associationId) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.update(mockk(), association, menu.id, payload)
        }
        assertEquals(HttpStatusCode.InternalServerError, exception.code)
        assertEquals("error_internal", exception.key)
    }

    @Test
    fun testUpdateNotFound() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getWebMenuUseCase = mockk<IGetChildModelSuspendUseCase<WebMenu, UUID, UUID>>()
        val controller = WebMenusController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(),
            mockk(), getWebMenuUseCase, mockk(), mockk(), mockk()
        )
        val payload = UpdateWebMenuPayload("url", "title", 0)
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBMENUS_UPDATE inAssociation association.id) } returns true
        coEvery { getWebMenuUseCase(menu.id, menu.associationId) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.update(mockk(), association, menu.id, payload)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("webmenus_not_found", exception.key)
    }

    @Test
    fun testUpdateForbidden() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val controller = WebMenusController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk()
        )
        val payload = UpdateWebMenuPayload("url", "title", 0)
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBMENUS_UPDATE inAssociation association.id) } returns false
        val exception = assertFailsWith(ControllerException::class) {
            controller.update(mockk(), association, menu.id, payload)
        }
        assertEquals(HttpStatusCode.Forbidden, exception.code)
        assertEquals("webmenus_update_not_allowed", exception.key)
    }

    @Test
    fun testDelete() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getWebMenuUseCase = mockk<IGetChildModelSuspendUseCase<WebMenu, UUID, UUID>>()
        val deleteWebMenuUseCase = mockk<IDeleteChildModelSuspendUseCase<WebMenu, UUID, UUID>>()
        val controller = WebMenusController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(),
            mockk(), getWebMenuUseCase, mockk(), mockk(), deleteWebMenuUseCase
        )
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBMENUS_DELETE inAssociation association.id) } returns true
        coEvery { getWebMenuUseCase(menu.id, menu.associationId) } returns menu
        coEvery { deleteWebMenuUseCase(menu.id, menu.associationId) } returns true
        controller.delete(mockk(), association, menu.id)
        coVerify {
            deleteWebMenuUseCase(menu.id, menu.associationId)
        }
    }

    @Test
    fun testDeleteInternalError() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getWebMenuUseCase = mockk<IGetChildModelSuspendUseCase<WebMenu, UUID, UUID>>()
        val deleteWebMenuUseCase = mockk<IDeleteChildModelSuspendUseCase<WebMenu, UUID, UUID>>()
        val controller = WebMenusController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(),
            mockk(), getWebMenuUseCase, mockk(), mockk(), deleteWebMenuUseCase
        )
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBMENUS_DELETE inAssociation association.id) } returns true
        coEvery { getWebMenuUseCase(menu.id, menu.associationId) } returns menu
        coEvery { deleteWebMenuUseCase(menu.id, menu.associationId) } returns false
        val exception = assertFailsWith(ControllerException::class) {
            controller.delete(mockk(), association, menu.id)
        }
        assertEquals(HttpStatusCode.InternalServerError, exception.code)
        assertEquals("error_internal", exception.key)
    }

    @Test
    fun testDeleteNotFound() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getWebMenuUseCase = mockk<IGetChildModelSuspendUseCase<WebMenu, UUID, UUID>>()
        val controller = WebMenusController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(),
            mockk(), getWebMenuUseCase, mockk(), mockk(), mockk()
        )
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBMENUS_DELETE inAssociation association.id) } returns true
        coEvery { getWebMenuUseCase(menu.id, menu.associationId) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.delete(mockk(), association, menu.id)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("webmenus_not_found", exception.key)
    }

    @Test
    fun testDeleteForbidden() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val controller = WebMenusController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk()
        )
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBMENUS_DELETE inAssociation association.id) } returns false
        val exception = assertFailsWith(ControllerException::class) {
            controller.delete(mockk(), association, menu.id)
        }
        assertEquals(HttpStatusCode.Forbidden, exception.code)
        assertEquals("webmenus_delete_not_allowed", exception.key)
    }

}
