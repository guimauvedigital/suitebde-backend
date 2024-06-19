package com.suitebde.usecases.associations

import com.suitebde.models.associations.Association
import com.suitebde.models.clubs.Club
import com.suitebde.models.users.OptionalUserContext
import com.suitebde.models.users.User
import com.suitebde.repositories.associations.IAssociationsRepository
import dev.kaccelero.commons.repositories.IDeleteChildModelSuspendUseCase
import dev.kaccelero.commons.repositories.IDeleteModelSuspendUseCase
import dev.kaccelero.commons.repositories.IListChildModelSuspendUseCase
import dev.kaccelero.commons.repositories.IListChildModelWithContextSuspendUseCase
import dev.kaccelero.models.UUID

class DeleteAssociationUseCase(
    private val repository: IAssociationsRepository,
    private val listUsersUseCase: IListChildModelSuspendUseCase<User, UUID>,
    private val deleteUserUseCase: IDeleteChildModelSuspendUseCase<User, UUID, UUID>,
    private val listClubsUseCase: IListChildModelWithContextSuspendUseCase<Club, UUID>,
    private val deleteClubUseCase: IDeleteChildModelSuspendUseCase<Club, UUID, UUID>,
) : IDeleteModelSuspendUseCase<Association, UUID> {

    override suspend fun invoke(input: UUID): Boolean {
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
