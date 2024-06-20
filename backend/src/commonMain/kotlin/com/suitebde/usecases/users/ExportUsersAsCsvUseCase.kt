package com.suitebde.usecases.users

import com.suitebde.models.users.SubscriptionInUser
import com.suitebde.models.users.User
import dev.kaccelero.commons.repositories.IListChildModelSuspendUseCase
import dev.kaccelero.models.UUID
import kotlinx.datetime.Clock

class ExportUsersAsCsvUseCase(
    private val listSubscriptionsInUsersUseCase: IListChildModelSuspendUseCase<SubscriptionInUser, UUID>,
) : IExportUsersAsCsvUseCase {

    override suspend fun invoke(input1: List<Pair<User, List<String>>>, input2: List<String>): String {
        val now = Clock.System.now()
        val columns = (listOf("Last name", "First name", "Active subscriptions") + input2)
            .joinToString(";", postfix = "\n")
        val usersList = input1.map { (user, extra) ->
            val subs = listSubscriptionsInUsersUseCase(user.id)
                .filter { it.endsAt >= now }.map { it.subscription?.name }.joinToString(", ")
            (listOf(user.lastName, user.firstName, subs) + extra).joinToString(";")
        }
        return usersList.joinToString("\n", columns)
    }

}
