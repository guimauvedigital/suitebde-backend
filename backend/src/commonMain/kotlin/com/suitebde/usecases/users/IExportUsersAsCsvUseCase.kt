package com.suitebde.usecases.users

import com.suitebde.models.users.User
import dev.kaccelero.usecases.IPairSuspendUseCase

interface IExportUsersAsCsvUseCase : IPairSuspendUseCase<List<Pair<User, List<String>>>, List<String>, String> {

    suspend operator fun invoke(input: List<User>): String = invoke(input.map { it to emptyList() }, emptyList())

}
