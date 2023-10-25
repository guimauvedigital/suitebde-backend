package me.nathanfallet.suitebde.controllers

import io.ktor.util.reflect.*
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForDomainUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUserForCallUseCase

class AbstractModelControllerTestImpl(
    getAssociationForDomainUseCase: IGetAssociationForDomainUseCase,
    getUserForCallUseCase: IGetUserForCallUseCase
) : AbstractModelController<String, String, String>(
    "test",
    typeInfo<String>(),
    typeInfo<List<String>>(),
    typeInfo<String>(),
    typeInfo<String>(),
    getAssociationForDomainUseCase,
    getUserForCallUseCase
) {

    override suspend fun getAll(association: Association, user: User?): List<String> {
        return listOf("mock")
    }

    override suspend fun get(association: Association, user: User?, id: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun delete(association: Association, user: User?, id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun update(association: Association, user: User?, id: String, obj: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun create(association: Association, user: User?, obj: String): String {
        TODO("Not yet implemented")
    }

}