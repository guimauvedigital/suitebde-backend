package com.suitebde.usecases.web

import com.suitebde.models.associations.Association
import com.suitebde.models.roles.AdminPermission
import com.suitebde.models.roles.Permission
import com.suitebde.models.users.User
import com.suitebde.models.web.WebMenu
import com.suitebde.usecases.associations.IGetAssociationForCallUseCase
import dev.kaccelero.commons.exceptions.ControllerException
import dev.kaccelero.commons.localization.IGetLocaleForCallUseCase
import dev.kaccelero.commons.localization.ITranslateUseCase
import dev.kaccelero.commons.permissions.ICheckPermissionSuspendUseCase
import dev.kaccelero.commons.repositories.IGetModelSuspendUseCase
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetAdminMenuForCallUseCaseTest {

    private val association = Association(
        UUID(), "name", "school", "city",
        true, Clock.System.now(), Clock.System.now()
    )
    private val user = User(
        UUID(), association.id, "email", "password",
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
                    UUID(java.util.UUID.nameUUIDFromBytes("dashboard".toByteArray())),
                    association.id,
                    "t:admin_menu_dashboard",
                    "/admin"
                ),
                WebMenu(
                    UUID(java.util.UUID.nameUUIDFromBytes("files".toByteArray())),
                    association.id,
                    "t:admin_menu_files",
                    "/admin/files"
                ),
                WebMenu(
                    UUID(java.util.UUID.nameUUIDFromBytes("subscriptions".toByteArray())),
                    association.id,
                    "t:admin_menu_subscriptions",
                    "/admin/subscriptions"
                ),
                WebMenu(
                    UUID(java.util.UUID.nameUUIDFromBytes("users".toByteArray())),
                    association.id,
                    "t:admin_menu_users",
                    "/admin/users"
                ),
                WebMenu(
                    UUID(java.util.UUID.nameUUIDFromBytes("roles".toByteArray())),
                    association.id,
                    "t:admin_menu_roles",
                    "/admin/roles"
                ),
                WebMenu(
                    UUID(java.util.UUID.nameUUIDFromBytes("webpages".toByteArray())),
                    association.id,
                    "t:admin_menu_webpages",
                    "/admin/webpages"
                ),
                WebMenu(
                    UUID(java.util.UUID.nameUUIDFromBytes("webmenus".toByteArray())),
                    association.id,
                    "t:admin_menu_webmenus",
                    "/admin/webmenus"
                ),
                WebMenu(
                    UUID(java.util.UUID.nameUUIDFromBytes("events".toByteArray())),
                    association.id,
                    "t:admin_menu_events",
                    "/admin/events"
                ),
                WebMenu(
                    UUID(java.util.UUID.nameUUIDFromBytes("clubs".toByteArray())),
                    association.id,
                    "t:admin_menu_clubs",
                    "/admin/clubs"
                ),
                WebMenu(
                    UUID(java.util.UUID.nameUUIDFromBytes("notifications".toByteArray())),
                    association.id,
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
                    UUID(java.util.UUID.nameUUIDFromBytes("dashboard".toByteArray())),
                    association.id,
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
        val getAssociationByIdUseCase = mockk<IGetModelSuspendUseCase<Association, UUID>>()
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
        val getAssociationByIdUseCase = mockk<IGetModelSuspendUseCase<Association, UUID>>()
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
                    UUID(java.util.UUID.nameUUIDFromBytes("associations".toByteArray())),
                    AdminPermission.adminAssociationId,
                    "t:admin_menu_associations",
                    "/admin/associations"
                )
            ),
            useCase(call)
        )
    }

}
