package com.suitebde.usecases.application

import com.suitebde.models.associations.CodeInEmail
import com.suitebde.models.users.User
import com.suitebde.repositories.associations.ICodesInEmailsRepository
import com.suitebde.repositories.users.IClientsInUsersRepository
import com.suitebde.repositories.users.IResetsInUsersRepository
import com.suitebde.usecases.associations.IDeleteCodeInEmailUseCase
import com.suitebde.usecases.auth.IDeleteAuthCodeUseCase
import com.suitebde.usecases.auth.IDeleteResetPasswordUseCase
import com.suitebde.usecases.users.IListUsersLastLoggedBeforeUseCase
import dev.kaccelero.commons.repositories.IDeleteChildModelSuspendUseCase
import dev.kaccelero.models.UUID
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlin.test.Test

class ExpireUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val codesInEmailsRepository = mockk<ICodesInEmailsRepository>()
        val deleteCodeInEmailUseCase = mockk<IDeleteCodeInEmailUseCase>(relaxed = true)
        val resetsInUsersRepository = mockk<IResetsInUsersRepository>()
        val deleteResetInUserUseCase = mockk<IDeleteResetPasswordUseCase>(relaxed = true)
        val clientsInUsersRepository = mockk<IClientsInUsersRepository>()
        val deleteClientInUserUseCase = mockk<IDeleteAuthCodeUseCase>(relaxed = true)
        val listUsersLastLoggedBeforeUseCase = mockk<IListUsersLastLoggedBeforeUseCase>()
        val deleteUserUseCase = mockk<IDeleteChildModelSuspendUseCase<User, UUID, UUID>>(relaxed = true)
        val useCase = ExpireUseCase(
            codesInEmailsRepository,
            deleteCodeInEmailUseCase,
            resetsInUsersRepository,
            deleteResetInUserUseCase,
            clientsInUsersRepository,
            deleteClientInUserUseCase,
            listUsersLastLoggedBeforeUseCase,
            deleteUserUseCase
        )
        val now = Clock.System.now()
        val code = CodeInEmail("email", "code", UUID(), now)
        coEvery { codesInEmailsRepository.getCodesInEmailsExpiringBefore(now) } returns listOf(code)
        coEvery { clientsInUsersRepository.getExpiringBefore(now) } returns listOf()
        coEvery { resetsInUsersRepository.getExpiringBefore(now) } returns listOf()
        coEvery {
            listUsersLastLoggedBeforeUseCase(now.minus(1, DateTimeUnit.YEAR, TimeZone.currentSystemDefault()))
        } returns listOf()
        useCase(now)
        coVerify { deleteCodeInEmailUseCase(code.code) }
    }

}
