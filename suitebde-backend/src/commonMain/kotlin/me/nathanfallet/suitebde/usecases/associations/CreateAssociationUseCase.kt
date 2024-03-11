package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.models.web.CreateWebMenuPayload
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.suitebde.models.web.WebPagePayload
import me.nathanfallet.suitebde.repositories.associations.IAssociationsRepository
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import me.nathanfallet.usecases.models.create.ICreateModelSuspendUseCase

class CreateAssociationUseCase(
    private val associationsRepository: IAssociationsRepository,
    private val createUserUseCase: ICreateChildModelSuspendUseCase<User, CreateUserPayload, String>,
    private val createWebPageUseCase: ICreateChildModelSuspendUseCase<WebPage, WebPagePayload, String>,
    private val createWebMenuUseCase: ICreateChildModelSuspendUseCase<WebMenu, CreateWebMenuPayload, String>,
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
