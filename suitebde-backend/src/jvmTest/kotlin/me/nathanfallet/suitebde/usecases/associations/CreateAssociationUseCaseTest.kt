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
import me.nathanfallet.suitebde.models.web.CreateWebMenuPayload
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.suitebde.models.web.WebPagePayload
import me.nathanfallet.suitebde.repositories.associations.IAssociationsRepository
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateAssociationUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val associationRepository = mockk<IAssociationsRepository>()
        val createUserUseCase = mockk<ICreateChildModelSuspendUseCase<User, CreateUserPayload, String>>()
        val createWebPageUseCase = mockk<ICreateChildModelSuspendUseCase<WebPage, WebPagePayload, String>>()
        val createWebMenuUseCase = mockk<ICreateChildModelSuspendUseCase<WebMenu, CreateWebMenuPayload, String>>()
        val now = Clock.System.now()
        val association = Association(
            "associationId", "name", "school", "city",
            false, now, now
        )
        val useCase = CreateAssociationUseCase(
            associationRepository, createUserUseCase,
            createWebPageUseCase, createWebMenuUseCase
        )
        coEvery { associationRepository.create(any()) } returns association
        coEvery { createUserUseCase(any(), "associationId") } returns User(
            "id", "associationId", "email", null,
            "firstName", "lastName", true, Clock.System.now()
        )
        coEvery { createWebPageUseCase(any(), "associationId") } returns mockk()
        coEvery { createWebMenuUseCase(any(), "associationId") } returns mockk()
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
                    "email", "password",
                    "firstName", "lastName", true
                ),
                "associationId"
            )
        }
    }

    @Test
    fun invokeWithNull() = runBlocking {
        val associationRepository = mockk<IAssociationsRepository>()
        val useCase = CreateAssociationUseCase(associationRepository, mockk(), mockk(), mockk())
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
