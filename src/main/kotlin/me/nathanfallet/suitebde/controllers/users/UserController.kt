package me.nathanfallet.suitebde.controllers.users

import io.ktor.util.reflect.*
import me.nathanfallet.suitebde.controllers.AbstractModelController
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForDomainUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUserForCallUseCase

class UserController(
    getAssociationForDomainUseCase: IGetAssociationForDomainUseCase,
    getUserForCallUseCase: IGetUserForCallUseCase
) : AbstractModelController<User, Unit, Unit>(
    "users",
    typeInfo<User>(),
    typeInfo<Unit>(),
    typeInfo<Unit>(),
    getAssociationForDomainUseCase,
    getUserForCallUseCase
) {

    override suspend fun getAll(association: Association, user: User?): List<User> {
        TODO("Not yet implemented")
    }

    override suspend fun get(association: Association, user: User?, id: String): User {
        TODO("Not yet implemented")
    }

    override suspend fun create(association: Association, user: User?, obj: Unit): User {
        TODO("Not yet implemented")
    }

    override suspend fun update(association: Association, user: User?, id: String, obj: Unit): User {
        TODO("Not yet implemented")
    }

    override suspend fun delete(association: Association, user: User?, id: String) {
        TODO("Not yet implemented")
    }

}