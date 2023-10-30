package me.nathanfallet.suitebde.usecases.web

import io.ktor.http.*
import io.ktor.server.application.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.extensions.invoke
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.suitebde.usecases.application.ITranslateUseCase
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForCallUseCase
import me.nathanfallet.suitebde.usecases.roles.ICheckPermissionUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUserForCallUseCase
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAdminMenuForCallUseCaseTest {

    private val association = Association(
        "associationId", "name", "school", "city",
        true, Clock.System.now(), Clock.System.now()
    )
    private val user = User(
        "id", "associationId", "email", "password",
        "firstname", "lastname", false
    )

    @Test
    fun invoke() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val call = mockk<ApplicationCall>()
        val useCase = GetAdminMenuForCallUseCase(
            getAssociationForCallUseCase, getUserForCallUseCase,
            checkPermissionUseCase, translateUseCase
        )
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, association, Permission.ADMIN) } returns true
        coEvery { checkPermissionUseCase(user, association, Permission.USERS_VIEW) } returns true
        every { translateUseCase(Locale.ENGLISH, any()) } answers { "t:${secondArg<String>()}" }
        assertEquals(
            listOf(
                WebMenu(
                    "dashboard",
                    "associationId",
                    "t:admin_menu_dashboard",
                    "/admin"
                ),
                WebMenu(
                    "users",
                    "associationId",
                    "t:admin_menu_users",
                    "/admin/users"
                )
            ),
            useCase(call, Locale.ENGLISH)
        )
    }

    @Test
    fun invokeOnlyDashboard() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val call = mockk<ApplicationCall>()
        val useCase = GetAdminMenuForCallUseCase(
            getAssociationForCallUseCase, getUserForCallUseCase,
            checkPermissionUseCase, translateUseCase
        )
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, association, Permission.ADMIN) } returns true
        coEvery { checkPermissionUseCase(user, association, Permission.USERS_VIEW) } returns false
        every { translateUseCase(Locale.ENGLISH, any()) } answers { "t:${secondArg<String>()}" }
        assertEquals(
            listOf(
                WebMenu(
                    "dashboard",
                    "associationId",
                    "t:admin_menu_dashboard",
                    "/admin"
                )
            ),
            useCase(call, Locale.ENGLISH)
        )
    }

    @Test
    fun invokeNotAdmin() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionUseCase>()
        val call = mockk<ApplicationCall>()
        val useCase = GetAdminMenuForCallUseCase(
            getAssociationForCallUseCase, getUserForCallUseCase,
            checkPermissionUseCase, mockk()
        )
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, association, Permission.ADMIN) } returns false
        val exception = assertThrows<ControllerException> {
            useCase(call, Locale.ENGLISH)
        }
        assertEquals(HttpStatusCode.Forbidden, exception.code)
        assertEquals("admin_not_allowed", exception.key)
    }

    @Test
    fun invokeNoUser() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val call = mockk<ApplicationCall>()
        val useCase = GetAdminMenuForCallUseCase(
            getAssociationForCallUseCase, getUserForCallUseCase,
            mockk(), mockk()
        )
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns null
        val exception = assertThrows<ControllerException> {
            useCase(call, Locale.ENGLISH)
        }
        assertEquals(HttpStatusCode.Unauthorized, exception.code)
        assertEquals("auth_invalid_credentials", exception.key)
    }

    @Test
    fun invokeNoAssociation() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val call = mockk<ApplicationCall>()
        val useCase = GetAdminMenuForCallUseCase(
            getAssociationForCallUseCase, mockk(), mockk(), mockk()
        )
        coEvery { getAssociationForCallUseCase(call) } returns null
        val exception = assertThrows<ControllerException> {
            useCase(call, Locale.ENGLISH)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("associations_not_found", exception.key)
    }

}