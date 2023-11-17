package me.nathanfallet.suitebde.usecases.associations

import io.ktor.http.*
import io.ktor.server.application.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.associations.Association
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RequireAssociationForCallUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val useCase = RequireAssociationForCallUseCase(getAssociationForCallUseCase)
        val call = mockk<ApplicationCall>()
        val association = Association(
            "associationId", "name", "school", "city",
            true, Clock.System.now(), Clock.System.now()
        )
        coEvery { getAssociationForCallUseCase(call) } returns association
        assertEquals(association, useCase(call))
    }

    @Test
    fun invokeFails() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val useCase = RequireAssociationForCallUseCase(getAssociationForCallUseCase)
        val call = mockk<ApplicationCall>()
        coEvery { getAssociationForCallUseCase(call) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            useCase(call)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("associations_not_found", exception.key)
    }

}
