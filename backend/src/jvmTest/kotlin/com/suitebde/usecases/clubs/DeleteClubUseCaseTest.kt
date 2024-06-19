package com.suitebde.usecases.clubs

import com.suitebde.models.clubs.RoleInClub
import com.suitebde.models.clubs.UserInClub
import com.suitebde.repositories.clubs.IClubsRepository
import dev.kaccelero.commons.repositories.IDeleteChildModelSuspendUseCase
import dev.kaccelero.commons.repositories.IListChildModelSuspendUseCase
import dev.kaccelero.models.UUID
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class DeleteClubUseCaseTest {

    private val roleInClub = RoleInClub(
        UUID(), UUID(), "name", true
    )
    private val userInClub = UserInClub(
        UUID(), UUID(), UUID(), null, null, roleInClub
    )

    @Test
    fun testInvoke() = runBlocking {
        val repository = mockk<IClubsRepository>()
        val listUsersInClubUseCase = mockk<IListChildModelSuspendUseCase<UserInClub, UUID>>()
        val deleteUserInClubUseCase = mockk<IDeleteChildModelSuspendUseCase<UserInClub, UUID, UUID>>()
        val listRolesInClubUseCase = mockk<IListChildModelSuspendUseCase<RoleInClub, UUID>>()
        val deleteRoleInClubUseCase = mockk<IDeleteChildModelSuspendUseCase<RoleInClub, UUID, UUID>>()
        val useCase = DeleteClubUseCase(
            repository,
            listUsersInClubUseCase,
            deleteUserInClubUseCase,
            listRolesInClubUseCase,
            deleteRoleInClubUseCase
        )
        coEvery { repository.delete(userInClub.clubId, UUID()) } returns true
        coEvery { listUsersInClubUseCase(userInClub.clubId) } returns listOf(userInClub)
        coEvery { deleteUserInClubUseCase(userInClub.userId, userInClub.clubId) } returns true
        coEvery { listRolesInClubUseCase(userInClub.clubId) } returns listOf(roleInClub)
        coEvery { deleteRoleInClubUseCase(userInClub.roleId, userInClub.clubId) } returns true
        assertEquals(true, useCase(userInClub.clubId, UUID()))
        coVerify { deleteUserInClubUseCase(userInClub.userId, userInClub.clubId) }
        coVerify { deleteRoleInClubUseCase(userInClub.roleId, userInClub.clubId) }
        coVerify { repository.delete(userInClub.clubId, UUID()) }
    }

    @Test
    fun testInvokeFails() = runBlocking {
        val repository = mockk<IClubsRepository>()
        val useCase = DeleteClubUseCase(repository, mockk(), mockk(), mockk(), mockk())
        coEvery { repository.delete(userInClub.clubId, UUID()) } returns false
        assertEquals(false, useCase(userInClub.clubId, UUID()))
    }

}
