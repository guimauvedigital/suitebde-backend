package me.nathanfallet.suitebde.usecases.web

import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.suitebde.repositories.web.IWebPagesRepository

class GetWebPageByUrlUseCase(
    private val repository: IWebPagesRepository
) : IGetWebPageByUrlUseCase {

    override suspend fun invoke(input1: String, input2: String): WebPage? {
        return repository.getByUrl(input1, input2)
    }

}
