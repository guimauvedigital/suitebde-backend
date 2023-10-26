package me.nathanfallet.suitebde.controllers.users

import io.ktor.http.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.models.LocalizedString
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.roles.ICheckPermissionUseCase
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
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        val getUsersInAssociationUseCase = mockk<IGetUsersInAssociationUseCase>()
        coEvery { checkPermissionUseCase(Pair(user, Permission.USERS_VIEW)) } returns true
        coEvery { getUsersInAssociationUseCase(association.id) } returns listOf(targetUser)
        val controller = UserController(checkPermissionUseCase, getUsersInAssociationUseCase, mockk(), mockk())
        assertEquals(listOf(targetUser), controller.getAll(association, user))
    }

    @Test
    fun testGetAllNoUser() = runBlocking {
        val controller = UserController(mockk(), mockk(), mockk(), mockk())
        val exception = assertThrows<ControllerException> {
            controller.getAll(association, null)
        }
        assertEquals(HttpStatusCode.Unauthorized, exception.code)
        assertEquals(LocalizedString.ERROR_USERS_VIEW_NOT_ALLOWED, exception.error)
    }

    @Test
    fun testGetAllUnauthorized() = runBlocking {
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        coEvery { checkPermissionUseCase(Pair(user, Permission.USERS_VIEW)) } returns false
        val controller = UserController(checkPermissionUseCase, mockk(), mockk(), mockk())
        val exception = assertThrows<ControllerException> {
            controller.getAll(association, user)
        }
        assertEquals(HttpStatusCode.Unauthorized, exception.code)
        assertEquals(LocalizedString.ERROR_USERS_VIEW_NOT_ALLOWED, exception.error)
    }

    @Test
    fun testGet() = runBlocking {
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        val getUserUseCase = mockk<IGetUserUseCase>()
        coEvery { checkPermissionUseCase(Pair(user, Permission.USERS_VIEW)) } returns true
        coEvery { getUserUseCase(targetUser.id) } returns targetUser
        val controller = UserController(checkPermissionUseCase, mockk(), getUserUseCase, mockk())
        assertEquals(targetUser, controller.get(association, user, targetUser.id))
    }

    @Test
    fun testGetNotFound() = runBlocking {
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        val getUserUseCase = mockk<IGetUserUseCase>()
        coEvery { checkPermissionUseCase(Pair(user, Permission.USERS_VIEW)) } returns true
        coEvery { getUserUseCase(targetUser.id) } returns null
        val controller = UserController(checkPermissionUseCase, mockk(), getUserUseCase, mockk())
        val exception = assertThrows<ControllerException> {
            controller.get(association, user, targetUser.id)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals(LocalizedString.ERROR_USERS_NOT_FOUND, exception.error)
    }

    @Test
    fun testGetNotInAssociation() = runBlocking {
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        val getUserUseCase = mockk<IGetUserUseCase>()
        coEvery { checkPermissionUseCase(Pair(user, Permission.USERS_VIEW)) } returns true
        coEvery { getUserUseCase(targetUser2.id) } returns targetUser2
        val controller = UserController(checkPermissionUseCase, mockk(), getUserUseCase, mockk())
        val exception = assertThrows<ControllerException> {
            controller.get(association, user, targetUser2.id)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals(LocalizedString.ERROR_USERS_NOT_FOUND, exception.error)
    }

    @Test
    fun testGetUnauthorized() = runBlocking {
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        coEvery { checkPermissionUseCase(Pair(user, Permission.USERS_VIEW)) } returns false
        val controller = UserController(checkPermissionUseCase, mockk(), mockk(), mockk())
        val exception = assertThrows<ControllerException> {
            controller.get(association, user, targetUser.id)
        }
        assertEquals(HttpStatusCode.Unauthorized, exception.code)
        assertEquals(LocalizedString.ERROR_USERS_VIEW_NOT_ALLOWED, exception.error)
    }

    @Test
    fun testGetUnauthorizedButMe() = runBlocking {
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        val getUserUseCase = mockk<IGetUserUseCase>()
        coEvery { checkPermissionUseCase(Pair(targetUser, Permission.USERS_VIEW)) } returns false
        coEvery { getUserUseCase(targetUser.id) } returns targetUser
        val controller = UserController(checkPermissionUseCase, mockk(), getUserUseCase, mockk())
        assertEquals(targetUser, controller.get(association, targetUser, targetUser.id))
    }

    @Test
    fun testCreate() = runBlocking {
        val controller = UserController(mockk(), mockk(), mockk(), mockk())
        val exception = assertThrows<ControllerException> {
            controller.create(association, user, Unit)
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
        val controller = UserController(mockk(), mockk(), mockk(), mockk())
        val exception = assertThrows<ControllerException> {
            controller.delete(association, user, targetUser.id)
        }
        assertEquals(HttpStatusCode.MethodNotAllowed, exception.code)
        assertEquals(LocalizedString.ERROR_USERS_DELETE_NOT_ALLOWED, exception.error)
    }

}