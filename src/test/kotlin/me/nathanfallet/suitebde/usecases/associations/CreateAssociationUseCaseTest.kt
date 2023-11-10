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
import me.nathanfallet.suitebde.repositories.associations.IAssociationsRepository
import me.nathanfallet.usecases.models.create.ICreateModelSuspendUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateAssociationUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val associationRepository = mockk<IAssociationsRepository>()
        val createUserUseCase = mockk<ICreateModelSuspendUseCase<User, CreateUserPayload>>()
        val now = Clock.System.now()
        val association = Association(
            "associationId", "name", "school", "city",
            false, now, now
        )
        val useCase = CreateAssociationUseCase(associationRepository, createUserUseCase)
        coEvery { associationRepository.create(any()) } returns association
        coEvery { createUserUseCase(any()) } returns User(
            "id", "associationId", "email", null,
            "firstName", "lastName", true
        )
        assertEquals(
            association, useCase(
                CreateAssociationPayload(
                    "name", "school", "city", "email",
                    "password", "firstName", "lastName"
                )
            )
        )
        coVerifyOrder {
            associationRepository.create(
                CreateAssociationPayload(
                    "name", "school", "city", "email", "password", "firstName", "lastName"
                )
            )
            createUserUseCase(
                CreateUserPayload(
                    "associationId", "email", "password",
                    "firstName", "lastName", true
                )
            )
        }
    }

    @Test
    fun invokeWithNull() = runBlocking {
        val associationRepository = mockk<IAssociationsRepository>()
        val useCase = CreateAssociationUseCase(associationRepository, mockk())
        coEvery { associationRepository.create(any()) } returns null
        assertEquals(
            null, useCase(
                CreateAssociationPayload(
                    "name", "school", "city", "email",
                    "password", "firstName", "lastName"
                )
            )
        )
    }

}