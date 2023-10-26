package me.nathanfallet.suitebde.usecases.associations

import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.IAssociationsRepository
import me.nathanfallet.suitebde.repositories.IUsersRepository
import me.nathanfallet.suitebde.usecases.auth.IHashPasswordUseCase
import kotlin.test.Test

class CreateAssociationUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val associationRepository = mockk<IAssociationsRepository>()
        val usersRepository = mockk<IUsersRepository>()
        val hashPasswordUseCase = mockk<IHashPasswordUseCase>()
        val useCase = CreateAssociationUseCase(associationRepository, usersRepository, hashPasswordUseCase)
        coEvery { associationRepository.createAssociation(any()) } returns Association("associationId", "name")
        coEvery { usersRepository.createUser(any(), any(), any(), any(), any(), any()) } returns User(
            "id",
            "associationId",
            "email",
            null,
            "firstName",
            "lastName",
            true
        )
        every { hashPasswordUseCase(any()) } returns "hash"
        useCase(CreateAssociationPayload("name", "email", "password", "firstName", "lastName"))
        coVerifyOrder {
            associationRepository.createAssociation("name")
            usersRepository.createUser("associationId", "email", "hash", "firstName", "lastName", true)
        }
    }

}