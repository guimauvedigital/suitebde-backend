package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.users.OptionalUserContext
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.associations.IAssociationsRepository
import me.nathanfallet.usecases.models.delete.IDeleteChildModelSuspendUseCase
import me.nathanfallet.usecases.models.delete.IDeleteModelSuspendUseCase
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.context.IListChildModelWithContextSuspendUseCase

class DeleteAssociationUseCase(
    private val repository: IAssociationsRepository,
    private val listUsersUseCase: IListChildModelSuspendUseCase<User, String>,
    private val deleteUserUseCase: IDeleteChildModelSuspendUseCase<User, String, String>,
    private val listClubsUseCase: IListChildModelWithContextSuspendUseCase<Club, String>,
    private val deleteClubUseCase: IDeleteChildModelSuspendUseCase<Club, String, String>,
) : IDeleteModelSuspendUseCase<Association, String> {

    override suspend fun invoke(input: String): Boolean {
        return repository.delete(input).also { success ->
            if (!success) return@also

            // Delete association's entities (users, clubs, events, ...)
            listUsersUseCase(input).forEach { user ->
                deleteUserUseCase(user.id, input)
            }
            listClubsUseCase(input, OptionalUserContext()).forEach { club ->
                deleteClubUseCase(club.id, input)
            }

            // TODO: Add missing types
        }
    }

}
