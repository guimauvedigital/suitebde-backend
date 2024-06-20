package com.suitebde.controllers.associations

import com.suitebde.models.associations.Association
import com.suitebde.models.associations.UpdateAssociationPayload
import com.suitebde.models.roles.AdminPermission
import com.suitebde.models.users.User
import com.suitebde.usecases.associations.IGetAssociationsUseCase
import dev.kaccelero.commons.exceptions.ControllerException
import dev.kaccelero.commons.permissions.ICheckPermissionSuspendUseCase
import dev.kaccelero.commons.repositories.IGetModelSuspendUseCase
import dev.kaccelero.commons.repositories.IUpdateModelSuspendUseCase
import dev.kaccelero.commons.users.IGetUserForCallUseCase
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AssociationsControllerTest {

    private val association = Association(
        UUID(), "name", "school", "city",
        true, Clock.System.now(), Clock.System.now()
    )
    private val user = User(
        UUID(), UUID(), "email", null,
        "firstname", "lastname", false, Clock.System.now()
    )

    @Test
    fun testGetAll() = runBlocking {
        val getAssociationsUseCase = mockk<IGetAssociationsUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        coEvery { getAssociationsUseCase(true) } returns listOf(association)
        coEvery { getUserForCallUseCase(any()) } returns null
        val controller = AssociationsController(
            getAssociationsUseCase, getUserForCallUseCase, mockk(), mockk(), mockk(), mockk()
        )
        assertEquals(listOf(association), controller.list(mockk()))
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
            getAssociationsUseCase, getUserForCallUseCase, mockk(), checkPermissionUseCase, mockk(), mockk()
        )
        assertEquals(listOf(association), controller.list(mockk()))
    }

    @Test
    fun testGet() = runBlocking {
        val getAssociationUseCase = mockk<IGetModelSuspendUseCase<Association, UUID>>()
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationUseCase(association.id) } returns association
        val controller = AssociationsController(
            mockk(),
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
        val getAssociationUseCase = mockk<IGetModelSuspendUseCase<Association, UUID>>()
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationUseCase(association.id) } returns null
        val controller = AssociationsController(
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            getAssociationUseCase,
            mockk()
        )
        val exception = assertFailsWith(ControllerException::class) {
            controller.get(call, association.id)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("associations_not_found", exception.key)
    }

    @Test
    fun testUpdate() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getAssociationUseCase = mockk<IGetModelSuspendUseCase<Association, UUID>>()
        val updateAssociationUseCase =
            mockk<IUpdateModelSuspendUseCase<Association, UUID, UpdateAssociationPayload>>()
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
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, AdminPermission) } returns true
        coEvery { getAssociationUseCase(association.id) } returns association
        coEvery { updateAssociationUseCase(association.id, payload) } returns updatedAssociation
        val controller = AssociationsController(
            mockk(),
            mockk(),
            requireUserForCallUseCase,
            checkPermissionUseCase,
            getAssociationUseCase,
            updateAssociationUseCase
        )
        assertEquals(updatedAssociation, controller.update(call, association.id, payload))
    }

    @Test
    fun testUpdateNotFound() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getAssociationUseCase = mockk<IGetModelSuspendUseCase<Association, UUID>>()
        val call = mockk<ApplicationCall>()
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, AdminPermission) } returns true
        coEvery { getAssociationUseCase(association.id) } returns null
        val controller = AssociationsController(
            mockk(),
            mockk(),
            requireUserForCallUseCase,
            checkPermissionUseCase,
            getAssociationUseCase,
            mockk()
        )
        val exception = assertFailsWith(ControllerException::class) {
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
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, AdminPermission) } returns false
        val controller = AssociationsController(
            mockk(),
            mockk(),
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk()
        )
        val exception = assertFailsWith(ControllerException::class) {
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
    fun testUpdateInternalError() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getAssociationUseCase = mockk<IGetModelSuspendUseCase<Association, UUID>>()
        val updateAssociationUseCase =
            mockk<IUpdateModelSuspendUseCase<Association, UUID, UpdateAssociationPayload>>()
        val call = mockk<ApplicationCall>()
        val payload = UpdateAssociationPayload(
            "new name", "new school", "new city", false
        )
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, AdminPermission) } returns true
        coEvery { getAssociationUseCase(association.id) } returns association
        coEvery { updateAssociationUseCase(association.id, payload) } returns null
        val controller = AssociationsController(
            mockk(),
            mockk(),
            requireUserForCallUseCase,
            checkPermissionUseCase,
            getAssociationUseCase,
            updateAssociationUseCase
        )
        val exception = assertFailsWith(ControllerException::class) {
            controller.update(call, association.id, payload)
        }
        assertEquals(HttpStatusCode.InternalServerError, exception.code)
        assertEquals("error_internal", exception.key)
    }

}
