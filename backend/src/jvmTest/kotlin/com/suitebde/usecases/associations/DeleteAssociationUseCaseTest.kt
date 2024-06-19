package com.suitebde.usecases.associations

import com.suitebde.models.associations.Association
import com.suitebde.models.clubs.Club
import com.suitebde.models.users.User
import com.suitebde.repositories.associations.IAssociationsRepository
import dev.kaccelero.commons.repositories.IDeleteChildModelSuspendUseCase
import dev.kaccelero.commons.repositories.IListChildModelSuspendUseCase
import dev.kaccelero.commons.repositories.IListChildModelWithContextSuspendUseCase
import dev.kaccelero.models.UUID
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlin.test.Test

class DeleteAssociationUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val repository = mockk<IAssociationsRepository>()
        val listUsersUseCase = mockk<IListChildModelSuspendUseCase<User, UUID>>()
        val deleteUserUseCase = mockk<IDeleteChildModelSuspendUseCase<User, UUID, UUID>>()
        val listClubsUseCase = mockk<IListChildModelWithContextSuspendUseCase<Club, UUID>>()
        val deleteClubUseCase = mockk<IDeleteChildModelSuspendUseCase<Club, UUID, UUID>>()
        val useCase = DeleteAssociationUseCase(
            repository,
            listUsersUseCase,
            deleteUserUseCase,
            listClubsUseCase,
            deleteClubUseCase
        )
        val now = Clock.System.now()
        val association = Association(UUID(), "name", "school", "city", true, now, now)
        coEvery { repository.delete(association.id) } returns true
        coEvery { listUsersUseCase(association.id) } returns listOf(
            User(
                UUID(), UUID(), "name", "password", "firstName", "lastName", false, Clock.System.now()
            )
        )
        coEvery { deleteUserUseCase(UUID(), UUID()) } returns true
        coEvery { listClubsUseCase(association.id, any()) } returns listOf(
            Club(
                UUID(),
                UUID(),
                "name",
                "description",
                "logo",
                now,
                true,
                0,
                false
            )
        )
        coEvery { deleteClubUseCase(UUID(), UUID()) } returns true
        useCase(association.id)
        coVerify { repository.delete(association.id) }
        coVerify { deleteUserUseCase(UUID(), UUID()) }
        coVerify { deleteClubUseCase(UUID(), UUID()) }
    }

}
