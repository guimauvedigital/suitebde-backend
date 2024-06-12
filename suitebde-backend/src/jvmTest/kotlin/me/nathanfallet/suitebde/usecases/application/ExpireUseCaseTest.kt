package me.nathanfallet.suitebde.usecases.application

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.associations.ICodesInEmailsRepository
import me.nathanfallet.suitebde.repositories.users.IClientsInUsersRepository
import me.nathanfallet.suitebde.usecases.associations.IDeleteCodeInEmailUseCase
import me.nathanfallet.suitebde.usecases.auth.IDeleteAuthCodeUseCase
import me.nathanfallet.suitebde.usecases.users.IListUsersLastLoggedBeforeUseCase
import me.nathanfallet.usecases.models.delete.IDeleteChildModelSuspendUseCase
import kotlin.test.Test

class ExpireUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val codesInEmailsRepository = mockk<ICodesInEmailsRepository>()
        val deleteCodeInEmailUseCase = mockk<IDeleteCodeInEmailUseCase>(relaxed = true)
        val clientsInUsersRepository = mockk<IClientsInUsersRepository>()
        val deleteClientInUserUseCase = mockk<IDeleteAuthCodeUseCase>(relaxed = true)
        val listUsersLastLoggedBeforeUseCase = mockk<IListUsersLastLoggedBeforeUseCase>()
        val deleteUserUseCase = mockk<IDeleteChildModelSuspendUseCase<User, String, String>>(relaxed = true)
        val useCase = ExpireUseCase(
            codesInEmailsRepository,
            deleteCodeInEmailUseCase,
            clientsInUsersRepository,
            deleteClientInUserUseCase,
            listUsersLastLoggedBeforeUseCase,
            deleteUserUseCase
        )
        val now = Clock.System.now()
        val code = CodeInEmail("email", "code", "association", now)
        coEvery { codesInEmailsRepository.getCodesInEmailsExpiringBefore(now) } returns listOf(code)
        coEvery { clientsInUsersRepository.getExpiringBefore(now) } returns listOf()
        coEvery {
            listUsersLastLoggedBeforeUseCase(now.minus(1, DateTimeUnit.YEAR, TimeZone.currentSystemDefault()))
        } returns listOf()
        useCase(now)
        coVerify { deleteCodeInEmailUseCase(code.code) }
    }

}
