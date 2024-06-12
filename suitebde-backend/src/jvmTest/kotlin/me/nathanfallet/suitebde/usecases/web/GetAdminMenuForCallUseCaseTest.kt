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
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.roles.AdminPermission
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForCallUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase
import me.nathanfallet.usecases.models.get.IGetModelSuspendUseCase
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
        "firstname", "lastname", false, Clock.System.now()
    )

    @Test
    fun invoke() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val call = mockk<ApplicationCall>()
        val useCase = GetAdminMenuForCallUseCase(
            requireUserForCallUseCase, getAssociationForCallUseCase, mockk(),
            checkPermissionUseCase, getLocaleForCallUseCase, translateUseCase
        )
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { checkPermissionUseCase(user, Permission.ADMIN inAssociation association.id) } returns true
        coEvery { checkPermissionUseCase(user, Permission.FILES_VIEW inAssociation association.id) } returns true
        coEvery {
            checkPermissionUseCase(user, Permission.SUBSCRIPTIONS_VIEW inAssociation association.id)
        } returns true
        coEvery { checkPermissionUseCase(user, Permission.USERS_VIEW inAssociation association.id) } returns true
        coEvery { checkPermissionUseCase(user, Permission.ROLES_VIEW inAssociation association.id) } returns true
        coEvery { checkPermissionUseCase(user, Permission.WEBPAGES_VIEW inAssociation association.id) } returns true
        coEvery { checkPermissionUseCase(user, Permission.WEBMENUS_VIEW inAssociation association.id) } returns true
        coEvery { checkPermissionUseCase(user, Permission.EVENTS_VIEW inAssociation association.id) } returns true
        coEvery { checkPermissionUseCase(user, Permission.CLUBS_VIEW inAssociation association.id) } returns true
        coEvery {
            checkPermissionUseCase(
                user,
                Permission.NOTIFICATIONS_SEND inAssociation association.id
            )
        } returns true
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
                    "files",
                    "associationId",
                    "t:admin_menu_files",
                    "/admin/files"
                ),
                WebMenu(
                    "subscriptions",
                    "associationId",
                    "t:admin_menu_subscriptions",
                    "/admin/subscriptions"
                ),
                WebMenu(
                    "users",
                    "associationId",
                    "t:admin_menu_users",
                    "/admin/users"
                ),
                WebMenu(
                    "roles",
                    "associationId",
                    "t:admin_menu_roles",
                    "/admin/roles"
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
                ),
                WebMenu(
                    "events",
                    "associationId",
                    "t:admin_menu_events",
                    "/admin/events"
                ),
                WebMenu(
                    "clubs",
                    "associationId",
                    "t:admin_menu_clubs",
                    "/admin/clubs"
                ),
                WebMenu(
                    "notifications",
                    "associationId",
                    "t:admin_menu_notifications",
                    "/admin/notifications"
                )
            ),
            useCase(call)
        )
    }

    @Test
    fun invokeOnlyDashboard() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val call = mockk<ApplicationCall>()
        val useCase = GetAdminMenuForCallUseCase(
            requireUserForCallUseCase, getAssociationForCallUseCase, mockk(),
            checkPermissionUseCase, getLocaleForCallUseCase, translateUseCase
        )
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { checkPermissionUseCase(user, Permission.ADMIN inAssociation association.id) } returns true
        coEvery { checkPermissionUseCase(user, Permission.FILES_VIEW inAssociation association.id) } returns false
        coEvery {
            checkPermissionUseCase(user, Permission.SUBSCRIPTIONS_VIEW inAssociation association.id)
        } returns false
        coEvery { checkPermissionUseCase(user, Permission.USERS_VIEW inAssociation association.id) } returns false
        coEvery { checkPermissionUseCase(user, Permission.ROLES_VIEW inAssociation association.id) } returns false
        coEvery { checkPermissionUseCase(user, Permission.WEBPAGES_VIEW inAssociation association.id) } returns false
        coEvery { checkPermissionUseCase(user, Permission.WEBMENUS_VIEW inAssociation association.id) } returns false
        coEvery { checkPermissionUseCase(user, Permission.EVENTS_VIEW inAssociation association.id) } returns false
        coEvery { checkPermissionUseCase(user, Permission.CLUBS_VIEW inAssociation association.id) } returns false
        coEvery {
            checkPermissionUseCase(
                user,
                Permission.NOTIFICATIONS_SEND inAssociation association.id
            )
        } returns false
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
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val call = mockk<ApplicationCall>()
        val useCase = GetAdminMenuForCallUseCase(
            requireUserForCallUseCase, getAssociationForCallUseCase, mockk(),
            checkPermissionUseCase, mockk(), mockk()
        )
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { checkPermissionUseCase(user, Permission.ADMIN inAssociation association.id) } returns false
        val exception = assertFailsWith(ControllerException::class) {
            useCase(call)
        }
        assertEquals(HttpStatusCode.Forbidden, exception.code)
        assertEquals("admin_not_allowed", exception.key)
    }

    @Test
    fun invokeNoAssociationNeitherAdmin() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getAssociationByIdUseCase = mockk<IGetModelSuspendUseCase<Association, String>>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val call = mockk<ApplicationCall>()
        val useCase = GetAdminMenuForCallUseCase(
            requireUserForCallUseCase, getAssociationForCallUseCase, getAssociationByIdUseCase,
            checkPermissionUseCase, mockk(), mockk()
        )
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { getAssociationForCallUseCase(call) } returns null
        coEvery { getAssociationByIdUseCase(user.associationId) } returns null
        coEvery { checkPermissionUseCase(user, AdminPermission) } returns false
        val exception = assertFailsWith(ControllerException::class) {
            useCase(call)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("associations_not_found", exception.key)
    }

    @Test
    fun invokeNoAssociationButAdmin() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getAssociationByIdUseCase = mockk<IGetModelSuspendUseCase<Association, String>>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val call = mockk<ApplicationCall>()
        val useCase = GetAdminMenuForCallUseCase(
            requireUserForCallUseCase, getAssociationForCallUseCase, getAssociationByIdUseCase,
            checkPermissionUseCase, getLocaleForCallUseCase, translateUseCase
        )
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { getAssociationForCallUseCase(call) } returns null
        coEvery { getAssociationByIdUseCase(user.associationId) } returns null
        coEvery { checkPermissionUseCase(user, AdminPermission) } returns true
        every { getLocaleForCallUseCase(call) } returns Locale.ENGLISH
        every { translateUseCase(Locale.ENGLISH, any()) } answers { "t:${secondArg<String>()}" }
        assertEquals(
            listOf(
                WebMenu(
                    "associations",
                    "admin",
                    "t:admin_menu_associations",
                    "/admin/associations"
                )
            ),
            useCase(call)
        )
    }

}
