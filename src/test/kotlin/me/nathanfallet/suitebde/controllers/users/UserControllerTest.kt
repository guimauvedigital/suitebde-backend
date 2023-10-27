package me.nathanfallet.suitebde.controllers.users

import io.ktor.http.*
import io.ktor.server.application.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.models.LocalizedString
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForCallUseCase
import me.nathanfallet.suitebde.usecases.roles.ICheckPermissionUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUserForCallUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUserUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUsersInAssociationUseCase
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class UserControllerTest {

    private val association = Association("associationId", "name")
    private val user = User("id", "associationId", "email", "password", "firstname", "lastname", false)
    private val targetUser = User("targetId", "associationId", "email", "password", "firstname", "lastname", false)
    private val targetUser2 =
        User("targetId2", "associationId2", "email2", "password2", "firstname2", "lastname2", false)

    @Test
    fun testGetAll() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        val getUsersInAssociationUseCase = mockk<IGetUsersInAssociationUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(Pair(user, Permission.USERS_VIEW)) } returns true
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
        assertEquals(LocalizedString.ERROR_ASSOCIATIONS_NOT_FOUND, exception.error)
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
        assertEquals(LocalizedString.ERROR_USERS_VIEW_NOT_ALLOWED, exception.error)
    }

    @Test
    fun testGetAllUnauthorized() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(Pair(user, Permission.USERS_VIEW)) } returns false
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
        assertEquals(HttpStatusCode.Unauthorized, exception.code)
        assertEquals(LocalizedString.ERROR_USERS_VIEW_NOT_ALLOWED, exception.error)
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
        coEvery { checkPermissionUseCase(Pair(user, Permission.USERS_VIEW)) } returns true
        coEvery { getUserUseCase(targetUser.id) } returns targetUser
        every { call.parameters["id"] } returns targetUser.id
        val controller = UserController(
            getAssociationForCallUseCase,
            getUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            getUserUseCase,
            mockk()
        )
        assertEquals(targetUser, controller.get(call))
    }

    @Test
    fun testGetNoAssociation() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationForCallUseCase(call) } returns null
        val controller = UserController(getAssociationForCallUseCase, mockk(), mockk(), mockk(), mockk(), mockk())
        val exception = assertThrows<ControllerException> {
            controller.get(call)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals(LocalizedString.ERROR_ASSOCIATIONS_NOT_FOUND, exception.error)
    }

    @Test
    fun testGetNoUser() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns null
        every { call.parameters["id"] } returns targetUser.id
        val controller =
            UserController(getAssociationForCallUseCase, getUserForCallUseCase, mockk(), mockk(), mockk(), mockk())
        val exception = assertThrows<ControllerException> {
            controller.get(call)
        }
        assertEquals(HttpStatusCode.Unauthorized, exception.code)
        assertEquals(LocalizedString.ERROR_USERS_VIEW_NOT_ALLOWED, exception.error)
    }

    @Test
    fun testGetNoId() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationForCallUseCase(call) } returns association
        every { call.parameters["id"] } returns null
        val controller = UserController(getAssociationForCallUseCase, mockk(), mockk(), mockk(), mockk(), mockk())
        val exception = assertThrows<ControllerException> {
            controller.get(call)
        }
        assertEquals(HttpStatusCode.BadRequest, exception.code)
        assertEquals(LocalizedString.ERROR_MISSING_ID, exception.error)
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
        coEvery { checkPermissionUseCase(Pair(user, Permission.USERS_VIEW)) } returns true
        coEvery { getUserUseCase(targetUser.id) } returns null
        every { call.parameters["id"] } returns targetUser.id
        val controller = UserController(
            getAssociationForCallUseCase,
            getUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            getUserUseCase,
            mockk()
        )
        val exception = assertThrows<ControllerException> {
            controller.get(call)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals(LocalizedString.ERROR_USERS_NOT_FOUND, exception.error)
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
        coEvery { checkPermissionUseCase(Pair(user, Permission.USERS_VIEW)) } returns true
        coEvery { getUserUseCase(targetUser2.id) } returns targetUser2
        every { call.parameters["id"] } returns targetUser2.id
        val controller = UserController(
            getAssociationForCallUseCase,
            getUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            getUserUseCase,
            mockk()
        )
        val exception = assertThrows<ControllerException> {
            controller.get(call)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals(LocalizedString.ERROR_USERS_NOT_FOUND, exception.error)
    }

    @Test
    fun testGetUnauthorized() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(Pair(user, Permission.USERS_VIEW)) } returns false
        every { call.parameters["id"] } returns targetUser.id
        val controller = UserController(
            getAssociationForCallUseCase,
            getUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            mockk()
        )
        val exception = assertThrows<ControllerException> {
            controller.get(call)
        }
        assertEquals(HttpStatusCode.Unauthorized, exception.code)
        assertEquals(LocalizedString.ERROR_USERS_VIEW_NOT_ALLOWED, exception.error)
    }

    @Test
    fun testGetUnauthorizedButMe() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        val getUserUseCase = mockk<IGetUserUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns targetUser
        coEvery { checkPermissionUseCase(Pair(targetUser, Permission.USERS_VIEW)) } returns false
        every { call.parameters["id"] } returns targetUser.id
        coEvery { getUserUseCase(targetUser.id) } returns targetUser
        val controller = UserController(
            getAssociationForCallUseCase,
            getUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            getUserUseCase,
            mockk()
        )
        assertEquals(targetUser, controller.get(call))
    }

    @Test
    fun testCreate() = runBlocking {
        val call = mockk<ApplicationCall>()
        val controller = UserController(mockk(), mockk(), mockk(), mockk(), mockk(), mockk())
        val exception = assertThrows<ControllerException> {
            controller.create(call, Unit)
        }
        assertEquals(HttpStatusCode.MethodNotAllowed, exception.code)
        assertEquals(LocalizedString.ERROR_USERS_CREATE_NOT_ALLOWED, exception.error)
    }

    @Test
    fun testUpdate() = runBlocking {
        // TODO
    }

    @Test
    fun testDelete() = runBlocking {
        val call = mockk<ApplicationCall>()
        val controller = UserController(mockk(), mockk(), mockk(), mockk(), mockk(), mockk())
        val exception = assertThrows<ControllerException> {
            controller.delete(call)
        }
        assertEquals(HttpStatusCode.MethodNotAllowed, exception.code)
        assertEquals(LocalizedString.ERROR_USERS_DELETE_NOT_ALLOWED, exception.error)
    }

}