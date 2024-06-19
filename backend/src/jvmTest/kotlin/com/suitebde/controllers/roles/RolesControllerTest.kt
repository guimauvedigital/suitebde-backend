package com.suitebde.controllers.roles

import com.suitebde.models.associations.Association
import com.suitebde.models.roles.CreateRolePayload
import com.suitebde.models.roles.Permission
import com.suitebde.models.roles.Role
import com.suitebde.models.roles.UpdateRolePayload
import com.suitebde.models.users.User
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

class RolesControllerTest {

    private val association = Association(
        UUID(), "name", "school", "city",
        true, Clock.System.now(), Clock.System.now()
    )
    private val user = User(
        UUID(), UUID(), "email", null,
        "firstname", "lastname", false, Clock.System.now()
    )
    private val role = Role(UUID(), UUID(), "name")

    @Test
    fun testList() = runBlocking {
        val getRolesInAssociationUseCase = mockk<IListSliceChildModelSuspendUseCase<Role, UUID>>()
        val call = mockk<ApplicationCall>()
        coEvery { getRolesInAssociationUseCase(Pagination(10, 5), association.id) } returns listOf(role)
        val controller = RolesController(
            mockk(), mockk(), mockk(), getRolesInAssociationUseCase,
            mockk(), mockk(), mockk(), mockk()
        )
        every { call.request.path() } returns "/api/v1/associations/id/roles"
        assertEquals(listOf(role), controller.list(call, association, 10, 5))
    }

    @Test
    fun testListDefaultLimitOffset() = runBlocking {
        val getRolesInAssociationUseCase = mockk<IListSliceChildModelSuspendUseCase<Role, UUID>>()
        val call = mockk<ApplicationCall>()
        coEvery { getRolesInAssociationUseCase(Pagination(25, 0), association.id) } returns listOf(role)
        val controller = RolesController(
            mockk(), mockk(), mockk(), getRolesInAssociationUseCase,
            mockk(), mockk(), mockk(), mockk()
        )
        every { call.request.path() } returns "/api/v1/associations/id/roles"
        assertEquals(listOf(role), controller.list(call, association, null, null))
    }

    @Test
    fun testListAdmin() = runBlocking {
        val getRolesInAssociationUseCase = mockk<IListChildModelSuspendUseCase<Role, UUID>>()
        val call = mockk<ApplicationCall>()
        coEvery { getRolesInAssociationUseCase(association.id) } returns listOf(role)
        val controller = RolesController(
            mockk(), mockk(), getRolesInAssociationUseCase, mockk(),
            mockk(), mockk(), mockk(), mockk()
        )
        every { call.request.path() } returns "/admin/roles"
        assertEquals(listOf(role), controller.list(call, association, null, null))
    }

    @Test
    fun testGet() = runBlocking {
        val getRoleUseCase = mockk<IGetChildModelSuspendUseCase<Role, UUID, UUID>>()
        val controller = RolesController(
            mockk(), mockk(), mockk(), mockk(), mockk(),
            getRoleUseCase, mockk(), mockk()
        )
        coEvery { getRoleUseCase(role.id, role.associationId) } returns role
        assertEquals(role, controller.get(mockk(), association, role.id))
    }

    @Test
    fun testGetNotFound() = runBlocking {
        val getRoleUseCase = mockk<IGetChildModelSuspendUseCase<Role, UUID, UUID>>()
        val controller = RolesController(
            mockk(), mockk(), mockk(), mockk(), mockk(),
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
        val createRoleUseCase = mockk<ICreateChildModelSuspendUseCase<Role, CreateRolePayload, UUID>>()
        val controller = RolesController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            createRoleUseCase,
            mockk(),
            mockk(),
            mockk()
        )
        val payload = CreateRolePayload("name")
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.ROLES_CREATE inAssociation association.id) } returns true
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
            mockk(),
            mockk()
        )
        val payload = CreateRolePayload("name")
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.ROLES_CREATE inAssociation association.id) } returns false
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
        val createRoleUseCase = mockk<ICreateChildModelSuspendUseCase<Role, CreateRolePayload, UUID>>()
        val controller = RolesController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            createRoleUseCase,
            mockk(),
            mockk(),
            mockk()
        )
        val payload = CreateRolePayload("name")
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.ROLES_CREATE inAssociation association.id) } returns true
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
        val getRoleUseCase = mockk<IGetChildModelSuspendUseCase<Role, UUID, UUID>>()
        val updateRoleUseCase = mockk<IUpdateChildModelSuspendUseCase<Role, UUID, UpdateRolePayload, UUID>>()
        val controller = RolesController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
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
        coEvery { checkPermissionUseCase(user, Permission.ROLES_UPDATE inAssociation association.id) } returns true
        coEvery { getRoleUseCase(role.id, role.associationId) } returns role
        coEvery { updateRoleUseCase(role.id, payload, role.associationId) } returns updatedPage
        assertEquals(updatedPage, controller.update(mockk(), association, role.id, payload))
    }

    @Test
    fun testUpdateInternalError() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getRoleUseCase = mockk<IGetChildModelSuspendUseCase<Role, UUID, UUID>>()
        val updateRoleUseCase = mockk<IUpdateChildModelSuspendUseCase<Role, UUID, UpdateRolePayload, UUID>>()
        val controller = RolesController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            mockk(),
            getRoleUseCase,
            updateRoleUseCase,
            mockk()
        )
        val payload = UpdateRolePayload("newName")
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.ROLES_UPDATE inAssociation association.id) } returns true
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
        val getRoleUseCase = mockk<IGetChildModelSuspendUseCase<Role, UUID, UUID>>()
        val controller = RolesController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            mockk(),
            getRoleUseCase,
            mockk(),
            mockk()
        )
        val payload = UpdateRolePayload("newName")
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.ROLES_UPDATE inAssociation association.id) } returns true
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
            mockk(),
            mockk()
        )
        val payload = UpdateRolePayload("newName")
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.ROLES_UPDATE inAssociation association.id) } returns false
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
        val getRoleUseCase = mockk<IGetChildModelSuspendUseCase<Role, UUID, UUID>>()
        val deleteRoleUseCase = mockk<IDeleteChildModelSuspendUseCase<Role, UUID, UUID>>()
        val controller = RolesController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            mockk(),
            getRoleUseCase,
            mockk(),
            deleteRoleUseCase
        )
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.ROLES_DELETE inAssociation association.id) } returns true
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
        val getRoleUseCase = mockk<IGetChildModelSuspendUseCase<Role, UUID, UUID>>()
        val deleteRoleUseCase = mockk<IDeleteChildModelSuspendUseCase<Role, UUID, UUID>>()
        val controller = RolesController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            mockk(),
            getRoleUseCase,
            mockk(),
            deleteRoleUseCase
        )
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.ROLES_DELETE inAssociation association.id) } returns true
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
        val getRoleUseCase = mockk<IGetChildModelSuspendUseCase<Role, UUID, UUID>>()
        val controller = RolesController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            mockk(),
            getRoleUseCase,
            mockk(),
            mockk()
        )
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.ROLES_DELETE inAssociation association.id) } returns true
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
            mockk(),
            mockk()
        )
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.ROLES_DELETE inAssociation association.id) } returns false
        val exception = assertFailsWith(ControllerException::class) {
            controller.delete(mockk(), association, role.id)
        }
        assertEquals(HttpStatusCode.Forbidden, exception.code)
        assertEquals("roles_delete_not_allowed", exception.key)
    }

}
