package me.nathanfallet.suitebde.controllers.users

import io.ktor.http.*
import io.ktor.server.application.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.users.IGetUsersInAssociationUseCase
import me.nathanfallet.suitebde.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.usecases.models.get.IGetChildModelSuspendUseCase
import me.nathanfallet.usecases.models.update.IUpdateChildModelSuspendUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

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
    fun testGetAll() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getUsersInAssociationUseCase = mockk<IGetUsersInAssociationUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.USERS_VIEW inAssociation association) } returns true
        coEvery { getUsersInAssociationUseCase(association.id) } returns listOf(targetUser)
        val controller = UsersController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            getUsersInAssociationUseCase,
            mockk(),
            mockk()
        )
        assertEquals(listOf(targetUser), controller.getAll(call, association))
    }

    @Test
    fun testGetAllForbidden() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.USERS_VIEW inAssociation association) } returns false
        val controller = UsersController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            mockk()
        )
        val exception = assertThrows<ControllerException> {
            controller.getAll(call, association)
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
        coEvery { checkPermissionUseCase(user, Permission.USERS_VIEW inAssociation association) } returns true
        coEvery { getUserUseCase(targetUser.id, association.id) } returns targetUser
        val controller = UsersController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
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
        coEvery { checkPermissionUseCase(user, Permission.USERS_VIEW inAssociation association) } returns true
        coEvery { getUserUseCase(targetUser.id, association.id) } returns null
        val controller = UsersController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            getUserUseCase,
            mockk()
        )
        val exception = assertThrows<ControllerException> {
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
        coEvery { checkPermissionUseCase(user, Permission.USERS_VIEW inAssociation association) } returns false
        val controller = UsersController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            mockk()
        )
        val exception = assertThrows<ControllerException> {
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
        coEvery { checkPermissionUseCase(targetUser, Permission.USERS_VIEW inAssociation association) } returns false
        coEvery { getUserUseCase(targetUser.id, association.id) } returns targetUser
        val controller = UsersController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            getUserUseCase,
            mockk()
        )
        assertEquals(targetUser, controller.get(call, association, targetUser.id))
    }

    @Test
    fun testCreate() = runBlocking {
        val call = mockk<ApplicationCall>()
        val controller = UsersController(mockk(), mockk(), mockk(), mockk(), mockk())
        val exception = assertThrows<ControllerException> {
            controller.create(
                call, association,
                CreateUserPayload("email", "password", "firstname", "lastname", false)
            )
        }
        assertEquals(HttpStatusCode.MethodNotAllowed, exception.code)
        assertEquals("users_create_not_allowed", exception.key)
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
        coEvery { checkPermissionUseCase(user, Permission.USERS_UPDATE inAssociation association) } returns true
        coEvery { getUserUseCase(targetUser.id, association.id) } returns targetUser
        coEvery { updateUserUseCase(targetUser.id, payload, association.id) } returns updatedUser
        val controller = UsersController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
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
        coEvery { checkPermissionUseCase(user, Permission.USERS_UPDATE inAssociation association) } returns true
        coEvery { getUserUseCase(targetUser.id, association.id) } returns targetUser
        coEvery { updateUserUseCase(targetUser.id, payload, association.id) } returns updatedUser
        val controller = UsersController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
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
        coEvery { checkPermissionUseCase(user, Permission.USERS_UPDATE inAssociation association) } returns true
        coEvery { getUserUseCase(targetUser.id, association.id) } returns targetUser
        coEvery { updateUserUseCase(targetUser.id, payload, association.id) } returns targetUser
        val controller = UsersController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
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
        coEvery { checkPermissionUseCase(user, Permission.USERS_UPDATE inAssociation association) } returns true
        coEvery { getUserUseCase(targetUser.id, association.id) } returns null
        val controller = UsersController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            getUserUseCase,
            mockk()
        )
        val exception = assertThrows<ControllerException> {
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
        coEvery { checkPermissionUseCase(user, Permission.USERS_UPDATE inAssociation association) } returns false
        val controller = UsersController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            mockk()
        )
        val exception = assertThrows<ControllerException> {
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
        coEvery { checkPermissionUseCase(targetUser, Permission.USERS_UPDATE inAssociation association) } returns false
        coEvery { getUserUseCase(targetUser.id, association.id) } returns targetUser
        coEvery { updateUserUseCase(targetUser.id, payload, association.id) } returns updatedUser
        val controller = UsersController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
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
        coEvery { checkPermissionUseCase(user, Permission.USERS_UPDATE inAssociation association) } returns true
        coEvery { getUserUseCase(targetUser.id, association.id) } returns targetUser
        coEvery { updateUserUseCase(targetUser.id, payload, association.id) } returns null
        val controller = UsersController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            getUserUseCase,
            updateUserUseCase
        )
        val exception = assertThrows<ControllerException> {
            controller.update(call, association, targetUser.id, payload)
        }
        assertEquals(HttpStatusCode.InternalServerError, exception.code)
        assertEquals("error_internal", exception.key)
    }

    @Test
    fun testDelete() = runBlocking {
        val call = mockk<ApplicationCall>()
        val controller = UsersController(mockk(), mockk(), mockk(), mockk(), mockk())
        val exception = assertThrows<ControllerException> {
            controller.delete(call, association, "id")
        }
        assertEquals(HttpStatusCode.MethodNotAllowed, exception.code)
        assertEquals("users_delete_not_allowed", exception.key)
    }

}
