package me.nathanfallet.suitebde.usecases.roles

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.roles.AdminPermission
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.User
import kotlin.test.Test
import kotlin.test.assertEquals

class CheckPermissionUseCaseTest {

    private val association = Association(
        "associationId", "name", "school", "city",
        true, Clock.System.now(), Clock.System.now()
    )

    @Test
    fun invokeNotInAssociation() = runBlocking {
        val useCase = CheckPermissionUseCase(mockk())
        val user = User("id", "otherAssociationId", "email", "password", "firstName", "lastName", true)
        assertEquals(false, useCase(user, Permission.USERS_VIEW inAssociation association.id))
    }

    @Test
    fun invokeSuperAdmin() = runBlocking {
        val useCase = CheckPermissionUseCase(mockk())
        val user = User("id", "admin", "email", "password", "firstName", "lastName", false)
        assertEquals(true, useCase(user, Permission.USERS_VIEW inAssociation association.id))
    }

    @Test
    fun invokeSuperuser() = runBlocking {
        val useCase = CheckPermissionUseCase(mockk())
        val user = User("id", "associationId", "email", "password", "firstName", "lastName", true)
        assertEquals(true, useCase(user, Permission.USERS_VIEW inAssociation association.id))
    }

    @Test
    fun invokeNone() = runBlocking {
        val getPermissionsForUserUseCase = mockk<IGetPermissionsForUserUseCase>()
        val useCase = CheckPermissionUseCase(getPermissionsForUserUseCase)
        val user = User("id", "associationId", "email", "password", "firstName", "lastName", false)
        coEvery { getPermissionsForUserUseCase(user) } returns setOf()
        assertEquals(false, useCase(user, Permission.USERS_VIEW inAssociation association.id))
    }

    @Test
    fun invokeFromUserPermission() = runBlocking {
        val getPermissionsForUserUseCase = mockk<IGetPermissionsForUserUseCase>()
        val useCase = CheckPermissionUseCase(getPermissionsForUserUseCase)
        val user = User("id", "associationId", "email", "password", "firstName", "lastName", false)
        coEvery { getPermissionsForUserUseCase(user) } returns setOf(Permission.USERS_VIEW)
        assertEquals(true, useCase(user, Permission.USERS_VIEW inAssociation association.id))
    }

    @Test
    fun invokeAdmin() = runBlocking {
        val useCase = CheckPermissionUseCase(mockk())
        val user = User("id", "admin", "email", "password", "firstName", "lastName", false)
        assertEquals(true, useCase(user, AdminPermission))
    }

    @Test
    fun invokeAdminNotInAssociation() = runBlocking {
        val useCase = CheckPermissionUseCase(mockk())
        val user = User("id", "associationId", "email", "password", "firstName", "lastName", false)
        assertEquals(false, useCase(user, AdminPermission))
    }

}
