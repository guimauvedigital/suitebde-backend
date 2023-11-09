package me.nathanfallet.suitebde.controllers.associations

import io.ktor.http.*
import io.ktor.server.application.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.ktor.routers.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.UpdateAssociationPayload
import me.nathanfallet.suitebde.models.roles.AdminPermission
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationsUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUserForCallUseCase
import me.nathanfallet.usecases.models.get.IGetModelSuspendUseCase
import me.nathanfallet.usecases.models.update.IUpdateModelSuspendUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class AssociationsControllerTest {

    private val association = Association(
        "associationId", "name", "school", "city",
        true, Clock.System.now(), Clock.System.now()
    )
    private val user = User(
        "id", "associationId", "email", null,
        "firstname", "lastname", false
    )

    @Test
    fun testGetAll() = runBlocking {
        val getAssociationsUseCase = mockk<IGetAssociationsUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        coEvery { getAssociationsUseCase(true) } returns listOf(association)
        coEvery { getUserForCallUseCase(any()) } returns null
        val controller = AssociationsController(
            getAssociationsUseCase, getUserForCallUseCase, mockk(), mockk(), mockk()
        )
        assertEquals(listOf(association), controller.getAll(mockk()))
    }

    @Test
    fun testGetAllAsAdmin() = runBlocking {
        val getAssociationsUseCase = mockk<IGetAssociationsUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        coEvery { getAssociationsUseCase(false) } returns listOf(association)
        coEvery { getUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, AdminPermission) } returns true
        val controller = AssociationsController(
            getAssociationsUseCase, getUserForCallUseCase, checkPermissionUseCase, mockk(), mockk()
        )
        assertEquals(listOf(association), controller.getAll(mockk()))
    }

    @Test
    fun testGet() = runBlocking {
        val getAssociationUseCase = mockk<IGetModelSuspendUseCase<Association, String>>()
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationUseCase(association.id) } returns association
        val controller = AssociationsController(
            mockk(),
            mockk(),
            mockk(),
            getAssociationUseCase,
            mockk()
        )
        assertEquals(association, controller.get(call, association.id))
    }

    @Test
    fun testGetNotFound() = runBlocking {
        val getAssociationUseCase = mockk<IGetModelSuspendUseCase<Association, String>>()
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationUseCase(association.id) } returns null
        val controller = AssociationsController(
            mockk(),
            mockk(),
            mockk(),
            getAssociationUseCase,
            mockk()
        )
        val exception = assertThrows<ControllerException> {
            controller.get(call, association.id)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("associations_not_found", exception.key)
    }

    @Test
    fun testUpdate() = runBlocking {
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getAssociationUseCase = mockk<IGetModelSuspendUseCase<Association, String>>()
        val updateAssociationUseCase =
            mockk<IUpdateModelSuspendUseCase<Association, String, UpdateAssociationPayload>>()
        val call = mockk<ApplicationCall>()
        val payload = UpdateAssociationPayload(
            "new name", "new school", "new city", false
        )
        val updatedAssociation = association.copy(
            name = "new name",
            school = "new school",
            city = "new city",
            validated = false
        )
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, AdminPermission) } returns true
        coEvery { getAssociationUseCase(association.id) } returns association
        coEvery { updateAssociationUseCase(association.id, payload) } returns updatedAssociation
        val controller = AssociationsController(
            mockk(),
            getUserForCallUseCase,
            checkPermissionUseCase,
            getAssociationUseCase,
            updateAssociationUseCase
        )
        assertEquals(updatedAssociation, controller.update(call, association.id, payload))
    }

    @Test
    fun testUpdateNotFound() = runBlocking {
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getAssociationUseCase = mockk<IGetModelSuspendUseCase<Association, String>>()
        val call = mockk<ApplicationCall>()
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, AdminPermission) } returns true
        coEvery { getAssociationUseCase(association.id) } returns null
        val controller = AssociationsController(
            mockk(),
            getUserForCallUseCase,
            checkPermissionUseCase,
            getAssociationUseCase,
            mockk()
        )
        val exception = assertThrows<ControllerException> {
            controller.update(
                call, association.id, UpdateAssociationPayload(
                    "new name", "new school", "new city", false
                )
            )
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("associations_not_found", exception.key)
    }

    @Test
    fun testUpdateForbidden() = runBlocking {
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, AdminPermission) } returns false
        val controller = AssociationsController(
            mockk(),
            getUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk()
        )
        val exception = assertThrows<ControllerException> {
            controller.update(
                call, association.id, UpdateAssociationPayload(
                    "new name", "new school", "new city", false
                )
            )
        }
        assertEquals(HttpStatusCode.Forbidden, exception.code)
        assertEquals("associations_update_not_allowed", exception.key)
    }

    @Test
    fun testUpdateNoUser() = runBlocking {
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { getUserForCallUseCase(call) } returns null
        val controller = AssociationsController(
            mockk(),
            getUserForCallUseCase,
            mockk(),
            mockk(),
            mockk()
        )
        val exception = assertThrows<ControllerException> {
            controller.update(
                call, association.id, UpdateAssociationPayload(
                    "new name", "new school", "new city", false
                )
            )
        }
        assertEquals(HttpStatusCode.Unauthorized, exception.code)
        assertEquals("auth_invalid_credentials", exception.key)
    }

    @Test
    fun testUpdateInternalError() = runBlocking {
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getAssociationUseCase = mockk<IGetModelSuspendUseCase<Association, String>>()
        val updateAssociationUseCase =
            mockk<IUpdateModelSuspendUseCase<Association, String, UpdateAssociationPayload>>()
        val call = mockk<ApplicationCall>()
        val payload = UpdateAssociationPayload(
            "new name", "new school", "new city", false
        )
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, AdminPermission) } returns true
        coEvery { getAssociationUseCase(association.id) } returns association
        coEvery { updateAssociationUseCase(association.id, payload) } returns null
        val controller = AssociationsController(
            mockk(),
            getUserForCallUseCase,
            checkPermissionUseCase,
            getAssociationUseCase,
            updateAssociationUseCase
        )
        val exception = assertThrows<ControllerException> {
            controller.update(call, association.id, payload)
        }
        assertEquals(HttpStatusCode.InternalServerError, exception.code)
        assertEquals("error_internal", exception.key)
    }

}
