package me.nathanfallet.suitebde.usecases.web

import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.suitebde.repositories.web.IWebMenusRepository

class GetWebMenusUseCase(
    private val repository: IWebMenusRepository
) : IGetWebMenusUseCase {

    override suspend fun invoke(input: String): List<WebMenu> {
        return repository.getMenus(input)
    }

}
