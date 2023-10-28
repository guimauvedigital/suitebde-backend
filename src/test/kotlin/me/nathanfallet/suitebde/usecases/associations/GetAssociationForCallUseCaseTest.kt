package me.nathanfallet.suitebde.usecases.associations

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.repositories.IAssociationsRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAssociationForCallUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val associationRepository = mockk<IAssociationsRepository>()
        val useCase = GetAssociationForCallUseCase(associationRepository)
        val association = Association(
            "id", "name", "school", "city",
            true, Clock.System.now(), Clock.System.now()
        )
        val call = mockk<ApplicationCall>()
        coEvery { associationRepository.getAssociationForDomain("domain") } returns association
        every { call.request.host() } returns "domain"
        assertEquals(association, useCase(call))
    }

}