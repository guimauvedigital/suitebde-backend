package me.nathanfallet.suitebde.usecases.roles

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.extensions.invoke
import me.nathanfallet.suitebde.models.associations.Association
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
        val useCase = CheckPermissionUseCase()
        val user = User("id", "otherAssociationId", "email", "password", "firstName", "lastName", true)
        assertEquals(false, useCase(user, association, Permission.USERS_VIEW))
    }

    @Test
    fun invokeSuperuser() = runBlocking {
        val useCase = CheckPermissionUseCase()
        val user = User("id", "associationId", "email", "password", "firstName", "lastName", true)
        assertEquals(true, useCase(user, association, Permission.USERS_VIEW))
    }

    @Test
    fun invokeNone() = runBlocking {
        val useCase = CheckPermissionUseCase()
        val user = User("id", "associationId", "email", "password", "firstName", "lastName", false)
        assertEquals(false, useCase(user, association, Permission.USERS_VIEW))
    }

}