package me.nathanfallet.suitebde.usecases.roles

import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.User
import kotlin.test.Test
import kotlin.test.assertEquals

class CheckPermissionUseCaseTest {

    @Test
    fun invokeSuperuser() = runBlocking {
        val useCase = CheckPermissionUseCase()
        val user = User("id", "associationId", "email", "password", "firstName", "lastName", true)
        assertEquals(true, useCase(Pair(user, Permission.USERS_VIEW)))
    }

    @Test
    fun invokeNone() = runBlocking {
        val useCase = CheckPermissionUseCase()
        val user = User("id", "associationId", "email", "password", "firstName", "lastName", false)
        assertEquals(false, useCase(Pair(user, Permission.USERS_VIEW)))
    }

}