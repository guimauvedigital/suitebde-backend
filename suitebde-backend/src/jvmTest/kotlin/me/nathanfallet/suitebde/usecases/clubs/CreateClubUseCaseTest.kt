package me.nathanfallet.suitebde.usecases.clubs

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.clubs.*
import me.nathanfallet.suitebde.models.users.OptionalUserContext
import me.nathanfallet.suitebde.repositories.clubs.IClubsRepository
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import me.nathanfallet.usecases.models.create.context.ICreateChildModelWithContextSuspendUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateClubUseCaseTest {

    private val club = Club(
        "roleId", "associationId", "name", "description",
        "image", Clock.System.now(), true, 1, true
    )
    private val roleInClub = RoleInClub(
        "roleId", club.id, "name", true
    )
    private val userInClub = UserInClub(
        "userId", club.id, "roleId", null, null, roleInClub
    )

    @Test
    fun testInvoke() = runBlocking {
        val repository = mockk<IClubsRepository>()
        val createRoleInClubUseCase =
            mockk<ICreateChildModelSuspendUseCase<RoleInClub, CreateRoleInClubPayload, String>>()
        val createUserInClubUseCase =
            mockk<ICreateChildModelWithContextSuspendUseCase<UserInClub, CreateUserInClubPayload, String>>()
        val createClubUseCase = CreateClubUseCase(repository, createRoleInClubUseCase, createUserInClubUseCase)
        val payload = CreateClubPayload("name", "description", "image", "member", "admin")
        coEvery { repository.create(payload, "associationId", OptionalUserContext("userId")) } returns club
        coEvery {
            createRoleInClubUseCase(
                CreateRoleInClubPayload("member", admin = false, default = true),
                club.id
            )
        } returns RoleInClub(
            "memberRoleId", club.id, "member", admin = false, default = true
        )
        coEvery {
            createRoleInClubUseCase(
                CreateRoleInClubPayload("admin", admin = true),
                club.id
            )
        } returns RoleInClub(
            "adminRoleId", club.id, "admin", admin = true
        )
        coEvery {
            createUserInClubUseCase(
                CreateUserInClubPayload("userId"),
                club.id,
                OptionalRoleInClubContext("adminRoleId")
            )
        } returns userInClub
        assertEquals(club, createClubUseCase(payload, "associationId", OptionalUserContext("userId")))
        coVerify {
            createUserInClubUseCase(
                CreateUserInClubPayload("userId"),
                club.id,
                OptionalRoleInClubContext("adminRoleId")
            )
        }
        coVerify { createRoleInClubUseCase(CreateRoleInClubPayload("member", admin = false, default = true), club.id) }
        coVerify { createRoleInClubUseCase(CreateRoleInClubPayload("admin", admin = true), club.id) }
    }

    @Test
    fun testInvokeNoUser() = runBlocking {
        val repository = mockk<IClubsRepository>()
        val createRoleInClubUseCase =
            mockk<ICreateChildModelSuspendUseCase<RoleInClub, CreateRoleInClubPayload, String>>()
        val createClubUseCase = CreateClubUseCase(repository, createRoleInClubUseCase, mockk())
        val payload = CreateClubPayload("name", "description", "image", "member", "admin")
        coEvery { repository.create(payload, "associationId", OptionalUserContext()) } returns club
        coEvery {
            createRoleInClubUseCase(
                CreateRoleInClubPayload("member", admin = false, default = true),
                club.id
            )
        } returns RoleInClub(
            "memberRoleId", club.id, "member", admin = false, default = true
        )
        coEvery {
            createRoleInClubUseCase(
                CreateRoleInClubPayload("admin", admin = true),
                club.id
            )
        } returns RoleInClub(
            "adminRoleId", club.id, "admin", admin = true
        )
        assertEquals(club, createClubUseCase(payload, "associationId", OptionalUserContext()))
        coVerify { createRoleInClubUseCase(CreateRoleInClubPayload("member", admin = false, default = true), club.id) }
        coVerify { createRoleInClubUseCase(CreateRoleInClubPayload("admin", admin = true), club.id) }
    }

    @Test
    fun testInvokeFails() = runBlocking {
        val repository = mockk<IClubsRepository>()
        val createClubUseCase = CreateClubUseCase(repository, mockk(), mockk())
        val payload = CreateClubPayload("name", "description", "image", "member", "admin")
        coEvery { repository.create(payload, "associationId", OptionalUserContext()) } returns null
        assertEquals(null, createClubUseCase(payload, "associationId", OptionalUserContext()))
    }

}
