package me.nathanfallet.suitebde.usecases.associations

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.models.associations.CreateDomainInAssociationPayload
import me.nathanfallet.suitebde.models.associations.DomainInAssociation
import me.nathanfallet.suitebde.repositories.associations.IDomainsInAssociationsRepository
import me.nathanfallet.suitebde.usecases.application.ISetupDomainUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateDomainInAssociationUseCaseTest {

    private val domain = DomainInAssociation("domain", "associationId")
    private val payload = CreateDomainInAssociationPayload("domain")

    @Test
    fun invoke() = runBlocking {
        val repository = mockk<IDomainsInAssociationsRepository>()
        val setupDomainUseCase = mockk<ISetupDomainUseCase>()
        val useCase = CreateDomainInAssociationUseCase(repository, setupDomainUseCase)
        coEvery { repository.get(payload.domain, domain.associationId) } returns null
        coEvery { setupDomainUseCase(payload.domain) } returns true
        coEvery { repository.create(payload, domain.associationId) } returns domain
        assertEquals(domain, useCase(payload, domain.associationId))
    }

    @Test
    fun invokeExists() = runBlocking {
        val repository = mockk<IDomainsInAssociationsRepository>()
        val setupDomainUseCase = mockk<ISetupDomainUseCase>()
        val useCase = CreateDomainInAssociationUseCase(repository, setupDomainUseCase)
        coEvery { repository.get(payload.domain, domain.associationId) } returns domain
        assertEquals(null, useCase(payload, domain.associationId))
    }

    @Test
    fun invokeFails() = runBlocking {
        val repository = mockk<IDomainsInAssociationsRepository>()
        val setupDomainUseCase = mockk<ISetupDomainUseCase>()
        val useCase = CreateDomainInAssociationUseCase(repository, setupDomainUseCase)
        coEvery { repository.get(payload.domain, domain.associationId) } returns null
        coEvery { setupDomainUseCase(payload.domain) } returns false
        assertEquals(null, useCase(payload, domain.associationId))
    }

}
