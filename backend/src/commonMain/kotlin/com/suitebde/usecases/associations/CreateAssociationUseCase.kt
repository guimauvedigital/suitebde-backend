package com.suitebde.usecases.associations

import com.suitebde.models.associations.Association
import com.suitebde.models.associations.CreateAssociationPayload
import com.suitebde.models.users.CreateUserPayload
import com.suitebde.models.users.User
import com.suitebde.models.web.CreateWebMenuPayload
import com.suitebde.models.web.WebMenu
import com.suitebde.models.web.WebPage
import com.suitebde.models.web.WebPagePayload
import com.suitebde.repositories.associations.IAssociationsRepository
import dev.kaccelero.commons.repositories.ICreateChildModelSuspendUseCase
import dev.kaccelero.commons.repositories.ICreateModelSuspendUseCase
import dev.kaccelero.models.UUID

class CreateAssociationUseCase(
    private val associationsRepository: IAssociationsRepository,
    private val createUserUseCase: ICreateChildModelSuspendUseCase<User, CreateUserPayload, UUID>,
    private val createWebPageUseCase: ICreateChildModelSuspendUseCase<WebPage, WebPagePayload, UUID>,
    private val createWebMenuUseCase: ICreateChildModelSuspendUseCase<WebMenu, CreateWebMenuPayload, UUID>,
) : ICreateModelSuspendUseCase<Association, CreateAssociationPayload> {

    override suspend fun invoke(input: CreateAssociationPayload): Association? =
        associationsRepository.create(input)?.also {
            createUserUseCase(
                CreateUserPayload(input.email, input.password, input.firstName, input.lastName, true),
                it.id
            )

            createWebPageUseCase(
                WebPagePayload("home", "Home", "Welcome to ${it.name}", true),
                it.id
            )
            createWebMenuUseCase(
                CreateWebMenuPayload("Home", "/pages/home", 1),
                it.id
            )
            createWebMenuUseCase(
                CreateWebMenuPayload("Events", "/events", 2),
                it.id
            )
            createWebMenuUseCase(
                CreateWebMenuPayload("Clubs", "/clubs", 3),
                it.id
            )
        }

}
