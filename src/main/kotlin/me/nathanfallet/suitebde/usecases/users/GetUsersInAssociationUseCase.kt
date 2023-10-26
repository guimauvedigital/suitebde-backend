package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.IUsersRepository

class GetUsersInAssociationUseCase(
    private val repository: IUsersRepository
) : IGetUsersInAssociationUseCase {

    override suspend fun invoke(input: String): List<User> {
        return repository.getUsersInAssociation(input)
    }

}