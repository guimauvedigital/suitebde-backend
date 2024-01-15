package me.nathanfallet.suitebde.controllers.roles

import io.ktor.http.*
import io.ktor.server.application.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.roles.CreateRolePayload
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.roles.Role
import me.nathanfallet.suitebde.models.roles.UpdateRolePayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import me.nathanfallet.usecases.models.delete.IDeleteChildModelSuspendUseCase
import me.nathanfallet.usecases.models.get.IGetChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase
import me.nathanfallet.usecases.models.update.IUpdateChildModelSuspendUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RolesControllerTest {

    private val association = Association(
        "associationId", "name", "school", "city",
        true, Clock.System.now(), Clock.System.now()
    )
    private val user = User(
        "id", "associationId", "email", null,
        "firstname", "lastname", false
    )
    private val role = Role(
        "roleId", "associationId", "name"
    )

    @Test
    fun testGetAll() = runBlocking {
        val getRolesInAssociationUseCase = mockk<IListChildModelSuspendUseCase<Role, String>>()
        val call = mockk<ApplicationCall>()
        coEvery { getRolesInAssociationUseCase(association.id) } returns listOf(role)
        val controller = RolesController(
            mockk(), mockk(), getRolesInAssociationUseCase,
            mockk(), mockk(), mockk(), mockk()
        )
        assertEquals(listOf(role), controller.list(call, association))
    }

    @Test
    fun testGet() = runBlocking {
        val getRoleUseCase = mockk<IGetChildModelSuspendUseCase<Role, String, String>>()
        val controller = RolesController(
            mockk(), mockk(), mockk(), mockk(),
            getRoleUseCase, mockk(), mockk()
        )
        coEvery { getRoleUseCase(role.id, role.associationId) } returns role
        assertEquals(role, controller.get(mockk(), association, role.id))
    }

    @Test
    fun testGetNotFound() = runBlocking {
        val getRoleUseCase = mockk<IGetChildModelSuspendUseCase<Role, String, String>>()
        val controller = RolesController(
            mockk(), mockk(), mockk(), mockk(),
            getRoleUseCase, mockk(), mockk()
        )
        coEvery { getRoleUseCase(role.id, role.associationId) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.get(mockk(), association, role.id)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("roles_not_found", exception.key)
    }

    @Test
    fun testCreate() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val createRoleUseCase = mockk<ICreateChildModelSuspendUseCase<Role, CreateRolePayload, String>>()
        val controller = RolesController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            createRoleUseCase,
            mockk(),
            mockk(),
            mockk()
        )
        val payload = CreateRolePayload("name")
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.ROLES_CREATE inAssociation association) } returns true
        coEvery { createRoleUseCase(payload, role.associationId) } returns role
        assertEquals(role, controller.create(mockk(), association, payload))
    }

    @Test
    fun testCreateForbidden() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val controller = RolesController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            mockk()
        )
        val payload = CreateRolePayload("name")
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.ROLES_CREATE inAssociation association) } returns false
        val exception = assertFailsWith(ControllerException::class) {
            controller.create(mockk(), association, payload)
        }
        assertEquals(HttpStatusCode.Forbidden, exception.code)
        assertEquals("roles_create_not_allowed", exception.key)
    }

    @Test
    fun testCreateInternalError() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val createRoleUseCase = mockk<ICreateChildModelSuspendUseCase<Role, CreateRolePayload, String>>()
        val controller = RolesController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            createRoleUseCase,
            mockk(),
            mockk(),
            mockk()
        )
        val payload = CreateRolePayload("name")
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.ROLES_CREATE inAssociation association) } returns true
        coEvery { createRoleUseCase(payload, role.associationId) } returns null
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
        val getRoleUseCase = mockk<IGetChildModelSuspendUseCase<Role, String, String>>()
        val updateRoleUseCase = mockk<IUpdateChildModelSuspendUseCase<Role, String, UpdateRolePayload, String>>()
        val controller = RolesController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            getRoleUseCase,
            updateRoleUseCase,
            mockk()
        )
        val payload = UpdateRolePayload("newName")
        val updatedPage = role.copy(
            name = "newName"
        )
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.ROLES_UPDATE inAssociation association) } returns true
        coEvery { getRoleUseCase(role.id, role.associationId) } returns role
        coEvery { updateRoleUseCase(role.id, payload, role.associationId) } returns updatedPage
        assertEquals(updatedPage, controller.update(mockk(), association, role.id, payload))
    }

    @Test
    fun testUpdateInternalError() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getRoleUseCase = mockk<IGetChildModelSuspendUseCase<Role, String, String>>()
        val updateRoleUseCase = mockk<IUpdateChildModelSuspendUseCase<Role, String, UpdateRolePayload, String>>()
        val controller = RolesController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            getRoleUseCase,
            updateRoleUseCase,
            mockk()
        )
        val payload = UpdateRolePayload("newName")
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.ROLES_UPDATE inAssociation association) } returns true
        coEvery { getRoleUseCase(role.id, role.associationId) } returns role
        coEvery { updateRoleUseCase(role.id, payload, role.associationId) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.update(mockk(), association, role.id, payload)
        }
        assertEquals(HttpStatusCode.InternalServerError, exception.code)
        assertEquals("error_internal", exception.key)
    }

    @Test
    fun testUpdateNotFound() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getRoleUseCase = mockk<IGetChildModelSuspendUseCase<Role, String, String>>()
        val controller = RolesController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            getRoleUseCase,
            mockk(),
            mockk()
        )
        val payload = UpdateRolePayload("newName")
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.ROLES_UPDATE inAssociation association) } returns true
        coEvery { getRoleUseCase(role.id, role.associationId) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.update(mockk(), association, role.id, payload)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("roles_not_found", exception.key)
    }

    @Test
    fun testUpdateForbidden() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val controller = RolesController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            mockk()
        )
        val payload = UpdateRolePayload("newName")
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.ROLES_UPDATE inAssociation association) } returns false
        val exception = assertFailsWith(ControllerException::class) {
            controller.update(mockk(), association, role.id, payload)
        }
        assertEquals(HttpStatusCode.Forbidden, exception.code)
        assertEquals("roles_update_not_allowed", exception.key)
    }

    @Test
    fun testDelete() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getRoleUseCase = mockk<IGetChildModelSuspendUseCase<Role, String, String>>()
        val deleteRoleUseCase = mockk<IDeleteChildModelSuspendUseCase<Role, String, String>>()
        val controller = RolesController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            getRoleUseCase,
            mockk(),
            deleteRoleUseCase
        )
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.ROLES_DELETE inAssociation association) } returns true
        coEvery { getRoleUseCase(role.id, role.associationId) } returns role
        coEvery { deleteRoleUseCase(role.id, role.associationId) } returns true
        controller.delete(mockk(), association, role.id)
        coVerify {
            deleteRoleUseCase(role.id, role.associationId)
        }
    }

    @Test
    fun testDeleteInternalError() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getRoleUseCase = mockk<IGetChildModelSuspendUseCase<Role, String, String>>()
        val deleteRoleUseCase = mockk<IDeleteChildModelSuspendUseCase<Role, String, String>>()
        val controller = RolesController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            getRoleUseCase,
            mockk(),
            deleteRoleUseCase
        )
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.ROLES_DELETE inAssociation association) } returns true
        coEvery { getRoleUseCase(role.id, role.associationId) } returns role
        coEvery { deleteRoleUseCase(role.id, role.associationId) } returns false
        val exception = assertFailsWith(ControllerException::class) {
            controller.delete(mockk(), association, role.id)
        }
        assertEquals(HttpStatusCode.InternalServerError, exception.code)
        assertEquals("error_internal", exception.key)
    }

    @Test
    fun testDeleteNotFound() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getRoleUseCase = mockk<IGetChildModelSuspendUseCase<Role, String, String>>()
        val controller = RolesController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            getRoleUseCase,
            mockk(),
            mockk()
        )
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.ROLES_DELETE inAssociation association) } returns true
        coEvery { getRoleUseCase(role.id, role.associationId) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.delete(mockk(), association, role.id)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("roles_not_found", exception.key)
    }

    @Test
    fun testDeleteForbidden() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val controller = RolesController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            mockk()
        )
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.ROLES_DELETE inAssociation association) } returns false
        val exception = assertFailsWith(ControllerException::class) {
            controller.delete(mockk(), association, role.id)
        }
        assertEquals(HttpStatusCode.Forbidden, exception.code)
        assertEquals("roles_delete_not_allowed", exception.key)
    }

}
