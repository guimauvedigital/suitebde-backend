package me.nathanfallet.suitebde.usecases.associations

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.associations.IAssociationsRepository
import me.nathanfallet.usecases.models.delete.IDeleteChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.context.IListChildModelWithContextSuspendUseCase
import kotlin.test.Test

class DeleteAssociationUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val repository = mockk<IAssociationsRepository>()
        val listUsersUseCase = mockk<IListChildModelSuspendUseCase<User, String>>()
        val deleteUserUseCase = mockk<IDeleteChildModelSuspendUseCase<User, String, String>>()
        val listClubsUseCase = mockk<IListChildModelWithContextSuspendUseCase<Club, String>>()
        val deleteClubUseCase = mockk<IDeleteChildModelSuspendUseCase<Club, String, String>>()
        val useCase = DeleteAssociationUseCase(
            repository,
            listUsersUseCase,
            deleteUserUseCase,
            listClubsUseCase,
            deleteClubUseCase
        )
        val now = Clock.System.now()
        val association = Association("associationId", "name", "school", "city", true, now, now)
        coEvery { repository.delete(association.id) } returns true
        coEvery { listUsersUseCase(association.id) } returns listOf(
            User(
                "userId", "associationId", "name", "password", "firstName", "lastName", false, Clock.System.now()
            )
        )
        coEvery { deleteUserUseCase("userId", "associationId") } returns true
        coEvery { listClubsUseCase(association.id, any()) } returns listOf(
            Club(
                "clubId",
                "associationId",
                "name",
                "description",
                "logo",
                now,
                true,
                0,
                false
            )
        )
        coEvery { deleteClubUseCase("clubId", "associationId") } returns true
        useCase(association.id)
        coVerify { repository.delete(association.id) }
        coVerify { deleteUserUseCase("userId", "associationId") }
        coVerify { deleteClubUseCase("clubId", "associationId") }
    }

}
