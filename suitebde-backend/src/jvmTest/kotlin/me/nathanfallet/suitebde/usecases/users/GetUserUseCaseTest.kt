package me.nathanfallet.suitebde.usecases.users

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.users.SubscriptionInUser
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.users.IUsersRepository
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class GetUserUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val repository = mockk<IUsersRepository>()
        val listSubscriptionsInUsersUseCase = mockk<IListChildModelSuspendUseCase<SubscriptionInUser, String>>()
        val useCase = GetUserUseCase(repository, listSubscriptionsInUsersUseCase)
        val user = User("id", "name", "email", "password", "first", "last", false)
        val subscriptionInUser = SubscriptionInUser("sub_id", "id", "sub_name", Clock.System.now(), Clock.System.now())
        coEvery { repository.get("id") } returns user
        coEvery { listSubscriptionsInUsersUseCase("id") } returns listOf(subscriptionInUser)
        assertEquals(user.copy(subscriptions = listOf(subscriptionInUser)), useCase("id"))
    }

    @Test
    fun invokeNotFound() = runBlocking {
        val repository = mockk<IUsersRepository>()
        val useCase = GetUserUseCase(repository, mockk())
        coEvery { repository.get("id") } returns null
        assertEquals(null, useCase("id"))
    }

}
