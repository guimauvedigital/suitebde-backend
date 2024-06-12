package me.nathanfallet.suitebde.controllers.associations

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateDomainInAssociationPayload
import me.nathanfallet.suitebde.models.associations.DomainInAssociation
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import me.nathanfallet.usecases.models.delete.IDeleteChildModelSuspendUseCase
import me.nathanfallet.usecases.models.get.IGetChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.slice.IListSliceChildModelSuspendUseCase
import me.nathanfallet.usecases.pagination.Pagination
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DomainsInAssociationsControllerTest {

    private val association = Association(
        "associationId", "name", "school", "city",
        true, Clock.System.now(), Clock.System.now()
    )
    private val user = User(
        "id", "associationId", "email", null,
        "firstname", "lastname", false, Clock.System.now()
    )
    private val domain = DomainInAssociation(
        "domain", "associationId"
    )

    @Test
    fun testList() = runBlocking {
        val getDomainsInAssociationsUseCase = mockk<IListSliceChildModelSuspendUseCase<DomainInAssociation, String>>()
        val call = mockk<ApplicationCall>()
        coEvery { getDomainsInAssociationsUseCase(Pagination(10, 5), association.id) } returns listOf(domain)
        val controller = DomainsInAssociationsController(
            mockk(),
            mockk(),
            mockk(),
            getDomainsInAssociationsUseCase,
            mockk(),
            mockk(),
            mockk()
        )
        every { call.request.path() } returns "/api/v1/associations/id/domains"
        assertEquals(listOf(domain), controller.list(call, association, 10, 5))
    }

    @Test
    fun testListDefaultLimitOffset() = runBlocking {
        val getDomainsInAssociationsUseCase = mockk<IListSliceChildModelSuspendUseCase<DomainInAssociation, String>>()
        val call = mockk<ApplicationCall>()
        coEvery { getDomainsInAssociationsUseCase(Pagination(25, 0), association.id) } returns listOf(domain)
        val controller = DomainsInAssociationsController(
            mockk(),
            mockk(),
            mockk(),
            getDomainsInAssociationsUseCase,
            mockk(),
            mockk(),
            mockk()
        )
        every { call.request.path() } returns "/api/v1/associations/id/domains"
        assertEquals(listOf(domain), controller.list(call, association, null, null))
    }

    @Test
    fun testListAdmin() = runBlocking {
        val getDomainsInAssociationsUseCase = mockk<IListChildModelSuspendUseCase<DomainInAssociation, String>>()
        val call = mockk<ApplicationCall>()
        coEvery { getDomainsInAssociationsUseCase(association.id) } returns listOf(domain)
        val controller = DomainsInAssociationsController(
            mockk(),
            mockk(),
            getDomainsInAssociationsUseCase,
            mockk(),
            mockk(),
            mockk(),
            mockk()
        )
        every { call.request.path() } returns "/admin/associations/id/domains"
        assertEquals(listOf(domain), controller.list(call, association, null, null))
    }

    @Test
    fun testGet() = runBlocking {
        val getDomainUseCase = mockk<IGetChildModelSuspendUseCase<DomainInAssociation, String, String>>()
        val call = mockk<ApplicationCall>()
        coEvery { getDomainUseCase(domain.domain, association.id) } returns domain
        val controller = DomainsInAssociationsController(
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            getDomainUseCase,
            mockk(),
            mockk()
        )
        assertEquals(domain, controller.get(call, association, domain.domain))
    }

    @Test
    fun testGetNotFound() = runBlocking {
        val getDomainUseCase = mockk<IGetChildModelSuspendUseCase<DomainInAssociation, String, String>>()
        val call = mockk<ApplicationCall>()
        coEvery { getDomainUseCase(domain.domain, association.id) } returns null
        val controller = DomainsInAssociationsController(
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            getDomainUseCase,
            mockk(),
            mockk()
        )
        val exception = assertFailsWith(ControllerException::class) {
            controller.get(call, association, domain.domain)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("domains_not_found", exception.key)
    }

    @Test
    fun testCreate() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val createDomainUseCase =
            mockk<ICreateChildModelSuspendUseCase<DomainInAssociation, CreateDomainInAssociationPayload, String>>()
        val call = mockk<ApplicationCall>()
        val payload = CreateDomainInAssociationPayload("domain")
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.DOMAINS_CREATE inAssociation association.id) } returns true
        coEvery { createDomainUseCase(payload, association.id) } returns domain
        val controller = DomainsInAssociationsController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            mockk(),
            createDomainUseCase,
            mockk()
        )
        assertEquals(domain, controller.create(call, association, payload))
    }

    @Test
    fun testCreateForbidden() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val call = mockk<ApplicationCall>()
        val payload = CreateDomainInAssociationPayload("domain")
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.DOMAINS_CREATE inAssociation association.id) } returns false
        val controller = DomainsInAssociationsController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            mockk()
        )
        val exception = assertFailsWith(ControllerException::class) {
            controller.create(call, association, payload)
        }
        assertEquals(HttpStatusCode.Forbidden, exception.code)
        assertEquals("domains_update_not_allowed", exception.key)
    }

    @Test
    fun testCreateInternalError() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val createDomainUseCase =
            mockk<ICreateChildModelSuspendUseCase<DomainInAssociation, CreateDomainInAssociationPayload, String>>()
        val call = mockk<ApplicationCall>()
        val payload = CreateDomainInAssociationPayload("domain")
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.DOMAINS_CREATE inAssociation association.id) } returns true
        coEvery { createDomainUseCase(payload, association.id) } returns null
        val controller = DomainsInAssociationsController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            mockk(),
            createDomainUseCase,
            mockk()
        )
        val exception = assertFailsWith(ControllerException::class) {
            controller.create(call, association, payload)
        }
        assertEquals(HttpStatusCode.InternalServerError, exception.code)
        assertEquals("error_internal", exception.key)
    }

    @Test
    fun testDelete() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getDomainUseCase = mockk<IGetChildModelSuspendUseCase<DomainInAssociation, String, String>>()
        val deleteDomainUseCase = mockk<IDeleteChildModelSuspendUseCase<DomainInAssociation, String, String>>()
        val call = mockk<ApplicationCall>()
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.DOMAINS_DELETE inAssociation association.id) } returns true
        coEvery { getDomainUseCase(domain.domain, association.id) } returns domain
        coEvery { deleteDomainUseCase(domain.domain, association.id) } returns true
        val controller = DomainsInAssociationsController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            getDomainUseCase,
            mockk(),
            deleteDomainUseCase
        )
        controller.delete(call, association, domain.domain)
        coVerify {
            deleteDomainUseCase(domain.domain, association.id)
        }
    }

    @Test
    fun testDeleteForbidden() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.DOMAINS_DELETE inAssociation association.id) } returns false
        val controller = DomainsInAssociationsController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            mockk()
        )
        val exception = assertFailsWith(ControllerException::class) {
            controller.delete(call, association, domain.domain)
        }
        assertEquals(HttpStatusCode.Forbidden, exception.code)
        assertEquals("domains_delete_not_allowed", exception.key)
    }

    @Test
    fun testDeleteNotFound() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val getDomainUseCase = mockk<IGetChildModelSuspendUseCase<DomainInAssociation, String, String>>()
        val deleteDomainUseCase = mockk<IDeleteChildModelSuspendUseCase<DomainInAssociation, String, String>>()
        val call = mockk<ApplicationCall>()
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { checkPermissionUseCase(user, Permission.DOMAINS_DELETE inAssociation association.id) } returns true
        coEvery { getDomainUseCase(domain.domain, association.id) } returns domain
        coEvery { deleteDomainUseCase(domain.domain, association.id) } returns false
        val controller = DomainsInAssociationsController(
            requireUserForCallUseCase,
            checkPermissionUseCase,
            mockk(),
            mockk(),
            getDomainUseCase,
            mockk(),
            deleteDomainUseCase
        )
        val exception = assertFailsWith(ControllerException::class) {
            controller.delete(call, association, domain.domain)
        }
        assertEquals(HttpStatusCode.InternalServerError, exception.code)
        assertEquals("error_internal", exception.key)
    }

}
