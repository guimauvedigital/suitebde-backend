package me.nathanfallet.suitebde.controllers.users

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.models.get.IGetChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.slice.IListSliceChildModelSuspendUseCase
import me.nathanfallet.usecases.models.update.IUpdateChildModelSuspendUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UsersControllerTest {

    private val association = Association(
        "associationId", "name", "school", "city",
        true, Clock.System.now(), Clock.System.now()
    )
    private val user = User(
        "id", "associationId", "email", null,
        "firstname", "lastname", false
    )
    private val targetUser = User(
        "targetId", "associationId", "email", null,
        "firstname", "lastname", false
    )
    private val targetUser2 = User(
        "targetId2", "associationId2", "email2", null,
        "firstname2", "lastname2", false
    )

    @Test
    fun testList() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getUsersInAssociationUseCase = mockk<IListSliceChildModelSuspendUseCase<User, String>>()
        val call = mockk<ApplicationCall>()
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.USERS_VIEW inAssociation association.id) } returns true
        coEvery { getUsersInAssociationUseCase(10, 5, association.id) } returns listOf(targetUser)
        val controller = UsersController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            getUsersInAssociationUseCase,
            mockk(),
            mockk()
        )
        every { call.request.path() } returns "/api/v1/associations/id/users"
        every { call.parameters["limit"] } returns "10"
        every { call.parameters["offset"] } returns "5"
        assertEquals(listOf(targetUser), controller.list(call, association))
    }

    @Test
    fun testListDefaultLimitOffset() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getUsersInAssociationUseCase = mockk<IListSliceChildModelSuspendUseCase<User, String>>()
        val call = mockk<ApplicationCall>()
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.USERS_VIEW inAssociation association.id) } returns true
        coEvery { getUsersInAssociationUseCase(25, 0, association.id) } returns listOf(targetUser)
        val controller = UsersController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            getUsersInAssociationUseCase,
            mockk(),
            mockk()
        )
        every { call.request.path() } returns "/api/v1/associations/id/users"
        every { call.parameters["limit"] } returns null
        every { call.parameters["offset"] } returns null
        assertEquals(listOf(targetUser), controller.list(call, association))
    }

    @Test
    fun testListInvalidLimitOffset() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getUsersInAssociationUseCase = mockk<IListSliceChildModelSuspendUseCase<User, String>>()
        val call = mockk<ApplicationCall>()
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.USERS_VIEW inAssociation association.id) } returns true
        coEvery { getUsersInAssociationUseCase(25, 0, association.id) } returns listOf(targetUser)
        val controller = UsersController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            getUsersInAssociationUseCase,
            mockk(),
            mockk()
        )
        every { call.request.path() } returns "/api/v1/associations/id/users"
        every { call.parameters["limit"] } returns "a"
        every { call.parameters["offset"] } returns "b"
        assertEquals(listOf(targetUser), controller.list(call, association))
    }

    @Test
    fun testListAdmin() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getUsersInAssociationUseCase = mockk<IListChildModelSuspendUseCase<User, String>>()
        val call = mockk<ApplicationCall>()
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.USERS_VIEW inAssociation association.id) } returns true
        coEvery { getUsersInAssociationUseCase(association.id) } returns listOf(targetUser)
        val controller = UsersController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            getUsersInAssociationUseCase,
            mockk(),
            mockk(),
            mockk()
        )
        every { call.request.path() } returns "/admin/users"
        assertEquals(listOf(targetUser), controller.list(call, association))
    }

    @Test
    fun testGetAllForbidden() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.USERS_VIEW inAssociation association.id) } returns false
        val controller = UsersController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            mockk(),
            mockk()
        )
        val exception = assertFailsWith(ControllerException::class) {
            controller.list(call, association)
        }
        assertEquals(HttpStatusCode.Forbidden, exception.code)
        assertEquals("users_view_not_allowed", exception.key)
    }

    @Test
    fun testGet() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getUserUseCase = mockk<IGetChildModelSuspendUseCase<User, String, String>>()
        val call = mockk<ApplicationCall>()
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.USERS_VIEW inAssociation association.id) } returns true
        coEvery { getUserUseCase(targetUser.id, association.id) } returns targetUser
        val controller = UsersController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            getUserUseCase,
            mockk()
        )
        assertEquals(targetUser, controller.get(call, association, targetUser.id))
    }

    @Test
    fun testGetNotFound() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getUserUseCase = mockk<IGetChildModelSuspendUseCase<User, String, String>>()
        val call = mockk<ApplicationCall>()
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.USERS_VIEW inAssociation association.id) } returns true
        coEvery { getUserUseCase(targetUser.id, association.id) } returns null
        val controller = UsersController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            getUserUseCase,
            mockk()
        )
        val exception = assertFailsWith(ControllerException::class) {
            controller.get(call, association, targetUser.id)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("users_not_found", exception.key)
    }

    @Test
    fun testGetForbidden() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.USERS_VIEW inAssociation association.id) } returns false
        val controller = UsersController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            mockk(),
            mockk()
        )
        val exception = assertFailsWith(ControllerException::class) {
            controller.get(call, association, targetUser.id)
        }
        assertEquals(HttpStatusCode.Forbidden, exception.code)
        assertEquals("users_view_not_allowed", exception.key)
    }

    @Test
    fun testGetForbiddenButMe() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getUserUseCase = mockk<IGetChildModelSuspendUseCase<User, String, String>>()
        val call = mockk<ApplicationCall>()
        coEvery { requireUserForCallUseCase(call) } returns targetUser
        coEvery { checkPermissionUseCase(targetUser, Permission.USERS_VIEW inAssociation association.id) } returns false
        coEvery { getUserUseCase(targetUser.id, association.id) } returns targetUser
        val controller = UsersController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            getUserUseCase,
            mockk()
        )
        assertEquals(targetUser, controller.get(call, association, targetUser.id))
    }

    @Test
    fun testUpdate() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getUserUseCase = mockk<IGetChildModelSuspendUseCase<User, String, String>>()
        val updateUserUseCase = mockk<IUpdateChildModelSuspendUseCase<User, String, UpdateUserPayload, String>>()
        val call = mockk<ApplicationCall>()
        val updatedUser = targetUser.copy(
            firstName = "new firstname",
            lastName = "new lastname"
        )
        val payload = UpdateUserPayload(
            "new firstname", "new lastname", null
        )
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.USERS_UPDATE inAssociation association.id) } returns true
        coEvery { getUserUseCase(targetUser.id, association.id) } returns targetUser
        coEvery { updateUserUseCase(targetUser.id, payload, association.id) } returns updatedUser
        val controller = UsersController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            getUserUseCase,
            updateUserUseCase
        )
        assertEquals(updatedUser, controller.update(call, association, targetUser.id, payload))
    }

    @Test
    fun testUpdateWithPassword() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getUserUseCase = mockk<IGetChildModelSuspendUseCase<User, String, String>>()
        val updateUserUseCase = mockk<IUpdateChildModelSuspendUseCase<User, String, UpdateUserPayload, String>>()
        val call = mockk<ApplicationCall>()
        val updatedUser = targetUser.copy(
            firstName = "new firstname",
            lastName = "new lastname",
            password = "new password"
        )
        val payload = UpdateUserPayload(
            "new firstname", "new lastname", "new password"
        )
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.USERS_UPDATE inAssociation association.id) } returns true
        coEvery { getUserUseCase(targetUser.id, association.id) } returns targetUser
        coEvery { updateUserUseCase(targetUser.id, payload, association.id) } returns updatedUser
        val controller = UsersController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            getUserUseCase,
            updateUserUseCase
        )
        assertEquals(updatedUser, controller.update(call, association, targetUser.id, payload))
    }

    @Test
    fun testUpdateWithNoChange() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getUserUseCase = mockk<IGetChildModelSuspendUseCase<User, String, String>>()
        val updateUserUseCase = mockk<IUpdateChildModelSuspendUseCase<User, String, UpdateUserPayload, String>>()
        val call = mockk<ApplicationCall>()
        val payload = UpdateUserPayload(null, null, null)
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.USERS_UPDATE inAssociation association.id) } returns true
        coEvery { getUserUseCase(targetUser.id, association.id) } returns targetUser
        coEvery { updateUserUseCase(targetUser.id, payload, association.id) } returns targetUser
        val controller = UsersController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            getUserUseCase,
            updateUserUseCase
        )
        assertEquals(targetUser, controller.update(call, association, targetUser.id, payload))
    }

    @Test
    fun testUpdateNotFound() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getUserUseCase = mockk<IGetChildModelSuspendUseCase<User, String, String>>()
        val call = mockk<ApplicationCall>()
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.USERS_UPDATE inAssociation association.id) } returns true
        coEvery { getUserUseCase(targetUser.id, association.id) } returns null
        val controller = UsersController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            getUserUseCase,
            mockk()
        )
        val exception = assertFailsWith(ControllerException::class) {
            controller.update(call, association, targetUser.id, UpdateUserPayload(null, null, null))
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("users_not_found", exception.key)
    }

    @Test
    fun testUpdateForbidden() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.USERS_UPDATE inAssociation association.id) } returns false
        val controller = UsersController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            mockk(),
            mockk()
        )
        val exception = assertFailsWith(ControllerException::class) {
            controller.update(call, association, targetUser.id, UpdateUserPayload(null, null, null))
        }
        assertEquals(HttpStatusCode.Forbidden, exception.code)
        assertEquals("users_update_not_allowed", exception.key)
    }

    @Test
    fun testUpdateForbiddenButMe() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getUserUseCase = mockk<IGetChildModelSuspendUseCase<User, String, String>>()
        val updateUserUseCase = mockk<IUpdateChildModelSuspendUseCase<User, String, UpdateUserPayload, String>>()
        val call = mockk<ApplicationCall>()
        val updatedUser = targetUser.copy(
            firstName = "new firstname",
            lastName = "new lastname"
        )
        val payload = UpdateUserPayload(
            "new firstname", "new lastname", null
        )
        coEvery { requireUserForCallUseCase(call) } returns targetUser
        coEvery {
            checkPermissionUseCase(
                targetUser,
                Permission.USERS_UPDATE inAssociation association.id
            )
        } returns false
        coEvery { getUserUseCase(targetUser.id, association.id) } returns targetUser
        coEvery { updateUserUseCase(targetUser.id, payload, association.id) } returns updatedUser
        val controller = UsersController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            getUserUseCase,
            updateUserUseCase
        )
        assertEquals(updatedUser, controller.update(call, association, targetUser.id, payload))
    }

    @Test
    fun testUpdateInternalError() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getUserUseCase = mockk<IGetChildModelSuspendUseCase<User, String, String>>()
        val updateUserUseCase = mockk<IUpdateChildModelSuspendUseCase<User, String, UpdateUserPayload, String>>()
        val call = mockk<ApplicationCall>()
        val payload = UpdateUserPayload(null, null, null)
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.USERS_UPDATE inAssociation association.id) } returns true
        coEvery { getUserUseCase(targetUser.id, association.id) } returns targetUser
        coEvery { updateUserUseCase(targetUser.id, payload, association.id) } returns null
        val controller = UsersController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            getUserUseCase,
            updateUserUseCase
        )
        val exception = assertFailsWith(ControllerException::class) {
            controller.update(call, association, targetUser.id, payload)
        }
        assertEquals(HttpStatusCode.InternalServerError, exception.code)
        assertEquals("error_internal", exception.key)
    }

}
