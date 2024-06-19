package com.suitebde.usecases.roles

import com.suitebde.models.associations.Association
import com.suitebde.models.roles.AdminPermission
import com.suitebde.models.roles.Permission
import com.suitebde.models.users.User
import dev.kaccelero.models.UUID
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals

class CheckPermissionUseCaseTest {

    private val adminAssociation = UUID("00000000-0000-0000-0000-000000000000")
    private val association = Association(
        UUID(), "name", "school", "city",
        true, Clock.System.now(), Clock.System.now()
    )

    @Test
    fun invokeNotInAssociation() = runBlocking {
        val useCase = CheckPermissionUseCase(mockk())
        val user = User(UUID(), UUID(), "email", "password", "firstName", "lastName", true, Clock.System.now())
        assertEquals(false, useCase(user, Permission.USERS_VIEW inAssociation association.id))
    }

    @Test
    fun invokeSuperAdmin() = runBlocking {
        val useCase = CheckPermissionUseCase(mockk())
        val user =
            User(UUID(), adminAssociation, "email", "password", "firstName", "lastName", false, Clock.System.now())
        assertEquals(true, useCase(user, Permission.USERS_VIEW inAssociation association.id))
    }

    @Test
    fun invokeSuperuser() = runBlocking {
        val useCase = CheckPermissionUseCase(mockk())
        val user = User(UUID(), association.id, "email", "password", "firstName", "lastName", true, Clock.System.now())
        assertEquals(true, useCase(user, Permission.USERS_VIEW inAssociation association.id))
    }

    @Test
    fun invokeNone() = runBlocking {
        val getPermissionsForUserUseCase = mockk<IGetPermissionsForUserUseCase>()
        val useCase = CheckPermissionUseCase(getPermissionsForUserUseCase)
        val user =
            User(UUID(), association.id, "email", "password", "firstName", "lastName", false, Clock.System.now())
        coEvery { getPermissionsForUserUseCase(user) } returns setOf()
        assertEquals(false, useCase(user, Permission.USERS_VIEW inAssociation association.id))
    }

    @Test
    fun invokeFromUserPermission() = runBlocking {
        val getPermissionsForUserUseCase = mockk<IGetPermissionsForUserUseCase>()
        val useCase = CheckPermissionUseCase(getPermissionsForUserUseCase)
        val user =
            User(UUID(), association.id, "email", "password", "firstName", "lastName", false, Clock.System.now())
        coEvery { getPermissionsForUserUseCase(user) } returns setOf(Permission.USERS_VIEW)
        assertEquals(true, useCase(user, Permission.USERS_VIEW inAssociation association.id))
    }

    @Test
    fun invokeAdmin() = runBlocking {
        val useCase = CheckPermissionUseCase(mockk())
        val user =
            User(UUID(), adminAssociation, "email", "password", "firstName", "lastName", false, Clock.System.now())
        assertEquals(true, useCase(user, AdminPermission))
    }

    @Test
    fun invokeAdminNotInAssociation() = runBlocking {
        val useCase = CheckPermissionUseCase(mockk())
        val user =
            User(UUID(), association.id, "email", "password", "firstName", "lastName", false, Clock.System.now())
        assertEquals(false, useCase(user, AdminPermission))
    }

}
