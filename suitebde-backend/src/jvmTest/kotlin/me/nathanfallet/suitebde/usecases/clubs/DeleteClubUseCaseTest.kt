package me.nathanfallet.suitebde.usecases.clubs

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.models.clubs.UserInClub
import me.nathanfallet.suitebde.repositories.clubs.IClubsRepository
import me.nathanfallet.usecases.models.delete.IDeleteChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class DeleteClubUseCaseTest {

    private val userInClub = UserInClub(
        "userId", "clubId"
    )

    @Test
    fun testInvoke() = runBlocking {
        val repository = mockk<IClubsRepository>()
        val listUsersInClubUseCase = mockk<IListChildModelSuspendUseCase<UserInClub, String>>()
        val deleteUserInClubUseCase = mockk<IDeleteChildModelSuspendUseCase<UserInClub, String, String>>()
        val useCase = DeleteClubUseCase(repository, listUsersInClubUseCase, deleteUserInClubUseCase)
        coEvery { repository.delete("clubId", "associationId") } returns true
        coEvery { listUsersInClubUseCase("clubId") } returns listOf(userInClub)
        coEvery { deleteUserInClubUseCase("userId", "clubId") } returns true
        assertEquals(true, useCase("clubId", "associationId"))
        coVerify { deleteUserInClubUseCase("userId", "clubId") }
        coVerify { repository.delete("clubId", "associationId") }
    }

    @Test
    fun testInvokeFails() = runBlocking {
        val repository = mockk<IClubsRepository>()
        val listUsersInClubUseCase = mockk<IListChildModelSuspendUseCase<UserInClub, String>>()
        val deleteUserInClubUseCase = mockk<IDeleteChildModelSuspendUseCase<UserInClub, String, String>>()
        val useCase = DeleteClubUseCase(repository, listUsersInClubUseCase, deleteUserInClubUseCase)
        coEvery { repository.delete("clubId", "associationId") } returns false
        assertEquals(false, useCase("clubId", "associationId"))
    }

}
