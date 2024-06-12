package me.nathanfallet.suitebde.controllers.clubs

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.users.IGetUserForCallUseCase
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.clubs.ClubContext
import me.nathanfallet.suitebde.models.clubs.CreateClubPayload
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.OptionalUserContext
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.models.create.context.ICreateChildModelWithContextSuspendUseCase
import me.nathanfallet.usecases.models.get.context.IGetChildModelWithContextSuspendUseCase
import me.nathanfallet.usecases.models.list.context.IListChildModelWithContextSuspendUseCase
import me.nathanfallet.usecases.models.list.slice.context.IListSliceChildModelWithContextSuspendUseCase
import me.nathanfallet.usecases.pagination.Pagination
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ClubsControllerTest {

    private val association = Association(
        "associationId", "name", "school", "city",
        true, Clock.System.now(), Clock.System.now()
    )
    private val user = User(
        "id", "associationId", "email", null,
        "firstname", "lastname", false, Clock.System.now()
    )
    private val club = Club(
        "roleId", "associationId", "name", "description",
        "image", Clock.System.now(), true, 1, true
    )

    @Test
    fun testList() = runBlocking {
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val getClubsInAssociationUseCase = mockk<IListSliceChildModelWithContextSuspendUseCase<Club, String>>()
        val call = mockk<ApplicationCall>()
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery {
            getClubsInAssociationUseCase(Pagination(10, 5), association.id, OptionalUserContext(user.id))
        } returns listOf(club)
        val controller = ClubsController(
            getUserForCallUseCase, mockk(), mockk(), mockk(), getClubsInAssociationUseCase,
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk()
        )
        every { call.request.path() } returns "/api/v1/associations/id/clubs"
        assertEquals(listOf(club), controller.list(call, association, 10, 5, null))
    }

    @Test
    fun testListNoUser() = runBlocking {
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val getClubsInAssociationUseCase = mockk<IListSliceChildModelWithContextSuspendUseCase<Club, String>>()
        val call = mockk<ApplicationCall>()
        coEvery { getUserForCallUseCase(call) } returns null
        coEvery {
            getClubsInAssociationUseCase(Pagination(10, 5), association.id, OptionalUserContext())
        } returns listOf(club)
        val controller = ClubsController(
            getUserForCallUseCase, mockk(), mockk(), mockk(), getClubsInAssociationUseCase,
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk()
        )
        every { call.request.path() } returns "/api/v1/associations/id/clubs"
        assertEquals(listOf(club), controller.list(call, association, 10, 5, null))
    }

    @Test
    fun testListDefaultLimitOffset() = runBlocking {
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val getClubsInAssociationUseCase = mockk<IListSliceChildModelWithContextSuspendUseCase<Club, String>>()
        val call = mockk<ApplicationCall>()
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery {
            getClubsInAssociationUseCase(Pagination(25, 0), association.id, OptionalUserContext(user.id))
        } returns listOf(club)
        val controller = ClubsController(
            getUserForCallUseCase, mockk(), mockk(), mockk(), getClubsInAssociationUseCase,
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk()
        )
        every { call.request.path() } returns "/api/v1/associations/id/clubs"
        assertEquals(listOf(club), controller.list(call, association, null, null, null))
    }

    @Test
    fun testListAdmin() = runBlocking {
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val getClubsInAssociationUseCase = mockk<IListChildModelWithContextSuspendUseCase<Club, String>>()
        val call = mockk<ApplicationCall>()
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery {
            getClubsInAssociationUseCase(association.id, ClubContext(user.id, false))
        } returns listOf(club)
        val controller = ClubsController(
            getUserForCallUseCase, mockk(), mockk(), getClubsInAssociationUseCase,
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk()
        )
        every { call.request.path() } returns "/admin/clubs"
        assertEquals(listOf(club), controller.list(call, association, null, null, null))
    }

    @Test
    fun testGet() = runBlocking {
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val getClubUseCase = mockk<IGetChildModelWithContextSuspendUseCase<Club, String, String>>()
        val controller = ClubsController(
            getUserForCallUseCase, mockk(), mockk(), mockk(), mockk(),
            getClubUseCase, mockk(), mockk(), mockk(), mockk(), mockk(), mockk()
        )
        val call = mockk<ApplicationCall>()
        coEvery { getUserForCallUseCase(call) } returns user
        coEvery { getClubUseCase(club.id, club.associationId, OptionalUserContext(user.id)) } returns club
        assertEquals(club, controller.get(call, association, club.id))
    }

    @Test
    fun testGetNoUser() = runBlocking {
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val getClubUseCase = mockk<IGetChildModelWithContextSuspendUseCase<Club, String, String>>()
        val controller = ClubsController(
            getUserForCallUseCase, mockk(), mockk(), mockk(), mockk(),
            getClubUseCase, mockk(), mockk(), mockk(), mockk(), mockk(), mockk()
        )
        val call = mockk<ApplicationCall>()
        coEvery { getUserForCallUseCase(call) } returns null
        coEvery { getClubUseCase(club.id, club.associationId, OptionalUserContext()) } returns club
        assertEquals(club, controller.get(call, association, club.id))
    }

    @Test
    fun testGetNotFound() = runBlocking {
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val getClubUseCase = mockk<IGetChildModelWithContextSuspendUseCase<Club, String, String>>()
        val controller = ClubsController(
            getUserForCallUseCase, mockk(), mockk(), mockk(), mockk(),
            getClubUseCase, mockk(), mockk(), mockk(), mockk(), mockk(), mockk()
        )
        val call = mockk<ApplicationCall>()
        coEvery { getUserForCallUseCase(call) } returns null
        coEvery { getClubUseCase(club.id, club.associationId, OptionalUserContext()) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.get(call, association, club.id)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("clubs_not_found", exception.key)
    }

    @Test
    fun testCreate() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val createClubUseCase = mockk<ICreateChildModelWithContextSuspendUseCase<Club, CreateClubPayload, String>>()
        val controller = ClubsController(
            mockk(), requireUserForCallUseCase, mockk(), mockk(), mockk(), mockk(),
            createClubUseCase, mockk(), mockk(), mockk(), mockk(), mockk()
        )
        val payload = CreateClubPayload("name", "description", "image", "member", "admin")
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { createClubUseCase(payload, club.associationId, OptionalUserContext(user.id)) } returns club
        assertEquals(club, controller.create(mockk(), association, payload))
    }

    @Test
    fun testCreateForbiddenValidated() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val controller = ClubsController(
            mockk(), requireUserForCallUseCase, checkPermissionUseCase, mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk()
        )
        val payload = CreateClubPayload("name", "description", "image", "member", "admin", true)
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { checkPermissionUseCase(user, Permission.CLUBS_CREATE inAssociation association.id) } returns false
        val exception = assertFailsWith(ControllerException::class) {
            controller.create(mockk(), association, payload)
        }
        assertEquals(HttpStatusCode.Forbidden, exception.code)
        assertEquals("clubs_validated_not_allowed", exception.key)
    }

    @Test
    fun testCreateInternalError() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val createClubUseCase = mockk<ICreateChildModelWithContextSuspendUseCase<Club, CreateClubPayload, String>>()
        val controller = ClubsController(
            mockk(), requireUserForCallUseCase, mockk(), mockk(), mockk(), mockk(),
            createClubUseCase, mockk(), mockk(), mockk(), mockk(), mockk()
        )
        val payload = CreateClubPayload("name", "description", "image", "member", "admin")
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { createClubUseCase(payload, club.associationId, OptionalUserContext(user.id)) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.create(mockk(), association, payload)
        }
        assertEquals(HttpStatusCode.InternalServerError, exception.code)
        assertEquals("error_internal", exception.key)
    }

}
