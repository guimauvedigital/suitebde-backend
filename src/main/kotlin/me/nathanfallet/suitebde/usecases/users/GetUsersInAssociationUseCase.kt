package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.users.IUsersRepository

class GetUsersInAssociationUseCase(
    private val repository: IUsersRepository
) : IGetUsersInAssociationUseCase {

    override suspend fun invoke(input: String): List<User> {
        return repository.getInAssociation(input)
    }

}