package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.base.IPairSuspendUseCase

interface IExportUsersAsCsvUseCase : IPairSuspendUseCase<List<Pair<User, List<String>>>, List<String>, String> {

    suspend operator fun invoke(input: List<User>): String = invoke(input.map { it to emptyList() }, emptyList())

}
