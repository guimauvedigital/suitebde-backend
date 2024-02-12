package me.nathanfallet.suitebde.usecases.clubs

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.clubs.CreateClubPayload
import me.nathanfallet.suitebde.models.clubs.CreateUserInClubPayload
import me.nathanfallet.suitebde.models.clubs.UserInClub
import me.nathanfallet.suitebde.models.users.OptionalUserContext
import me.nathanfallet.suitebde.repositories.clubs.IClubsRepository
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateClubUseCaseTest {

    private val club = Club(
        "roleId", "associationId", "name", "description",
        "image", Clock.System.now(), true, 1, true
    )
    private val userInClub = UserInClub(
        "userId", club.id
    )

    @Test
    fun testInvoke() = runBlocking {
        val repository = mockk<IClubsRepository>()
        val createUserInClubUseCase =
            mockk<ICreateChildModelSuspendUseCase<UserInClub, CreateUserInClubPayload, String>>()
        val createClubUseCase = CreateClubUseCase(repository, createUserInClubUseCase)
        val payload = CreateClubPayload("name", "description", "image")
        coEvery { repository.create(payload, "associationId", OptionalUserContext("userId")) } returns club
        coEvery { createUserInClubUseCase(CreateUserInClubPayload("userId"), club.id) } returns userInClub
        assertEquals(club, createClubUseCase(payload, "associationId", OptionalUserContext("userId")))
        coVerify { createUserInClubUseCase(CreateUserInClubPayload("userId"), club.id) }
    }

    @Test
    fun testInvokeNoUser() = runBlocking {
        val repository = mockk<IClubsRepository>()
        val createClubUseCase = CreateClubUseCase(repository, mockk())
        val payload = CreateClubPayload("name", "description", "image")
        coEvery { repository.create(payload, "associationId", OptionalUserContext()) } returns club
        assertEquals(club, createClubUseCase(payload, "associationId", OptionalUserContext()))
    }

    @Test
    fun testInvokeFails() = runBlocking {
        val repository = mockk<IClubsRepository>()
        val createClubUseCase = CreateClubUseCase(repository, mockk())
        val payload = CreateClubPayload("name", "description", "image")
        coEvery { repository.create(payload, "associationId", OptionalUserContext()) } returns null
        assertEquals(null, createClubUseCase(payload, "associationId", OptionalUserContext()))
    }

}
