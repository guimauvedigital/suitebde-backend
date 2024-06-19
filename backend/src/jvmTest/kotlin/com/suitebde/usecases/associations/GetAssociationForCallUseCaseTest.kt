package com.suitebde.usecases.associations

import com.suitebde.models.associations.Association
import com.suitebde.repositories.associations.IAssociationsRepository
import dev.kaccelero.models.UUID
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.util.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAssociationForCallUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val associationRepository = mockk<IAssociationsRepository>()
        val useCase = GetAssociationForCallUseCase(associationRepository)
        val association = Association(
            UUID(), "name", "school", "city",
            true, Clock.System.now(), Clock.System.now()
        )
        val attributes = Attributes()
        val call = mockk<ApplicationCall>()
        coEvery { associationRepository.getAssociationForDomain("domain") } returns association
        every { call.request.host() } returns "domain"
        every { call.attributes } returns attributes
        // Fetch from repository
        assertEquals(association, useCase(call))
        // Fetch from attributes
        assertEquals(association, useCase(call))
    }

}
