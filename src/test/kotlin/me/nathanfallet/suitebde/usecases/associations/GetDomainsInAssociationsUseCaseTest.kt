package me.nathanfallet.suitebde.usecases.associations

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.models.associations.DomainInAssociation
import me.nathanfallet.suitebde.repositories.associations.IDomainsInAssociationsRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class GetDomainsInAssociationsUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val domainsInAssociationsRepository = mockk<IDomainsInAssociationsRepository>()
        val useCase = GetDomainsInAssociationsUseCase(domainsInAssociationsRepository)
        val domain = DomainInAssociation("domain", "associationId")
        coEvery { domainsInAssociationsRepository.getDomains(domain.associationId) } returns listOf(domain)
        assertEquals(listOf(domain), useCase(domain.associationId))
    }

}
