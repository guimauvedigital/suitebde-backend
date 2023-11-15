package me.nathanfallet.suitebde.usecases.web

import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.suitebde.repositories.web.IWebPagesRepository

class GetWebPagesUseCase(
    private val webPagesRepository: IWebPagesRepository
) : IGetWebPagesUseCase {

    override suspend fun invoke(input: String): List<WebPage> {
        return webPagesRepository.getWebPages(input)
    }

}
