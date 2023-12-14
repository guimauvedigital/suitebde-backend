package me.nathanfallet.suitebde.usecases.auth

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.ktorx.models.auth.ClientForUser
import me.nathanfallet.suitebde.models.application.Client
import me.nathanfallet.suitebde.models.users.ClientInUser
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.users.IClientsInUsersRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateAuthCodeUseCaseTest {

    @Test
    fun testInvoke() = runBlocking {
        val repository = mockk<IClientsInUsersRepository>()
        val useCase = CreateAuthCodeUseCase(repository)
        coEvery { repository.create("uid", "cid", any()) } returns ClientInUser(
            "code", "uid", "cid", Clock.System.now()
        )
        assertEquals(
            "code",
            useCase(
                ClientForUser(
                    Client("cid", "oid", "name", "description", "secret", "redirect"),
                    User("uid", "associationId", "email", "firstName", "lastName", "password", false)
                )
            )
        )
    }

    @Test
    fun testInvokeError() = runBlocking {
        val repository = mockk<IClientsInUsersRepository>()
        val useCase = CreateAuthCodeUseCase(repository)
        coEvery { repository.create("uid", "cid", any()) } returns null
        assertEquals(
            null,
            useCase(
                ClientForUser(
                    Client("cid", "oid", "name", "description", "secret", "redirect"),
                    User("uid", "associationId", "email", "firstName", "lastName", "password", false)
                )
            )
        )
    }

}
