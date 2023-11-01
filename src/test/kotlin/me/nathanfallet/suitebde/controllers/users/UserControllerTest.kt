package me.nathanfallet.suitebde.controllers.users

import io.ktor.http.*
import io.ktor.server.application.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.ktor.routers.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForCallUseCase
import me.nathanfallet.suitebde.usecases.roles.ICheckPermissionUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUserForCallUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUserUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUsersInAssociationUseCase
import me.nathanfallet.suitebde.usecases.users.IUpdateUserUseCase
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class UserControllerTest {

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
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        val getUsersInAssociationUseCase = mockk<IGetUsersInAssociationUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.USERS_VIEW inAssociation association) } returns true
        coEvery { getUsersInAssociationUseCase(association.id) } returns listOf(targetUser)
        val controller = UserController(
            getAssociationForCallUseCase,
            getUserForCallUseCase,
            checkPermissionUseCase,
            getUsersInAssociationUseCase,
            mockk(),
            mockk()
        )
        assertEquals(listOf(targetUser), controller.getAll(call))
    }

    @Test
    fun testGetAllNoAssociation() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationForCallUseCase(call) } returns null
        val controller = UserController(getAssociationForCallUseCase, mockk(), mockk(), mockk(), mockk(), mockk())
        val exception = assertThrows<ControllerException> {
            controller.getAll(call)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("associations_not_found", exception.key)
    }

    @Test
    fun testGetAllNoUser() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns null
        val controller =
            UserController(getAssociationForCallUseCase, getUserForCallUseCase, mockk(), mockk(), mockk(), mockk())
        val exception = assertThrows<ControllerException> {
            controller.getAll(call)
        }
        assertEquals(HttpStatusCode.Unauthorized, exception.code)
        assertEquals("auth_invalid_credentials", exception.key)
    }

    @Test
    fun testGetAllForbidden() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.USERS_VIEW inAssociation association) } returns false
        val controller = UserController(
            getAssociationForCallUseCase,
            getUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            mockk()
        )
        val exception = assertThrows<ControllerException> {
            controller.getAll(call)
        }
        assertEquals(HttpStatusCode.Forbidden, exception.code)
        assertEquals("users_view_not_allowed", exception.key)
    }

    @Test
    fun testGet() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        val getUserUseCase = mockk<IGetUserUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.USERS_VIEW inAssociation association) } returns true
        coEvery { getUserUseCase(targetUser.id) } returns targetUser
        val controller = UserController(
            getAssociationForCallUseCase,
            getUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            getUserUseCase,
            mockk()
        )
        assertEquals(targetUser, controller.get(call, targetUser.id))
    }

    @Test
    fun testGetNoAssociation() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationForCallUseCase(call) } returns null
        val controller = UserController(getAssociationForCallUseCase, mockk(), mockk(), mockk(), mockk(), mockk())
        val exception = assertThrows<ControllerException> {
            controller.get(call, targetUser.id)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("associations_not_found", exception.key)
    }

    @Test
    fun testGetNoUser() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns null
        val controller = UserController(
            getAssociationForCallUseCase, getUserForCallUseCase,
            mockk(), mockk(), mockk(), mockk()
        )
        val exception = assertThrows<ControllerException> {
            controller.get(call, targetUser.id)
        }
        assertEquals(HttpStatusCode.Unauthorized, exception.code)
        assertEquals("auth_invalid_credentials", exception.key)
    }

    @Test
    fun testGetNotFound() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        val getUserUseCase = mockk<IGetUserUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.USERS_VIEW inAssociation association) } returns true
        coEvery { getUserUseCase(targetUser.id) } returns null
        val controller = UserController(
            getAssociationForCallUseCase,
            getUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            getUserUseCase,
            mockk()
        )
        val exception = assertThrows<ControllerException> {
            controller.get(call, targetUser.id)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("users_not_found", exception.key)
    }

    @Test
    fun testGetNotInAssociation() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        val getUserUseCase = mockk<IGetUserUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.USERS_VIEW inAssociation association) } returns true
        coEvery { getUserUseCase(targetUser2.id) } returns targetUser2
        val controller = UserController(
            getAssociationForCallUseCase,
            getUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            getUserUseCase,
            mockk()
        )
        val exception = assertThrows<ControllerException> {
            controller.get(call, targetUser2.id)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("users_not_found", exception.key)
    }

    @Test
    fun testGetForbidden() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.USERS_VIEW inAssociation association) } returns false
        val controller = UserController(
            getAssociationForCallUseCase,
            getUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            mockk()
        )
        val exception = assertThrows<ControllerException> {
            controller.get(call, targetUser.id)
        }
        assertEquals(HttpStatusCode.Forbidden, exception.code)
        assertEquals("users_view_not_allowed", exception.key)
    }

    @Test
    fun testGetForbiddenButMe() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        val getUserUseCase = mockk<IGetUserUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns targetUser
        coEvery { checkPermissionUseCase(targetUser, Permission.USERS_VIEW inAssociation association) } returns false
        coEvery { getUserUseCase(targetUser.id) } returns targetUser
        val controller = UserController(
            getAssociationForCallUseCase,
            getUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            getUserUseCase,
            mockk()
        )
        assertEquals(targetUser, controller.get(call, targetUser.id))
    }

    @Test
    fun testCreate() = runBlocking {
        val call = mockk<ApplicationCall>()
        val controller = UserController(mockk(), mockk(), mockk(), mockk(), mockk(), mockk())
        val exception = assertThrows<ControllerException> {
            controller.create(call, Unit)
        }
        assertEquals(HttpStatusCode.MethodNotAllowed, exception.code)
        assertEquals("users_create_not_allowed", exception.key)
    }

    @Test
    fun testUpdate() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        val getUserUseCase = mockk<IGetUserUseCase>()
        val updateUserUseCase = mockk<IUpdateUserUseCase>()
        val call = mockk<ApplicationCall>()
        val updatedUser = targetUser.copy(
            firstName = "new firstname",
            lastName = "new lastname"
        )
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.USERS_UPDATE inAssociation association) } returns true
        coEvery { getUserUseCase(targetUser.id) } returns targetUser
        coEvery { updateUserUseCase(updatedUser) } returns updatedUser
        val controller = UserController(
            getAssociationForCallUseCase,
            getUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            getUserUseCase,
            updateUserUseCase
        )
        assertEquals(
            updatedUser,
            controller.update(
                call, targetUser.id, UpdateUserPayload(
                    "new firstname", "new lastname", null
                )
            )
        )
    }

    @Test
    fun testUpdateWithPassword() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        val getUserUseCase = mockk<IGetUserUseCase>()
        val updateUserUseCase = mockk<IUpdateUserUseCase>()
        val call = mockk<ApplicationCall>()
        val updatedUser = targetUser.copy(
            firstName = "new firstname",
            lastName = "new lastname",
            password = "new password"
        )
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.USERS_UPDATE inAssociation association) } returns true
        coEvery { getUserUseCase(targetUser.id) } returns targetUser
        coEvery { updateUserUseCase(updatedUser) } returns updatedUser
        val controller = UserController(
            getAssociationForCallUseCase,
            getUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            getUserUseCase,
            updateUserUseCase
        )
        assertEquals(
            updatedUser,
            controller.update(
                call, targetUser.id, UpdateUserPayload(
                    "new firstname", "new lastname", "new password"
                )
            )
        )
    }

    @Test
    fun testUpdateWithNoChange() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        val getUserUseCase = mockk<IGetUserUseCase>()
        val updateUserUseCase = mockk<IUpdateUserUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.USERS_UPDATE inAssociation association) } returns true
        coEvery { getUserUseCase(targetUser.id) } returns targetUser
        coEvery { updateUserUseCase(targetUser) } returns targetUser
        val controller = UserController(
            getAssociationForCallUseCase,
            getUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            getUserUseCase,
            updateUserUseCase
        )
        assertEquals(
            targetUser,
            controller.update(call, targetUser.id, UpdateUserPayload(null, null, null))
        )
    }

    @Test
    fun testUpdateNoAssociation() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationForCallUseCase(call) } returns null
        val controller = UserController(getAssociationForCallUseCase, mockk(), mockk(), mockk(), mockk(), mockk())
        val exception = assertThrows<ControllerException> {
            controller.update(call, targetUser.id, UpdateUserPayload(null, null, null))
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("associations_not_found", exception.key)
    }

    @Test
    fun testUpdateNoUser() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns null
        val controller = UserController(
            getAssociationForCallUseCase, getUserForCallUseCase,
            mockk(), mockk(), mockk(), mockk()
        )
        val exception = assertThrows<ControllerException> {
            controller.update(call, targetUser.id, UpdateUserPayload(null, null, null))
        }
        assertEquals(HttpStatusCode.Unauthorized, exception.code)
        assertEquals("auth_invalid_credentials", exception.key)
    }

    @Test
    fun testUpdateNotFound() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        val getUserUseCase = mockk<IGetUserUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.USERS_UPDATE inAssociation association) } returns true
        coEvery { getUserUseCase(targetUser.id) } returns null
        val controller = UserController(
            getAssociationForCallUseCase,
            getUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            getUserUseCase,
            mockk()
        )
        val exception = assertThrows<ControllerException> {
            controller.update(call, targetUser.id, UpdateUserPayload(null, null, null))
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("users_not_found", exception.key)
    }

    @Test
    fun testUpdateNotInAssociation() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        val getUserUseCase = mockk<IGetUserUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.USERS_UPDATE inAssociation association) } returns true
        coEvery { getUserUseCase(targetUser2.id) } returns targetUser2
        val controller = UserController(
            getAssociationForCallUseCase,
            getUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            getUserUseCase,
            mockk()
        )
        val exception = assertThrows<ControllerException> {
            controller.update(call, targetUser2.id, UpdateUserPayload(null, null, null))
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("users_not_found", exception.key)
    }

    @Test
    fun testUpdateForbidden() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.USERS_UPDATE inAssociation association) } returns false
        val controller = UserController(
            getAssociationForCallUseCase,
            getUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            mockk()
        )
        val exception = assertThrows<ControllerException> {
            controller.update(call, targetUser.id, UpdateUserPayload(null, null, null))
        }
        assertEquals(HttpStatusCode.Forbidden, exception.code)
        assertEquals("users_update_not_allowed", exception.key)
    }

    @Test
    fun testUpdateForbiddenButMe() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        val getUserUseCase = mockk<IGetUserUseCase>()
        val updateUserUseCase = mockk<IUpdateUserUseCase>()
        val call = mockk<ApplicationCall>()
        val updatedUser = targetUser.copy(
            firstName = "new firstname",
            lastName = "new lastname"
        )
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns targetUser
        coEvery { checkPermissionUseCase(targetUser, Permission.USERS_UPDATE inAssociation association) } returns false
        coEvery { getUserUseCase(targetUser.id) } returns targetUser
        coEvery { updateUserUseCase(updatedUser) } returns updatedUser
        val controller = UserController(
            getAssociationForCallUseCase,
            getUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            getUserUseCase,
            updateUserUseCase
        )
        assertEquals(
            updatedUser,
            controller.update(
                call, targetUser.id, UpdateUserPayload(
                    "new firstname", "new lastname", null
                )
            )
        )
    }

    @Test
    fun testUpdateInternalError() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        val getUserUseCase = mockk<IGetUserUseCase>()
        val updateUserUseCase = mockk<IUpdateUserUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.USERS_UPDATE inAssociation association) } returns true
        coEvery { getUserUseCase(targetUser.id) } returns targetUser
        coEvery { updateUserUseCase(targetUser) } returns null
        val controller = UserController(
            getAssociationForCallUseCase,
            getUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            getUserUseCase,
            updateUserUseCase
        )
        val exception = assertThrows<ControllerException> {
            controller.update(call, targetUser.id, UpdateUserPayload(null, null, null))
        }
        assertEquals(HttpStatusCode.InternalServerError, exception.code)
        assertEquals("error_internal", exception.key)
    }

    @Test
    fun testDelete() = runBlocking {
        val call = mockk<ApplicationCall>()
        val controller = UserController(mockk(), mockk(), mockk(), mockk(), mockk(), mockk())
        val exception = assertThrows<ControllerException> {
            controller.delete(call, "id")
        }
        assertEquals(HttpStatusCode.MethodNotAllowed, exception.code)
        assertEquals("users_delete_not_allowed", exception.key)
    }

}