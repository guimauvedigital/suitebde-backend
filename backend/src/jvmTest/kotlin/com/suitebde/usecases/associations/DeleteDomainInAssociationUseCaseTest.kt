package com.suitebde.usecases.associations

import com.suitebde.repositories.associations.IDomainsInAssociationsRepository
import com.suitebde.usecases.application.IShutdownDomainUseCase
import dev.kaccelero.models.UUID
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class DeleteDomainInAssociationUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val repository = mockk<IDomainsInAssociationsRepository>()
        val shutdownDomainUseCase = mockk<IShutdownDomainUseCase>()
        val useCase = DeleteDomainInAssociationUseCase(repository, shutdownDomainUseCase)
        val associationId = UUID()
        coEvery { shutdownDomainUseCase("domain") } returns true
        coEvery { repository.delete("domain", associationId) } returns true
        assertEquals(true, useCase("domain", associationId))
    }

    @Test
    fun invokeFailsDelete() = runBlocking {
        val repository = mockk<IDomainsInAssociationsRepository>()
        val shutdownDomainUseCase = mockk<IShutdownDomainUseCase>()
        val useCase = DeleteDomainInAssociationUseCase(repository, shutdownDomainUseCase)
        val associationId = UUID()
        coEvery { shutdownDomainUseCase("domain") } returns true
        coEvery { repository.delete("domain", associationId) } returns false
        assertEquals(false, useCase("domain", associationId))
    }

    @Test
    fun invokeFailsShutdown() = runBlocking {
        val repository = mockk<IDomainsInAssociationsRepository>()
        val shutdownDomainUseCase = mockk<IShutdownDomainUseCase>()
        val useCase = DeleteDomainInAssociationUseCase(repository, shutdownDomainUseCase)
        val associationId = UUID()
        coEvery { shutdownDomainUseCase("domain") } returns false
        coEvery { repository.delete("domain", associationId) } returns true
        assertEquals(false, useCase("domain", associationId))
    }

}
