package me.nathanfallet.suitebde.usecases.associations

import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.extensions.invoke
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.IAssociationsRepository
import me.nathanfallet.suitebde.repositories.IUsersRepository
import me.nathanfallet.suitebde.usecases.auth.IHashPasswordUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateAssociationUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val associationRepository = mockk<IAssociationsRepository>()
        val usersRepository = mockk<IUsersRepository>()
        val hashPasswordUseCase = mockk<IHashPasswordUseCase>()
        val now = Clock.System.now()
        val association = Association(
            "associationId", "name", "school", "city",
            false, now, now
        )
        val useCase = CreateAssociationUseCase(associationRepository, usersRepository, hashPasswordUseCase)
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
        coEvery { usersRepository.createUser(any(), any(), any(), any(), any(), any()) } returns User(
            "id", "associationId", "email", null,
            "firstName", "lastName", true
        )
        every { hashPasswordUseCase(any()) } returns "hash"
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
            usersRepository.createUser(
                "associationId", "email", "hash", "firstName", "lastName", true
            )
        }
    }

    @Test
    fun invokeWithNull() = runBlocking {
        val associationRepository = mockk<IAssociationsRepository>()
        val usersRepository = mockk<IUsersRepository>()
        val hashPasswordUseCase = mockk<IHashPasswordUseCase>()
        val useCase = CreateAssociationUseCase(associationRepository, usersRepository, hashPasswordUseCase)
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