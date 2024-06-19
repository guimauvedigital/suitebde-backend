package com.suitebde.usecases.clubs

import com.suitebde.models.clubs.*
import com.suitebde.models.users.OptionalUserContext
import com.suitebde.repositories.clubs.IClubsRepository
import dev.kaccelero.commons.repositories.ICreateChildModelSuspendUseCase
import dev.kaccelero.commons.repositories.ICreateChildModelWithContextSuspendUseCase
import dev.kaccelero.models.UUID
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateClubUseCaseTest {

    private val club = Club(
        UUID(), UUID(), "name", "description",
        "image", Clock.System.now(), true, 1, true
    )
    private val roleInClub = RoleInClub(
        UUID(), club.id, "name", true
    )
    private val userInClub = UserInClub(
        UUID(), club.id, UUID(), null, null, roleInClub
    )

    @Test
    fun testInvoke() = runBlocking {
        val repository = mockk<IClubsRepository>()
        val createRoleInClubUseCase =
            mockk<ICreateChildModelSuspendUseCase<RoleInClub, CreateRoleInClubPayload, UUID>>()
        val createUserInClubUseCase =
            mockk<ICreateChildModelWithContextSuspendUseCase<UserInClub, CreateUserInClubPayload, UUID>>()
        val createClubUseCase = CreateClubUseCase(repository, createRoleInClubUseCase, createUserInClubUseCase)
        val payload = CreateClubPayload("name", "description", "image", "member", "admin")
        coEvery { repository.create(payload, UUID(), OptionalUserContext(userInClub.userId)) } returns club
        coEvery {
            createRoleInClubUseCase(
                CreateRoleInClubPayload("member", admin = false, default = true),
                club.id
            )
        } returns RoleInClub(
            UUID(), club.id, "member", admin = false, default = true
        )
        coEvery {
            createRoleInClubUseCase(
                CreateRoleInClubPayload("admin", admin = true),
                club.id
            )
        } returns RoleInClub(
            UUID(), club.id, "admin", admin = true
        )
        coEvery {
            createUserInClubUseCase(
                CreateUserInClubPayload(UUID()),
                club.id,
                OptionalRoleInClubContext(UUID())
            )
        } returns userInClub
        assertEquals(club, createClubUseCase(payload, UUID(), OptionalUserContext(UUID())))
        coVerify {
            createUserInClubUseCase(
                CreateUserInClubPayload(UUID()),
                club.id,
                OptionalRoleInClubContext(UUID())
            )
        }
        coVerify { createRoleInClubUseCase(CreateRoleInClubPayload("member", admin = false, default = true), club.id) }
        coVerify { createRoleInClubUseCase(CreateRoleInClubPayload("admin", admin = true), club.id) }
    }

    @Test
    fun testInvokeNoUser() = runBlocking {
        val repository = mockk<IClubsRepository>()
        val createRoleInClubUseCase =
            mockk<ICreateChildModelSuspendUseCase<RoleInClub, CreateRoleInClubPayload, UUID>>()
        val createClubUseCase = CreateClubUseCase(repository, createRoleInClubUseCase, mockk())
        val payload = CreateClubPayload("name", "description", "image", "member", "admin")
        coEvery { repository.create(payload, UUID(), OptionalUserContext()) } returns club
        coEvery {
            createRoleInClubUseCase(
                CreateRoleInClubPayload("member", admin = false, default = true),
                club.id
            )
        } returns RoleInClub(
            UUID(), club.id, "member", admin = false, default = true
        )
        coEvery {
            createRoleInClubUseCase(
                CreateRoleInClubPayload("admin", admin = true),
                club.id
            )
        } returns RoleInClub(
            UUID(), club.id, "admin", admin = true
        )
        assertEquals(club, createClubUseCase(payload, UUID(), OptionalUserContext()))
        coVerify { createRoleInClubUseCase(CreateRoleInClubPayload("member", admin = false, default = true), club.id) }
        coVerify { createRoleInClubUseCase(CreateRoleInClubPayload("admin", admin = true), club.id) }
    }

    @Test
    fun testInvokeFails() = runBlocking {
        val repository = mockk<IClubsRepository>()
        val createClubUseCase = CreateClubUseCase(repository, mockk(), mockk())
        val payload = CreateClubPayload("name", "description", "image", "member", "admin")
        coEvery { repository.create(payload, UUID(), OptionalUserContext()) } returns null
        assertEquals(null, createClubUseCase(payload, UUID(), OptionalUserContext()))
    }

}
