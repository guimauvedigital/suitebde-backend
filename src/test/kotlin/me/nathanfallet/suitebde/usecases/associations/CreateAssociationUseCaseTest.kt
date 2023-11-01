package me.nathanfallet.suitebde.usecases.associations

import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.IAssociationsRepository
import me.nathanfallet.suitebde.usecases.users.ICreateUserUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateAssociationUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val associationRepository = mockk<IAssociationsRepository>()
        val createUserUseCase = mockk<ICreateUserUseCase>()
        val now = Clock.System.now()
        val association = Association(
            "associationId", "name", "school", "city",
            false, now, now
        )
        val useCase = CreateAssociationUseCase(associationRepository, createUserUseCase)
        coEvery {
            associationRepository.createAssociation(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns association
        coEvery { createUserUseCase(any(), any()) } returns User(
            "id", "associationId", "email", null,
            "firstName", "lastName", true
        )
        assertEquals(
            association, useCase(
                CreateAssociationPayload(
                    "name", "school", "city", "email",
                    "password", "firstName", "lastName"
                ), now
            )
        )
        coVerifyOrder {
            associationRepository.createAssociation(
                "name", "school", "city", false, any(), any()
            )
            createUserUseCase(
                CreateUserPayload(
                    "associationId", "email", "password",
                    "firstName", "lastName", true
                ), now
            )
        }
    }

    @Test
    fun invokeWithNull() = runBlocking {
        val associationRepository = mockk<IAssociationsRepository>()
        val useCase = CreateAssociationUseCase(associationRepository, mockk())
        coEvery {
            associationRepository.createAssociation(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns null
        assertEquals(
            null, useCase(
                CreateAssociationPayload(
                    "name", "school", "city", "email",
                    "password", "firstName", "lastName"
                ), Clock.System.now()
            )
        )
    }

}