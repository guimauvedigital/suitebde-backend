package com.suitebde.usecases.associations

import com.suitebde.models.associations.Association
import com.suitebde.models.associations.CreateAssociationPayload
import com.suitebde.models.users.CreateUserPayload
import com.suitebde.models.users.User
import com.suitebde.models.web.CreateWebMenuPayload
import com.suitebde.models.web.WebMenu
import com.suitebde.models.web.WebPage
import com.suitebde.models.web.WebPagePayload
import com.suitebde.repositories.associations.IAssociationsRepository
import dev.kaccelero.commons.repositories.ICreateChildModelSuspendUseCase
import dev.kaccelero.models.UUID
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateAssociationUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val associationRepository = mockk<IAssociationsRepository>()
        val createUserUseCase = mockk<ICreateChildModelSuspendUseCase<User, CreateUserPayload, UUID>>()
        val createWebPageUseCase = mockk<ICreateChildModelSuspendUseCase<WebPage, WebPagePayload, UUID>>()
        val createWebMenuUseCase = mockk<ICreateChildModelSuspendUseCase<WebMenu, CreateWebMenuPayload, UUID>>()
        val now = Clock.System.now()
        val association = Association(
            UUID(), "name", "school", "city",
            false, now, now
        )
        val useCase = CreateAssociationUseCase(
            associationRepository, createUserUseCase,
            createWebPageUseCase, createWebMenuUseCase
        )
        coEvery { associationRepository.create(any()) } returns association
        coEvery { createUserUseCase(any(), UUID()) } returns User(
            UUID(), UUID(), "email", null,
            "firstName", "lastName", true, Clock.System.now()
        )
        coEvery { createWebPageUseCase(any(), UUID()) } returns mockk()
        coEvery { createWebMenuUseCase(any(), UUID()) } returns mockk()
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
                UUID()
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
