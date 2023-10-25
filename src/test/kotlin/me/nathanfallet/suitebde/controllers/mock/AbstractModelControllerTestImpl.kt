package me.nathanfallet.suitebde.controllers.mock

import io.ktor.http.*
import io.ktor.util.reflect.*
import me.nathanfallet.suitebde.controllers.AbstractModelController
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForDomainUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUserForCallUseCase

class AbstractModelControllerTestImpl(
    getAssociationForDomainUseCase: IGetAssociationForDomainUseCase,
    getUserForCallUseCase: IGetUserForCallUseCase
) : AbstractModelController<String, AbstractModelControllerBody, AbstractModelControllerBody>(
    "test",
    typeInfo<String>(),
    typeInfo<List<String>>(),
    typeInfo<AbstractModelControllerBody>(),
    typeInfo<AbstractModelControllerBody>(),
    getAssociationForDomainUseCase,
    getUserForCallUseCase
) {

    override suspend fun getAll(association: Association, user: User?): List<String> {
        if (user == null) throw ControllerException(HttpStatusCode.Unauthorized, "Mock error")
        return listOf("mock getAll")
    }

    override suspend fun get(association: Association, user: User?, id: String): String {
        if (user == null) throw ControllerException(HttpStatusCode.Unauthorized, "Mock error")
        return "mock get $id"
    }

    override suspend fun delete(association: Association, user: User?, id: String) {
        if (user == null) throw ControllerException(HttpStatusCode.Unauthorized, "Mock error")
    }

    override suspend fun update(
        association: Association,
        user: User?,
        id: String,
        obj: AbstractModelControllerBody
    ): String {
        if (user == null) throw ControllerException(HttpStatusCode.Unauthorized, "Mock error")
        return "mock put ${obj.value}"
    }

    override suspend fun create(association: Association, user: User?, obj: AbstractModelControllerBody): String {
        if (user == null) throw ControllerException(HttpStatusCode.Unauthorized, "Mock error")
        return "mock post ${obj.value}"
    }

}