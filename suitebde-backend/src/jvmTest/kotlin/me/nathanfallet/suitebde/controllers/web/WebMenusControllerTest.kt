package me.nathanfallet.suitebde.controllers.web

import io.ktor.http.*
import io.ktor.server.application.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.models.web.CreateWebMenuPayload
import me.nathanfallet.suitebde.models.web.UpdateWebMenuPayload
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import me.nathanfallet.usecases.models.delete.IDeleteChildModelSuspendUseCase
import me.nathanfallet.usecases.models.get.IGetChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.slice.IListSliceChildModelSuspendUseCase
import me.nathanfallet.usecases.models.update.IUpdateChildModelSuspendUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class WebMenusControllerTest {

    private val association = Association(
        "associationId", "name", "school", "city",
        true, Clock.System.now(), Clock.System.now()
    )
    private val user = User(
        "id", "associationId", "email", null,
        "firstname", "lastname", false
    )
    private val menu = WebMenu(
        "id", "associationId", "title", "url",
        0, null
    )

    @Test
    fun testList() = runBlocking {
        val getWebMenusUseCase = mockk<IListSliceChildModelSuspendUseCase<WebMenu, String>>()
        val controller = WebMenusController(
            mockk(), mockk(), getWebMenusUseCase, mockk(),
            mockk(), mockk(), mockk()
        )
        val call = mockk<ApplicationCall>()
        coEvery { getWebMenusUseCase(10, 5, menu.associationId) } returns listOf(menu)
        every { call.parameters["limit"] } returns "10"
        every { call.parameters["offset"] } returns "5"
        assertEquals(listOf(menu), controller.list(call, association))
    }

    @Test
    fun testListDefaultLimitOffset() = runBlocking {
        val getWebMenusUseCase = mockk<IListSliceChildModelSuspendUseCase<WebMenu, String>>()
        val controller = WebMenusController(
            mockk(), mockk(), getWebMenusUseCase, mockk(),
            mockk(), mockk(), mockk()
        )
        val call = mockk<ApplicationCall>()
        coEvery { getWebMenusUseCase(25, 0, menu.associationId) } returns listOf(menu)
        every { call.parameters["limit"] } returns null
        every { call.parameters["offset"] } returns null
        assertEquals(listOf(menu), controller.list(call, association))
    }

    @Test
    fun testListInvalidLimitOffset() = runBlocking {
        val getWebMenusUseCase = mockk<IListSliceChildModelSuspendUseCase<WebMenu, String>>()
        val controller = WebMenusController(
            mockk(), mockk(), getWebMenusUseCase, mockk(),
            mockk(), mockk(), mockk()
        )
        val call = mockk<ApplicationCall>()
        coEvery { getWebMenusUseCase(25, 0, menu.associationId) } returns listOf(menu)
        every { call.parameters["limit"] } returns "a"
        every { call.parameters["offset"] } returns "b"
        assertEquals(listOf(menu), controller.list(call, association))
    }

    @Test
    fun testGet() = runBlocking {
        val getWebMenuUseCase = mockk<IGetChildModelSuspendUseCase<WebMenu, String, String>>()
        val controller = WebMenusController(
            mockk(), mockk(), mockk(), getWebMenuUseCase,
            mockk(), mockk(), mockk()
        )
        coEvery { getWebMenuUseCase(menu.id, menu.associationId) } returns menu
        assertEquals(menu, controller.get(mockk(), association, menu.id))
    }

    @Test
    fun testGetNotFound() = runBlocking {
        val getWebMenuUseCase = mockk<IGetChildModelSuspendUseCase<WebMenu, String, String>>()
        val controller = WebMenusController(
            mockk(), mockk(), mockk(), getWebMenuUseCase,
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
        val createWebMenuUseCase = mockk<ICreateChildModelSuspendUseCase<WebMenu, CreateWebMenuPayload, String>>()
        val controller = WebMenusController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(),
            mockk(), createWebMenuUseCase, mockk(), mockk()
        )
        val payload = CreateWebMenuPayload("url", "title", 0)
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBMENUS_CREATE inAssociation association) } returns true
        coEvery { createWebMenuUseCase(payload, menu.associationId) } returns menu
        assertEquals(menu, controller.create(mockk(), association, payload))
    }

    @Test
    fun testCreateForbidden() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val controller = WebMenusController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(),
            mockk(), mockk(), mockk(), mockk()
        )
        val payload = CreateWebMenuPayload("url", "title", 0)
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBMENUS_CREATE inAssociation association) } returns false
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
        val createWebMenuUseCase = mockk<ICreateChildModelSuspendUseCase<WebMenu, CreateWebMenuPayload, String>>()
        val controller = WebMenusController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(),
            mockk(), createWebMenuUseCase, mockk(), mockk()
        )
        val payload = CreateWebMenuPayload("url", "title", 0)
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBMENUS_CREATE inAssociation association) } returns true
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
        val getWebMenuUseCase = mockk<IGetChildModelSuspendUseCase<WebMenu, String, String>>()
        val updateWebMenuUseCase =
            mockk<IUpdateChildModelSuspendUseCase<WebMenu, String, UpdateWebMenuPayload, String>>()
        val controller = WebMenusController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(),
            getWebMenuUseCase, mockk(), updateWebMenuUseCase, mockk()
        )
        val payload = UpdateWebMenuPayload("url", "title", 0)
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBMENUS_UPDATE inAssociation association) } returns true
        coEvery { getWebMenuUseCase(menu.id, menu.associationId) } returns menu
        coEvery { updateWebMenuUseCase(menu.id, payload, menu.associationId) } returns menu
        assertEquals(menu, controller.update(mockk(), association, menu.id, payload))
    }

    @Test
    fun testUpdateInternalError() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getWebMenuUseCase = mockk<IGetChildModelSuspendUseCase<WebMenu, String, String>>()
        val updateWebMenuUseCase =
            mockk<IUpdateChildModelSuspendUseCase<WebMenu, String, UpdateWebMenuPayload, String>>()
        val controller = WebMenusController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(),
            getWebMenuUseCase, mockk(), updateWebMenuUseCase, mockk()
        )
        val payload = UpdateWebMenuPayload("url", "title", 0)
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBMENUS_UPDATE inAssociation association) } returns true
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
        val getWebMenuUseCase = mockk<IGetChildModelSuspendUseCase<WebMenu, String, String>>()
        val controller = WebMenusController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(),
            getWebMenuUseCase, mockk(), mockk(), mockk()
        )
        val payload = UpdateWebMenuPayload("url", "title", 0)
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBMENUS_UPDATE inAssociation association) } returns true
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
            mockk(), mockk(), mockk(), mockk()
        )
        val payload = UpdateWebMenuPayload("url", "title", 0)
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBMENUS_UPDATE inAssociation association) } returns false
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
        val getWebMenuUseCase = mockk<IGetChildModelSuspendUseCase<WebMenu, String, String>>()
        val deleteWebMenuUseCase = mockk<IDeleteChildModelSuspendUseCase<WebMenu, String, String>>()
        val controller = WebMenusController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(),
            getWebMenuUseCase, mockk(), mockk(), deleteWebMenuUseCase
        )
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBMENUS_DELETE inAssociation association) } returns true
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
        val getWebMenuUseCase = mockk<IGetChildModelSuspendUseCase<WebMenu, String, String>>()
        val deleteWebMenuUseCase = mockk<IDeleteChildModelSuspendUseCase<WebMenu, String, String>>()
        val controller = WebMenusController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(),
            getWebMenuUseCase, mockk(), mockk(), deleteWebMenuUseCase
        )
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBMENUS_DELETE inAssociation association) } returns true
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
        val getWebMenuUseCase = mockk<IGetChildModelSuspendUseCase<WebMenu, String, String>>()
        val controller = WebMenusController(
            requireUserForCallUseCase, checkPermissionUseCase, mockk(),
            getWebMenuUseCase, mockk(), mockk(), mockk()
        )
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBMENUS_DELETE inAssociation association) } returns true
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
            mockk(), mockk(), mockk(), mockk()
        )
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.WEBMENUS_DELETE inAssociation association) } returns false
        val exception = assertFailsWith(ControllerException::class) {
            controller.delete(mockk(), association, menu.id)
        }
        assertEquals(HttpStatusCode.Forbidden, exception.code)
        assertEquals("webmenus_delete_not_allowed", exception.key)
    }

}
