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
import me.nathanfallet.suitebde.usecases.users.IGetUsersInAssociationUseCase
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class UserControllerTest {

    private val association = Association("id", "name")
    private val user = User("id", "username", "email", "password", "firstname", "lastname", false)

    @Test
    fun testGetAll() = runBlocking {
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        val getUsersInAssociationUseCase = mockk<IGetUsersInAssociationUseCase>()
        coEvery { checkPermissionUseCase(Pair(user, Permission.USERS_VIEW)) } returns true
        coEvery { getUsersInAssociationUseCase(association.id) } returns listOf(user)
        val controller = UserController(checkPermissionUseCase, getUsersInAssociationUseCase, mockk(), mockk())
        assertEquals(listOf(user), controller.getAll(association, user))
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

}