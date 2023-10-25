package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.ISuspendUseCase

interface IGetUsersInAssociationUseCase : ISuspendUseCase<String, List<User>>