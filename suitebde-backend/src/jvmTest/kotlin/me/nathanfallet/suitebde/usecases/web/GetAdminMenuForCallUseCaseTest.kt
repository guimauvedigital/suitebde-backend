package me.nathanfallet.suitebde.usecases.web

import io.ktor.http.*
import io.ktor.server.application.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.ktorx.usecases.users.IGetUserForCallUseCase
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForCallUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

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
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val call = mockk<ApplicationCall>()
        val useCase = GetAdminMenuForCallUseCase(
            getAssociationForCallUseCase, getUserForCallUseCase,
            checkPermissionUseCase, getLocaleForCallUseCase, translateUseCase
        )
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.ADMIN inAssociation association) } returns true
        coEvery { checkPermissionUseCase(user, Permission.USERS_VIEW inAssociation association) } returns true
        coEvery { checkPermissionUseCase(user, Permission.WEBPAGES_VIEW inAssociation association) } returns true
        coEvery { checkPermissionUseCase(user, Permission.WEBMENUS_VIEW inAssociation association) } returns true
        every { getLocaleForCallUseCase(call) } returns Locale.ENGLISH
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
                ),
                WebMenu(
                    "webpages",
                    "associationId",
                    "t:admin_menu_webpages",
                    "/admin/webpages"
                ),
                WebMenu(
                    "webmenus",
                    "associationId",
                    "t:admin_menu_webmenus",
                    "/admin/webmenus"
                )
            ),
            useCase(call)
        )
    }

    @Test
    fun invokeOnlyDashboard() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val call = mockk<ApplicationCall>()
        val useCase = GetAdminMenuForCallUseCase(
            getAssociationForCallUseCase, getUserForCallUseCase,
            checkPermissionUseCase, getLocaleForCallUseCase, translateUseCase
        )
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.ADMIN inAssociation association) } returns true
        coEvery { checkPermissionUseCase(user, Permission.USERS_VIEW inAssociation association) } returns false
        coEvery { checkPermissionUseCase(user, Permission.WEBPAGES_VIEW inAssociation association) } returns false
        coEvery { checkPermissionUseCase(user, Permission.WEBMENUS_VIEW inAssociation association) } returns false
        every { getLocaleForCallUseCase(call) } returns Locale.ENGLISH
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
            useCase(call)
        )
    }

    @Test
    fun invokeNotAdmin() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val call = mockk<ApplicationCall>()
        val useCase = GetAdminMenuForCallUseCase(
            getAssociationForCallUseCase, getUserForCallUseCase,
            checkPermissionUseCase, mockk(), mockk()
        )
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.ADMIN inAssociation association) } returns false
        val exception = assertFailsWith(ControllerException::class) {
            useCase(call)
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
            mockk(), mockk(), mockk()
        )
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getUserForCallUseCase(call) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            useCase(call)
        }
        assertEquals(HttpStatusCode.Unauthorized, exception.code)
        assertEquals("auth_invalid_credentials", exception.key)
    }

    @Test
    fun invokeNoAssociation() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val call = mockk<ApplicationCall>()
        val useCase = GetAdminMenuForCallUseCase(
            getAssociationForCallUseCase, mockk(), mockk(), mockk(), mockk()
        )
        coEvery { getAssociationForCallUseCase(call) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            useCase(call)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("associations_not_found", exception.key)
    }

}
