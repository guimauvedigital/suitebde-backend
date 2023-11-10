package me.nathanfallet.suitebde.usecases.associations

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.repositories.associations.IDomainsInAssociationsRepository
import me.nathanfallet.suitebde.usecases.application.IShutdownDomainUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class DeleteDomainInAssociationUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val repository = mockk<IDomainsInAssociationsRepository>()
        val shutdownDomainUseCase = mockk<IShutdownDomainUseCase>()
        val useCase = DeleteDomainInAssociationUseCase(repository, shutdownDomainUseCase)
        coEvery { shutdownDomainUseCase("domain") } returns true
        coEvery { repository.delete("domain", "associationId") } returns true
        assertEquals(true, useCase("domain", "associationId"))
    }

    @Test
    fun invokeFailsDelete() = runBlocking {
        val repository = mockk<IDomainsInAssociationsRepository>()
        val shutdownDomainUseCase = mockk<IShutdownDomainUseCase>()
        val useCase = DeleteDomainInAssociationUseCase(repository, shutdownDomainUseCase)
        coEvery { shutdownDomainUseCase("domain") } returns true
        coEvery { repository.delete("domain", "associationId") } returns false
        assertEquals(false, useCase("domain", "associationId"))
    }

    @Test
    fun invokeFailsShutdown() = runBlocking {
        val repository = mockk<IDomainsInAssociationsRepository>()
        val shutdownDomainUseCase = mockk<IShutdownDomainUseCase>()
        val useCase = DeleteDomainInAssociationUseCase(repository, shutdownDomainUseCase)
        coEvery { shutdownDomainUseCase("domain") } returns false
        coEvery { repository.delete("domain", "associationId") } returns true
        assertEquals(false, useCase("domain", "associationId"))
    }

}
