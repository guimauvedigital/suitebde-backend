package com.suitebde.usecases.associations

import com.suitebde.models.associations.CreateDomainInAssociationPayload
import com.suitebde.models.associations.DomainInAssociation
import com.suitebde.repositories.associations.IDomainsInAssociationsRepository
import com.suitebde.usecases.application.ISetupDomainUseCase
import dev.kaccelero.models.UUID
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateDomainInAssociationUseCaseTest {

    private val domain = DomainInAssociation("domain", UUID())
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
