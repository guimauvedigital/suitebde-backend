package me.nathanfallet.suitebde.usecases.web

import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.suitebde.repositories.web.IWebPagesRepository

class GetHomeWebPageUseCase(
    private val repository: IWebPagesRepository
) : IGetHomeWebPageUseCase {

    override suspend fun invoke(input: String): WebPage? {
        return repository.getHome(input)
    }

}
